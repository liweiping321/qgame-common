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

    protected Map<String, BaseCfgProvider<?, ?>> cfgProviderMap = new LinkedHashMap<>();

    protected Map<Class<? extends BaseCfgProvider>, BaseCfgProvider<?, ?>> clazzProviderMap = new LinkedHashMap<>();


    public BaseCfgProviders() {
        init();
    }

    protected void register(BaseCfgProvider<?, ?> provider) {
        cfgProviderMap.put(provider.getName(), provider);

    }

    private void init() {

        registers();

        loadData();
    }

    public void registers() {
    }


    public void loadData() {
        for (BaseCfgProvider<?, ?> provider : cfgProviderMap.values()) {
            provider.reLoad();
            provider.loadEnd();
        }

        for (BaseCfgProvider<?, ?> provider : cfgProviderMap.values()) {
            provider.printLoadOver();
        }
    }

    public void loadData(String names[]) {
        for (String table : names) {

            BaseCfgProvider<?, ?> provider = cfgProviderMap.get(table);
            if (null != provider) {
                provider.reLoad();
            }
        }

        for (String table : names) {

            BaseCfgProvider<?, ?> provider = cfgProviderMap.get(table);
            if (null != provider) {
                provider.loadEnd();

                provider.printLoadOver();
            }
        }

    }

    public <T extends BaseCfgProvider> T getProvider(Class<T> providerClazz){
        return (T)clazzProviderMap.get(providerClazz);
    }


    public void encode4Config( GeneratedMessage.Builder buidler ) {
        for (BaseCfgProvider provider : cfgProviderMap.values()) {
            provider.encode4Config(buidler);
        }
    }

}
