package com.mokylin.cfg.util;

import com.mokylin.cfg.BaseCfg;
import com.mokylin.cfg.CfgFieldType;
import com.mokylin.cfg.CfgType;
import com.mokylin.game.utils.config.Config;
import com.mokylin.util.ConfigLoader;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by liweiping on 2018/3/22.
 */
public class CfgProviderUtil {

    public static final ConfigLoader go = new ConfigLoader();


    public static <T extends BaseCfg> List<T> loadCfgs(Class<T> clazz) throws Exception {

        CfgType cfgType = CfgType.getCfgType(clazz);

        List<Config> configs = go.load(cfgType.getFileName());
        if(configs.size()<2)
        {
            return new ArrayList<>();
        }
        Config configType = configs.get(1);

        List<T> cfgs = new ArrayList<>(configs.size() - 2);
        for (int i = 2; i < configs.size(); i++) {
            Config config = configs.get(i);
            Constructor  constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            T cfgObj = (T) constructor.newInstance();

            Collection<CfgFieldType> cfgFieldTypes = cfgType.getCfgFieldTypes();
            for (CfgFieldType cfgFieldType : cfgFieldTypes) {
                cfgFieldType.setValue(cfgObj, config, configType);
            }
            cfgs.add(cfgObj);

        }

        return cfgs;
    }
}
