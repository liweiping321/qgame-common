package com.mokylin.cfg;

/**
 * Created by liweiping on 2018/7/16.
 */
public interface IParser {

     static  <T> T defaultParse(String value,Class<T> clazz){
        try{
            return  clazz.cast(clazz.getConstructor(String.class).newInstance(value));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
