package com.mokylin.cfg;

import com.google.protobuf.GeneratedMessage;

import com.mokylin.cfg.util.CfgProviderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 *
 * @author lip.li
 */
public abstract class BaseCfgProvider< V extends BaseCfg<?, ?>> {

    public Object services;

    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseCfgProvider.class);

    public boolean isFirstLoad = true;

    protected BaseCfgProvider() {

    }


    public abstract Collection<V> getConfigDatas();



    public abstract  void reLoad();

    public String getName() {
        return CfgType.getCfgType(getCfgClass()).getFileName();
    }

    public abstract Class<V> getCfgClass();

    public  abstract void loadEnd() ;

    protected abstract void afterLoad(V v);

    protected abstract void afterLoadAll();

    protected void printlnCheckInfo(String fieldName, Object value, String otherFile) {
        LOGGER.info("表中的{}：{}在{}中不存在", getName(), fieldName, value, otherFile);

    }
    protected void printlnNotValidate(String fieldName, Object value) {
        LOGGER.info("表中的{}：{}不合法数据", getName(), fieldName, value);
    }
    public abstract  void printLoadOver();

    public  void encode4Config(GeneratedMessage.Builder builder){
    }
}
