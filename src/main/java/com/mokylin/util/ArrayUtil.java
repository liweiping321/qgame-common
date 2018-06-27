package com.mokylin.util;

import com.alibaba.fastjson.JSON;
import com.mokylin.consts.Splitable;

import org.apache.commons.lang.StringUtils;


/**
 * Created by liweiping on 2018/6/22.
 */
public class ArrayUtil {
    private static final int[] emptyIntArr=new int[0];

    private static final int[][] entyIntArr2=new int[0][0];

    private static final long [] emptyLongArr=new long[0];

    private static final long [][] empltyLongArr2=new long[0][0];


    public  static int [] str2intArray(String str){
        return str2intArray(str, Splitable.HUO);
    }
    public static int[] str2intArray(String str, String separator) {
        String[] strArray = StringUtils.split(str, separator);
        if (strArray == null || strArray.length == 0) {
            return  emptyIntArr;
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
            return entyIntArr2;
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
            return emptyLongArr;
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
            return empltyLongArr2;
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
