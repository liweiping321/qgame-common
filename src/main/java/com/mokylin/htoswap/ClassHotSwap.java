package com.mokylin.htoswap;

import com.mokylin.util.CollectionUtil;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 类热加载
 * Created by liweiping on 2018/6/8.
 */
public class ClassHotSwap implements Runnable {

    private static final String hotPath = "/data/tmp/hotswap";

    protected static final Logger LOGGER = LoggerFactory.getLogger(ClassHotSwap.class);

    @Override
    public void run() {

        File file = new File(hotPath);
        if (!file.exists() || !file.isDirectory()) {
            LOGGER.info(" hotPath not exist!");
            return;
        }
        Collection<File> fileList = FileUtils.listFiles(file, new String[]{"zip"}, true);
        if (CollectionUtil.isEmpty(fileList)) {
            return;
        }
        try {

            JavaHotAgent.init();

            reLoadClassZip(fileList);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            JavaHotAgent.destroy();
        }

    }

    private void reLoadClassZip(Collection<File> fileList) {

        for (File zipFile : fileList) {
            if (!zipFile.isFile() || !zipFile.canRead() || !zipFile.canWrite()) {
                LOGGER.error("file cant load  isFile:{},canRead:{},canWrite:{}", zipFile.isFile(), zipFile.canRead(), zipFile.canWrite());
            }

            String unzipPath = zipFile.getPath().substring(0, zipFile.getPath().lastIndexOf(File.separator)) + File.separator;
            String unzipName = zipFile.getPath().substring(0, zipFile.getPath().lastIndexOf("."));

            try {
                FileUtils.forceDelete(new File(unzipName));
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }


            upZip(unzipPath);

            reloadClassFile(unzipName);

            zipFile.delete();

        }
    }

    private void reloadClassFile(String unZipName) {
        Collection<File> classFiles = FileUtils.listFiles(new File(unZipName), new String[]{"class"}, true);
        if (!CollectionUtil.isEmpty(classFiles)) {
            return;
        }

        for (File classFile : classFiles) {
            InputStream bis = null;
            try {
                byte[] buffer = new byte[(int) classFile.length()];

                bis = new BufferedInputStream(new FileInputStream(classFile));
                bis.mark(buffer.length);
                bis.read(buffer);
                bis.reset();

                String classPath = getClassName(bis);
                if (StringUtils.isNotEmpty(classPath)) {
                    String className = classPath.replaceAll("/", ".");
                    loadClass(className, buffer);

                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            } finally {
                try {
                    if (null != bis) {
                        bis.close();
                    }
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 加载class字节码文件
     *
     * @param className
     * @param theClassFile
     * @return
     */
    public static void loadClass(String className, byte[] theClassFile) {
        try {
            Class<?> reloadClass = Class.forName(className.trim());
            JavaHotAgent.load(reloadClass, theClassFile);
            LOGGER.error("AgentClassReloader succ, className:{}", className);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            LOGGER.error("AgentClassReloader fail, className:{}", className);
        }

    }

    private void upZip(String unzipPath) {
        try {
            File unZipFile = new File(unzipPath);
            ZipFile tempZipFile = new ZipFile(unZipFile, Charset.forName("utf8"));
            Enumeration e = tempZipFile.entries();
            while (e.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) e.nextElement();
                String name = zipEntry.getName();
                if (zipEntry.isDirectory()) {
                    name = name.substring(0, name.length() - 1);
                    File newFile = new File(unzipPath + File.separator + name);
                    newFile.mkdirs();
                } else {
                    File newFile = new File(unzipPath + File.separator + name);
                    newFile.getParentFile().mkdirs();
                    newFile.createNewFile();
                    InputStream in = tempZipFile.getInputStream(zipEntry);
                    FileOutputStream out = new FileOutputStream(newFile);
                    int length = 0;
                    byte[] readByte = new byte[1024];
                    try {
                        while ((length = in.read(readByte, 0, 1024)) != -1) {
                            out.write(readByte, 0, length);
                        }
                    } catch (Exception e2) {
                        LOGGER.error("解压文件失败!", e2);
                    } finally {
                        in.close();
                        out.close();
                    }
                }
            }

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }


    /**
     * @param ins
     * @return
     * @throws IOException
     */
    public static String getClassName(InputStream ins) throws IOException {
        DataInputStream din = new DataInputStream(ins);

        if (din.readInt() != 0xCAFEBABE) {
            // 不是一个.class文件
            throw new IOException("Not a class file");
        }
        din.readUnsignedShort();  // 次版本号
        din.readUnsignedShort();  // 主版本号
        din.readUnsignedShort();  // 常量池的数量
        din.readByte();           // CLASS=7
        din.readUnsignedShort();  // 忽略这个地方
        din.readByte();           // UTF8=1
        String name = din.readUTF();// 类的名字!!!
        return name;
    }
}
