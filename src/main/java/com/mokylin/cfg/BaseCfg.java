package com.mokylin.cfg;

import com.google.protobuf.GeneratedMessage;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by liweiping on 2018/3/22.
 */
public abstract class BaseCfg<Key, V> {

    public  Key getKey(){
        return null;
    }

    public  GeneratedMessage encode4Config(){
        return null;
    }

    public String getName(){
        return "";
    }
    @Override
    public String toString() {
        return getKey() + "-" + getName();
    }

    public int defaultInt(){
        return 0;
    }

    public boolean defaultBoolean(){
        return false;
    }

    public String defaultString(){
        return "";
    }

    public Date defaultDate(){
        return null;
    }

    public float defaultDouble(){
        return 0;
    }

    public List<?> defaultList(){
        return null;
    }

    public Map<?,?> defaultMap(){
        return null;
    }

    public Set<?> defaultSet(){
        return null;
    }


    public long defaultLong( long value) {
        return value;
    }

    public long defaultLong(){
        return 0;
    }
    public int defaultInt(int value){

        return value;
    }


    public boolean defaultBoolean(boolean value){
        return value;
    }

    public String defaultString(String value){
        return value;
    }

    public Date defaultDate(Date value){
        return value;
    }

    public float defaultDouble(double value){
        return (float) value;
    }

    public List<?> defaultList(List<?> value){
        return value;
    }

    public Map<?,?> defaultMap(Map<?,?> value){
        return value;
    }

    public Set<?> defaultSet(Set<?> value){
        return value;
    }

}
