package com.coolxtc.common.util;


/**
 * Created by lihui on 2015/8/27.
 * 工具类：字符串处理工具
 */
public class StrUtil {

    /**
     * null 转 ""
     * @param str
     * @return
     */
    public static String null2Str(String str){
        if(str == null){
            return "";
        }else{
            return str;
        }
    }

    /**
     * String 转 int
     * @param str
     * @return
     */
    public static int str2Int(String str){
        try{
            return Integer.parseInt(str);
        }catch (Exception e){
            return 0;
        }

    }

    /**
     * String 转 int
     * @param str
     * @return
     */
    public static long str2long(String str){
        try{
            return Long.parseLong(str);
        }catch (Exception e){
            return 0;
        }

    }

    /**
     * String 转 float
     * @param str
     * @return
     */
    public static float str2float(String str){
        try{
            return Float.parseFloat(str);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }
    /**
     * String 转 double
     * @param str
     * @return
     */
    public static double str2double(String str){
        try{
            return Double.valueOf(str);
        }catch (Exception e){
            return 0;
        }
    }

    /**
     * is null or its length is 0 or it is made by space
     *
     * <pre>
     * isBlank(null) = true;
     * isBlank(&quot;&quot;) = true;
     * isBlank(&quot;  &quot;) = true;
     * isBlank(&quot;a&quot;) = false;
     * isBlank(&quot;a &quot;) = false;
     * isBlank(&quot; a&quot;) = false;
     * isBlank(&quot;a b&quot;) = false;
     * </pre>
     *
     * @param str
     * @return if string is null or its size is 0 or it is made by space, return true, else return false.
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    /**
     * is null or its length is 0
     *
     * <pre>
     * isEmpty(null) = true;
     * isEmpty(&quot;&quot;) = true;
     * isEmpty(&quot;  &quot;) = false;
     * </pre>
     *
     * @param str
     * @return if string is null or its size is 0, return true, else return false.
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0) && isBlank(str);
    }
}
