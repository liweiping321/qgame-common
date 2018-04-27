package com.mokylin.cfg.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by liweiping on 2018/3/22.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CfgField {

    /**
     * 属性对应的数据库表列名
     */
    public String name() default "";

    /** List,Set,Map元素分割符 */
    public String splitRex() default "|";

    /** Map key,Value分割符 */
    public String splitRex1() default "#";

    /**是否是数组*/
    public boolean array() default false;

    /**默认值*/
    public String defaultValue() default "";

    /**是否必填的*/
    public boolean required() default false;
}
