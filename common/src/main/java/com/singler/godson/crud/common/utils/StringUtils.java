package com.singler.godson.crud.common.utils;

/**
 * 字符串相关工具类
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/4/5 23:32
 */
public class StringUtils extends org.springframework.util.StringUtils {

    public static String toLowerCaseFirst(String str) {
        return toLowerCaseAt(str, 0);
    }

    public static String toLowerCaseAt(String str, int index) {
        return toLowerCase(str, index,  index + 1);
    }

    public static String toLowerCase(String str, int from, int to) {
        return toggleCase(str, from, to, false);
    }

    public static String toUpperCaseFirst(String str) {
        return toUpperCaseAt(str, 0);
    }

    public static String toUpperCaseAt(String str, int index) {
        return toUpperCase(str, index,  index + 1);
    }

    public static String toUpperCase(String str, int from, int to) {
        return toggleCase(str, from, to, true);
    }

    private static String toggleCase(String str, int from, int to, boolean upperCase) {
        if (isEmpty(str)) {
            return str;
        }

        if (from >= to || to >= str.length()) {
            throw new IllegalArgumentException("from should less than to, and to should less than str.length()");
        }

        char[] cs = str.toCharArray();
        for (int i = from; i < to; i++) {
            if (upperCase && Character.isLowerCase(cs[i])) {
                cs[i] = Character.toUpperCase(cs[i]);
            } else if (!upperCase && Character.isUpperCase(cs[i])) {
                cs[i] = Character.toLowerCase(cs[i]);
            }
        }
        return String.valueOf(cs);
    }


}
