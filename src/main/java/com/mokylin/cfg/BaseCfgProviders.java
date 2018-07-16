package com.mokylin.cfg;

import com.google.protobuf.GeneratedMessage;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * 配置数据实体提供者集合
 *
 * @author lip.li
 *
 */
public abstract class BaseCfgProviders {

    protected Map<String, BaseCfgProvider< ?>> cfgProviderMap = new LinkedHashMap<>();

    protected CfgHotReload hotReload;

    public BaseCfgProviders() {
       // init();
        hotReload=new CfgHotReload(this);
    }

    protected void register(BaseCfgProvider< ?> provider) {
        cfgProviderMap.put(provider.getName(), provider);

    }

    public void init() {

        registers();

        loadData();
    }

    public void registers() {
    }


    public void loadData() {
        for (BaseCfgProvider< ?> provider : cfgProviderMap.values()) {
            provider.reLoad();
            provider.loadEnd();
        }
        for (BaseCfgProvider< ?> provider : cfgProviderMap.values()) {

            provider.printLoadOver();
        }
    }

    public void loadData(String names[]) {
        for (String table : names) {

            BaseCfgProvider< ?> provider = cfgProviderMap.get(table);
            if (null != provider) {
                provider.reLoad();
                provider.loadEnd();

            }
        }

        for (String table : names) {

            BaseCfgProvider< ?> provider = cfgProviderMap.get(table);
            if (null != provider) {
                provider.printLoadOver();
            }
        }

    }

    public void encode4Config( GeneratedMessage.Builder buidler ) {
        for (BaseCfgProvider provider : cfgProviderMap.values()) {
            provider.encode4Config(buidler);
        }
    }

}
