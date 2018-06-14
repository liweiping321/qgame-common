package com.mokylin.cfg;

import com.mokylin.cfg.util.CfgProviderUtil;
import com.mokylin.collection.IntHashMap;

import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 *
 * @author lip.li
 */
public abstract class IntCfgProvider< V extends BaseCfg<?, ?>> extends BaseCfgProvider<V> {

    protected transient IntHashMap<V> mapData;

    protected IntCfgProvider() {

    }

    public   void   addMultMap(IntHashMap<IntHashMap<V>> multiMap, int t ,V value){
        IntHashMap<V> map= multiMap.get(t);
        if(map==null){
            map=new IntHashMap<>();
            multiMap.put(t,map);
        }
        map.put(value.getIntKey(),value);

    }
    @Override
    public Collection<V> getConfigDatas() {
        return mapData.values();
    }

    public V getCfgByKey(int key) {
        return mapData.get(key);
    }


    public V get(int key) {
        return mapData.get(key);
    }
    @Override
    public void reLoad() {
        IntHashMap<V> tempMap = new IntHashMap<>();
        try {
            List<V> values = CfgProviderUtil.loadCfgs(getCfgClass());

            for (V v : values) {

                if (mapData != null && mapData.containsKey(v.getIntKey())) {
                    V oldV = mapData.get(v.getIntKey());
                    CfgType.copyProps(getCfgClass(), v, oldV);
                    tempMap.put(oldV.getIntKey(), oldV);

                } else {
                    tempMap.put(v.getIntKey(), v);
                }

            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage()+"|"+getName(), e);
            checkArgument(false, e.getMessage());
        }
        //替换掉原来旧的HashMap(防止reload时候HashMap触发死循环)
        mapData = tempMap;
    }

    public boolean isExist(int key) {
        return mapData.containsKey(key);
    }

    /**
     * 加载完成
     */
    @Override
    public  final void loadEnd() {
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

    public IntHashMap<V> getMapData() {
        return mapData;
    }
    @Override
    public void printLoadOver() {
        LOGGER.info("表{}：成功加载 {}条记录", getName(), mapData.size());
    }

}
