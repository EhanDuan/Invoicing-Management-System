package com.lzj.admin.utils;

public class MathUtil {


    public static float format2Bit(float n){
        return (float) (Math.round(n * 100)) / 100;
    }
}
