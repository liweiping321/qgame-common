package com.mokylin.htoswap;

import com.google.common.base.Preconditions;

import com.sun.tools.attach.VirtualMachine;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.net.URLDecoder;

/**
 * Created by liweiping on 2018/6/8.
 */
public class JavaHotAgent {

    public static final Logger logger = LoggerFactory.getLogger(JavaHotAgent.class);

    private static String classesPath;
    private static String jarPath;
    private static VirtualMachine vm;
    private static String pid;

    static {
        classesPath = JavaHotAgent.class.getClassLoader().getResource("").getPath();
        logger.error("java agent:classpath:{}", classesPath);
        jarPath = getJarPath();
        logger.error("java agent:jarPath:{}", jarPath);

        // 当前进程pid
        String name = ManagementFactory.getRuntimeMXBean().getName();
        pid = name.split("@")[0];
        logger.error("当前进程pid：{}", pid);
    }

    /**
     * 获取jar包路径
     * @return
     */
    public static String getJarPath() {
        // StringUtils是jar文件内容
        URL url = StringUtils.class.getProtectionDomain().getCodeSource().getLocation();
        String filePath = null;
        try {
            filePath = URLDecoder.decode(url.getPath(), "utf-8");// 转化为utf-8编码
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (filePath.endsWith(".jar")) {// 可执行jar包运行的结果里包含".jar"
            // 截取路径中的jar包名
            filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
        }

        File file = new File(filePath);

        filePath = file.getAbsolutePath();//得到windows下的正确路径
        return filePath;
    }

    public static void init() {
        try{
            // 虚拟机加载
            vm = VirtualMachine.attach(pid);
            vm.loadAgent(jarPath + "/javaagent.jar");

            Instrumentation instrumentation = JavaDynAgent.getInstrumentation();
            Preconditions.checkNotNull(instrumentation, "initInstrumentation must not be null");
        }catch (Exception e){

        }

    }

    public static void destroy()  {
        try{
            if (vm != null) {
                vm.detach();
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }

    }


    public static void load(Class<?> theClass, byte[] theClassFile) throws ClassNotFoundException, UnmodifiableClassException {
        ClassDefinition definition = new ClassDefinition(theClass, theClassFile);
        JavaDynAgent.getInstrumentation().redefineClasses(new ClassDefinition[]{definition});
        logger.info("hot load class:{} success",theClass.getName());

    }

}
