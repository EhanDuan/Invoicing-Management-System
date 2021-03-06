package com.lzj.admin.utils;

public class StringUtil {
    public static boolean isEmpty(String str){
        if(str == null || "".equals(str.trim())){
            return true;
        }else{
            return false;
        }
    }

    public static String formatCode(String code){
        try{
            int length = code.length();
            Integer num = Integer.valueOf(code.substring(length - 4, length)) + 1;
            String codeNum = num.toString();
            int codeLength = codeNum.length();
            for(int i = 4; i > codeLength; i--){
                codeNum = codeNum + "0";
            }
            return codeNum;
        }catch (NumberFormatException e){
            return "0100";
        }
    }
}
