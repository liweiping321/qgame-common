package com.mokylin.cfg;

import com.mokylin.cfg.util.CfgProviderUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 *
 * @author lip.li
 */
public abstract class CommonCfgProvider <Key, V extends BaseCfg<Key, ?>> extends BaseCfgProvider<V> {

    protected transient  Map<Key, V> mapData;

    protected CommonCfgProvider() {

    }
    public <T> void   addMultMap(Map<T,Map<Key,V>> multiMap,T t ,V value){
        Map<Key,V> map= multiMap.get(t);
        if(map==null){
            map=new HashMap<>();
            multiMap.put(t,map);
        }
        map.put(value.getKey(),value);

    }
    @Override
    public Collection<V> getConfigDatas() {
        return mapData.values();
    }

    public V getCfgByKey(Key key) {
        return mapData.get(key);
    }

    public V get(Key key) {
        return mapData.get(key);
    }

    @Override
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


    public boolean isExist(Key key) {
        return mapData.containsKey(key);
    }

    /**
     * 加载完成
     */
    @Override
    public   void loadEnd() {
        isFirstLoad = false;
        if(mapData==null){
            return;
        }
        for (V v : mapData.values()) {
            afterLoad(v);
            v.configValue=null;
        }

        afterLoadAll();

    }


    public Map<Key, V> getMapData() {
        return mapData;
    }
    @Override
    public void printLoadOver() {
        LOGGER.info("表{}：成功加载 {}条记录", getName(), mapData.size());

    }

}
