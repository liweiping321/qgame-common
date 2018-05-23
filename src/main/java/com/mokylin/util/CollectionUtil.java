package com.mokylin.util;

import com.alibaba.fastjson.util.TypeUtils;
import com.mokylin.cfg.BaseCfg;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author lip.li
 *
 */
public class CollectionUtil {

    public static <T> List<T> convertList(List<String> values, Class<T> castType) {
        List<T> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(values)) {
            for (String value : values) {
                if (StringUtils.isBlank(value)) {
                    continue;
                }
                list.add(TypeUtils.castToJavaBean(value, castType));
            }
        }
        return list;
    }

    public static <T> List<T> convertList(List<String> values, Class<T> castType,Object defautValue) {
        List<T> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(values)) {
            for (String value : values) {
                if (StringUtils.isBlank(value)) {
                    list.add(TypeUtils.castToJavaBean(defautValue, castType));
                }else{
                    list.add(TypeUtils.castToJavaBean(value, castType));
                }

            }
        }
        return list;
    }



    public static <T> Set<T> convertSet(List<String> values, Class<T> castType) {
        Set<T> set = new LinkedHashSet<>(values.size());
        if (CollectionUtils.isNotEmpty(values)) {
            for (String value : values) {
                if (StringUtils.isBlank(value)) {
                    continue;
                }
                set.add(TypeUtils.castToJavaBean(value, castType));
            }
        }
        return set;
    }

    public static <K, V> Map<K, V> convertMap(Map<String, String> maps, Class<K> keyType,
        Class<V> valueType) {
        Map<K, V> returnMap = new HashMap<>(maps.size());
        if (maps != null && maps.size() > 0) {
            for (Map.Entry<String, String> entry : maps.entrySet()) {
                if (StringUtils.isBlank(entry.getKey()) || StringUtils.isBlank(entry.getValue())) {
                    continue;
                }
                K key = TypeUtils.castToJavaBean(entry.getKey(), keyType);
                V value = TypeUtils.castToJavaBean(entry.getValue(), valueType);
                returnMap.put(key, value);
            }
        }
        return returnMap;
    }

    public static <K,V> Map<K,V> convertMap(String content,Class<K> keyType, Class<V> valueType,String splitRex,String splitRex1){
        Map<K, V> returnMap = new HashMap<>(3);
        if(!StringUtils.isEmpty(content)){
           String [] valueStrs= content.split(splitRex);

           for(String valueStr:valueStrs){
               String[] items=valueStr.split(splitRex1);
               K key = TypeUtils.castToJavaBean(items[0], keyType);
               V value = TypeUtils.castToJavaBean(items[1], valueType);
               returnMap.put(key, value);
           }
        }
        return returnMap;
    }


    public static Map<Integer,Integer> convertMap(String content,String splitRex,String splitRex1){
        return convertMap(content,Integer.class,Integer.class,splitRex,splitRex1);
    }

    public static boolean isEmpty(Collection<?> collections){
        return collections==null||collections.isEmpty();
    }

    public static boolean isEmpty(Map<?,?> map){
        return map==null||map.isEmpty();
    }



}
