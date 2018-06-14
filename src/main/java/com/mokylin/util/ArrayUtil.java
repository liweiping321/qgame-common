package com.mokylin.util;

import com.alibaba.fastjson.JSON;
import com.mokylin.consts.Splitable;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

/**
 * Created by liweiping on 2018/6/22.
 */
public class ArrayUtil {

    public  static int [] str2intArray(String str){
        return str2intArray(str, Splitable.HUO);
    }
    public static int[] str2intArray(String str, String separator) {
        String[] strArray = StringUtils.split(str, separator);
        if (strArray == null || strArray.length == 0) {
            return  new int[0];
        }

        int[] array = new int[strArray.length];
        for (int i = 0; i < strArray.length; i++) {
            array[i] = Integer.parseInt(strArray[i]);
        }
        return array;
    }
    public static int[][] str2intArray2(String str){
        return str2intArray2(str,Splitable.HUO,Splitable.JINGHAO);
    }
    public static int[][] str2intArray2(String str, String separator1,String separator2) {

        String[] strArray = StringUtils.split(str, separator1);
        if (strArray == null || strArray.length == 0) {
            return new int[0][0];
        }
        int [][] intArray=new int[strArray.length][];
        for(int i=0;i<intArray.length;i++){
            intArray[i]=str2intArray(strArray[i],separator2);
        }
        return intArray;
    }

    public static long[] str2longArray(String str){
        return  str2longArray(str,Splitable.HUO);
    }


    public static long[] str2longArray(String str, String separator) {
        String[] strArray = StringUtils.split(str, separator);
        if (strArray == null || strArray.length == 0) {
            return new long[0];
        }

        long[] array = new long[strArray.length];
        for (int i = 0; i < strArray.length; i++) {
            array[i] = Long.parseLong(strArray[i]);
        }

        return array;
    }

    public static long[][] str2longArray2(String str){
        return str2longArray2(str,Splitable.HUO,Splitable.JINGHAO);
    }
    public static long[][] str2longArray2(String str, String separator1,String separator2) {

        String[] strArray = StringUtils.split(str, separator1);
        if (strArray == null || strArray.length == 0) {
            return new long[0][0];
        }
        long [][] longArray=new long[strArray.length][];
        for(int i=0;i<longArray.length;i++){
            longArray[i]=str2longArray(strArray[i],separator2);
        }
        return longArray;
    }

    public static void main(String args[]){
        System.out.println(JSON.toJSONString(str2intArray("1|2|3|2|4|3")));
    }
}
