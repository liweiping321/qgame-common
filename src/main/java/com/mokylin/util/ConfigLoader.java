package com.mokylin.util;

import com.google.common.io.ByteStreams;
import com.google.inject.Inject;

import com.mokylin.game.utils.config.Config;
import com.mokylin.game.utils.config.parser.CfgParserKv;
import com.mokylin.game.utils.config.parser.CfgParserTsv;
import com.mokylin.game.utils.io.loader.FileLoader;
import com.mokylin.game.utils.io.loader.FileLoaderBytes;
import com.mokylin.game.utils.io.loader.FileLoaderOS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/** Created by wyt on 16-12-5. */
public class ConfigLoader {

    private static final Logger logger = LoggerFactory.getLogger(ConfigLoader.class);

    public static ConfigLoader of(FileLoader fileLoader) {
        return new ConfigLoader(fileLoader);
    }

    public static final String packedCfgName = "config.pk";

    private FileLoader fileLoader;

    @Inject
    public ConfigLoader() {
        File file = new File("config");
        if (file.exists()) {
            checkArgument(file.isDirectory(), "检测到config，但居然不是文件夹！%s", file.getAbsolutePath());
            this.fileLoader = FileLoaderOS.of("");
        } else {
            InputStream is = ClassLoader.getSystemResourceAsStream(packedCfgName);
            byte[] data;
            try {
                data = ByteStreams.toByteArray(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.fileLoader = FileLoaderBytes.of(data);
        }
    }

    private ConfigLoader(FileLoader fileLoader) {
        this.fileLoader = fileLoader;
    }

    public List<Config> load(String path) {
        return load(path, false); // TODO 妥协改成了false，这样ok？
    }

    public List<Config> load(String path, boolean mustExists) {
        //    logger.info("load: {}", path);
        String s = fileLoader.fileToStr(path, mustExists);
        if (s != null) {
            return CfgParserTsv.parse(s, path);
        }
        return Collections.emptyList();
    }

    public Config loadKV(String path) {
        return loadKV(path, true);
    }

    public Config loadKV(String path, boolean mustExists) {
        String s = fileLoader.fileToStr(path, mustExists);
        if (s != null) {
            return CfgParserKv.parse(s, path);
        }
        return Config.EMPTY;
    }

    public String loadFile(String file) {
        return fileLoader.fileToStr(file, false);
    }

    public Map<String, String> loadDir(String dir) {
        return fileLoader.dirToStr(dir, false);
    }

    public FileLoader getFileLoader() {
        return fileLoader;
    }

    public void clean() {
        fileLoader.clean();
        fileLoader = null;
    }
}
