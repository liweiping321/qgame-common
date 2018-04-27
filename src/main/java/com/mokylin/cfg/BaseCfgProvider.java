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
public abstract class BaseCfgProvider<Key, V extends BaseCfg<Key, ?>> {

    public Object services;

    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseCfgProvider.class);

    protected transient  Map<Key, V> mapData;

    public boolean isFirstLoad = true;

    protected BaseCfgProvider() {

    }

    public Collection<V> getConfigDatas() {
        return mapData.values();
    }

    public V getCfgByKey(Key key) {
        return mapData.get(key);
    }


    @SuppressWarnings("unchecked")
    public void reLoad() {
        Map<Key, V> tempMap = new HashMap<>();
        try {
            List<V> values = CfgProviderUtil.loadCfgs(getCfgClass());

            for (V v : values) {

                if (mapData != null && mapData.containsKey(v.getKey())) {
                    V oldV = mapData.get(v.getKey());
                    CfgType.copyProps(getCfgClass(), v, oldV);
                    tempMap.put(oldV.getKey(), oldV);

                } else {
                    tempMap.put(v.getKey(), v);
                }

            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage()+"|"+getName(), e);
            checkArgument(false, e.getMessage());
        }
        //替换掉原来旧的HashMap(防止reload时候HashMap触发死循环)
        mapData = tempMap;
    }

    public String getName() {
        return CfgType.getCfgType(getCfgClass()).getFileName();
    }

    public abstract Class<V> getCfgClass();

    public boolean isExist(Key key) {
        return mapData.containsKey(key);
    }

    /**
     * 加载完成
     */
    public  final void loadEnd() {
        isFirstLoad = false;
        if(mapData==null){
            return;
        }
        for (V v : mapData.values()) {
            afterLoad(v);
        }

        afterLoadAll();

    }


    protected abstract void afterLoad(V v);

    protected abstract void afterLoadAll();

    public Map<Key, V> getMapData() {
        return mapData;
    }

    protected void printlnCheckInfo(String fieldName, Object value, String otherFile) {
        LOGGER.info("表中的{}：{}在{}中不存在", getName(), fieldName, value, otherFile);

    }

    protected void printlnNotValidate(String fieldName, Object value) {
        LOGGER.info("表中的{}：{}不合法数据", getName(), fieldName, value);
    }

    public void printLoadOver() {
        LOGGER.info("表{}：成功加载 {}条记录", getName(), mapData.size());

    }


    public  void encode4Config(GeneratedMessage.Builder builder){

    };
}
