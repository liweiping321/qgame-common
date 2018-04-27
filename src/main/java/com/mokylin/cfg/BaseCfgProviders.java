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


    public Object services;

    protected Map<String, BaseCfgProvider<?, ?>> cfgProviderMap = new LinkedHashMap<>();

    protected Map<Class<? extends BaseCfgProvider>, BaseCfgProvider<?, ?>> clazzProviderMap = new LinkedHashMap<>();

    private GeneratedMessage cacheConfig;

    public BaseCfgProviders() {

    }

    protected void register(BaseCfgProvider<?, ?> provider) {
        cfgProviderMap.put(provider.getName(), provider);
        provider.services=services;
    }

    public void init(Object services) {
        this.services=services;
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

        cacheConfig = encode4Config();
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

        cacheConfig = encode4Config();
    }

    public <T extends BaseCfgProvider> T getProvider(Class<T> providerClazz){
        return (T)clazzProviderMap.get(providerClazz);
    }
    public abstract GeneratedMessage.Builder getMessageBuilder();

    public GeneratedMessage getConfigMessage() {
        if (cacheConfig == null) {
            cacheConfig = encode4Config();
        }
        return cacheConfig;
    }

    public GeneratedMessage encode4Config() {
        GeneratedMessage.Builder builder = getMessageBuilder();
        for (BaseCfgProvider provider : cfgProviderMap.values()) {
            provider.encode4Config(builder);
        }
        return (GeneratedMessage)builder.build();

    }

}
