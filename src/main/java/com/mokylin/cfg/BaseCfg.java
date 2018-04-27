package com.mokylin.cfg;

import com.google.protobuf.GeneratedMessage;

/**
 * Created by liweiping on 2018/3/22.
 */
public abstract class BaseCfg<Key, V> {

    public abstract Key getKey();

    public abstract GeneratedMessage encode4Config();

    public String getName(){
        return "";
    }
    @Override
    public String toString() {
        return getKey() + "-" + getName();
    }

}
