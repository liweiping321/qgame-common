package com.mokylin.cfg;

import com.mokylin.cfg.anno.Cfg;
import com.mokylin.cfg.anno.CfgField;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by liweiping on 2018/3/22.
 */
public class CfgType<T extends BaseCfg> {

    private static Map<Class<?>, CfgType> cfgTypeMap = new HashMap<>();

    public final Class<T> clazz;

    private String fileName;

    private Map<String, CfgFieldType> fieldMap = new HashMap<>();

    public CfgType(Class<T> clazz) {
        this.clazz = clazz;
        init();
    }

    private void init() {
        Cfg cfg = clazz.getAnnotation(Cfg.class);
        if (cfg != null) {
            fileName = cfg.config();
            if (StringUtils.isEmpty(fileName)) {
                throw new RuntimeException(clazz.getName() + " Cfg fileName is empty!");
            }

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                CfgField cfgField = field.getAnnotation(CfgField.class);
                if (Objects.nonNull(cfgField)) {
                    field.setAccessible(true);
                    String fieldName =
                        StringUtils.isNotEmpty(cfgField.name()) ? cfgField.name() : field.getName();
                    CfgFieldType cfgFieldType = new CfgFieldType(fieldName, cfgField, field);

                    fieldMap.put(fieldName, cfgFieldType);
                }
            }
        } else {
            checkArgument(true, "Cfg error class Type:%s", clazz.getName());

        }
    }

    public static <T extends BaseCfg> Map<String, CfgFieldType> getClazzFieldMap(Class<T> clazz) {
        return getCfgType(clazz).fieldMap;
    }

    public static CfgType getCfgType(Class<?> clazz) {
        CfgType cfgType = cfgTypeMap.get(clazz);
        if (cfgType == null) {

            cfgType = new CfgType(clazz);
            cfgTypeMap.put(clazz, cfgType);
        }
        return cfgType;
    }

    public static <T extends BaseCfg> void copyProps(Class<T> clazz, Object src, Object dest)
        throws IllegalAccessException {
        Collection<CfgFieldType> fieldTypes = getCfgType(clazz).fieldMap.values();
        for (CfgFieldType cfgFieldType : fieldTypes) {
            Object value = cfgFieldType.field.get(src);
            cfgFieldType.field.set(dest, value);
        }
    }
    public CfgFieldType getCfgFieldType(String name){
        return fieldMap.get(name);
    }

    public String getFileName() {
        return fileName;
    }

    public Collection<CfgFieldType> getCfgFieldTypes() {
        return fieldMap.values();
    }
}