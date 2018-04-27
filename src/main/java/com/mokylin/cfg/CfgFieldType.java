package com.mokylin.cfg;

import com.google.common.base.Splitter;

import com.alibaba.fastjson.util.TypeUtils;
import com.mokylin.cfg.anno.CfgField;
import com.mokylin.game.utils.config.Config;
import com.mokylin.util.CollectionUtil;
import com.mokylin.util.TimeUtils;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by liweiping on 2018/3/22.
 */
public class CfgFieldType {

    /**
     * 属性对应的csv列名
     */
    public final String name;

    /** List,Set,Map元素分割符 */
    public final String splitRex;

    /** Map key,Value分割符 */
    public final String splitRex1;

    public final boolean array;

    public final Field field;

    public final Type[] actualTypes;

    public final Class<?> fieldType;

    public final String defaultValue;

    public final boolean required;

    public CfgFieldType(String name, CfgField cfgField, Field field) {
        this.name = name;
        this.splitRex = cfgField.splitRex();
        this.splitRex1 = cfgField.splitRex1();
        this.array = cfgField.array();
        this.field = field;
        this.field.setAccessible(true);
        this.fieldType = field.getType();
        this.defaultValue = cfgField.defaultValue();
        this.required = cfgField.required();
        Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            actualTypes = pType.getActualTypeArguments();
        } else {
            actualTypes = null;
        }
    }

    public <T extends BaseCfg> void setValue(T cfgObj, Config config, Config txtType)
        throws Exception {
        String tType;
        Object valueObj = null;
        if (array) {
            tType = txtType.getStrList(name).get(0);
            List<String> valueStrs = config.getStrList(name);
            valueObj = CollectionUtil.convertList(valueStrs, (Class) actualTypes[0],getDefaultValueObj((Class) actualTypes[0],tType));
        } else {
            tType = txtType.getStr(name);
            String valueStr = StringUtils.trim(config.getStr(name));
            if (StringUtils.isEmpty(valueStr)) {
                valueStr = defaultValue;
            }

            if (fieldType == String.class) {
                valueObj = valueStr;
            } else if (StringUtils.isNotEmpty(valueStr)) {
                if (isJavaDataType()) {
                    if(fieldType==boolean.class||fieldType==Boolean.class){
                        if("0".equals(valueStr)) {
                            valueStr = "false";
                        }
                    }
                    valueObj = TypeUtils.castToJavaBean(valueStr, fieldType);
                } else if (fieldType == Date.class) {
                    if ("date".equalsIgnoreCase(tType)) {
                        valueObj = TimeUtils.YMD_FORMAT.parse(valueStr);
                    } else if ("date_time".equalsIgnoreCase(tType)) {
                        valueObj = TimeUtils.YMDHMSS_FORMAT.parse(valueStr);
                    } else {
                        checkArgument(true, "column:%s nonsupport data type :%s,className:%s,",
                            name, tType, cfgObj.getClass().getName());

                    }

                } else if (fieldType == List.class) {
                    List<String> strings =
                        Splitter.on(splitRex).omitEmptyStrings().splitToList(valueStr);
                    valueObj = CollectionUtil.convertList(strings, (Class<?>) actualTypes[0]);
                } else if (fieldType == Set.class) {
                    List<String> strings =
                        Splitter.on(splitRex).omitEmptyStrings().splitToList(valueStr);
                    valueObj = CollectionUtil.convertSet(strings, (Class<?>) actualTypes[0]);
                } else if (fieldType == Map.class) {
                    Map<String, String> maps =
                        Splitter.on(splitRex).withKeyValueSeparator(splitRex1).split(valueStr);
                    valueObj = CollectionUtil
                        .convertMap(maps, (Class<?>) actualTypes[0], (Class<?>) actualTypes[1]);
                } else {
                    checkArgument(true, "column:%s ,nonsupport data type :%s,className:%s", name,
                        tType, cfgObj.getClass().getName());
                }
            }

        }


        if (valueObj == null && required) {

            checkArgument(true, "column:%s can't null :%s,className:%s", name, tType,
                cfgObj.getClass().getName());
        }

        //临时设置默认值
        if (valueObj == null) {
            valueObj = getDefaultValueObj(fieldType, tType);
        }

        field.set(cfgObj, valueObj);


    }

    private Object getDefaultValueObj(Class<?> fieldType, String tType) {
        if (fieldType == String.class) {
            return "";
        } else if (fieldType == boolean.class || fieldType == Boolean.class) {
            return false;
        } else if (fieldType == byte.class || fieldType == Byte.class) {
            return 0;
        } else if (fieldType == short.class || fieldType == Short.class) {
            return 0;
        } else if (fieldType == int.class || fieldType == Integer.class) {
            return 0;
        } else if (fieldType == long.class || fieldType == Long.class) {
            return 0L;
        } else if (fieldType == float.class || fieldType == Float.class) {
            return 0F;
        } else if (fieldType == Double.class || fieldType == double.class) {
            return 0;
        } else if (fieldType == Set.class) {
            return new HashSet<>(0);
        } else if (fieldType == Map.class) {
            return new HashMap<>(0);
        } else if (fieldType == List.class) {
            return new ArrayList<>(0);
        } else if (fieldType == Date.class) {
            if ("date".equalsIgnoreCase(tType)) {
                return TimeUtils.DEFAULT_YMD;
            } else if ("date_time".equalsIgnoreCase(tType)) {
                return TimeUtils.DEFAULT_YMDHMSS;
            }
        }
        return null;
    }

    private boolean isJavaDataType() {
        return fieldType == boolean.class || fieldType == byte.class || fieldType == short.class ||
            fieldType == int.class || fieldType == long.class || fieldType == double.class ||
            fieldType == float.class || fieldType == Boolean.class || fieldType == Byte.class ||
            fieldType == Short.class || fieldType == Integer.class || fieldType == Long.class ||
            fieldType == Double.class || fieldType == Float.class;
    }
}
