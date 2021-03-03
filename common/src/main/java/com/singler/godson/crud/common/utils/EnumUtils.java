package com.singler.godson.crud.common.utils;


import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

/**
 * 枚举工具类
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/4/5 23:46
 */
public class EnumUtils {

    public static <ENUM extends Enum, T> T getField(Class<ENUM> enumType, Predicate<ENUM> predicate, Function<? super ENUM, ? extends T> mapper) {
        List<T> fieldList = getFieldList(enumType, predicate, mapper);
        if (fieldList.size() == 0) {
            return null;
        } else if (fieldList.size() == 1) {
            return fieldList.get(0);
        }
        throw new RuntimeException("multi value return.");
    }

    public static <ENUM extends Enum, T> List<T> getFieldList(Class<ENUM> enumType, Predicate<ENUM> predicate, Function<? super ENUM, ? extends T> mapper) {
        return Arrays.stream(enumType.getEnumConstants()).filter(predicate).map(mapper).distinct().collect(toList());
    }

    public static <ENUM extends Enum> ENUM getEnum(Class<ENUM> enumType, Predicate<ENUM> predicate) {
        List<ENUM> enumList = getEnumList(enumType, predicate);
        if (enumList.size() == 0) {
            return null;
        } else if (enumList.size() == 1) {
            return enumList.get(0);
        }
        throw new RuntimeException("multi value return.");
    }

    public static <ENUM extends Enum> List<ENUM> getEnumList(Class<ENUM> enumType, Predicate<ENUM> predicate) {
        return Arrays.stream(enumType.getEnumConstants()).filter(predicate).collect(toList());
    }

    public static <CODE, DESC, ENUM extends Enum & IEnum<CODE, DESC>> DESC getDesc(Class<ENUM> enumType, CODE code) {
        List<DESC> descListList = Arrays.stream(enumType.getEnumConstants()).filter(e -> e.getCode().equals(code)).map(IEnum::getDesc).collect(toList());
        if (descListList.size() == 0) {
            return null;
        } else if (descListList.size() == 1) {
            return descListList.get(0);
        }
        throw new RuntimeException("multi value return.");
    }

    public static <CODE, ENUM extends Enum & IEnum<CODE, ?>> ENUM getEnum(Class<ENUM> enumType, CODE code) {
        List<ENUM> enumList = getEnumList(enumType, code);
        if (enumList.size() == 0) {
            return null;
        } else if (enumList.size() == 1) {
            return enumList.get(0);
        }
        throw new RuntimeException("multi value return.");
    }

    public static <ENUM extends Enum & IEnum, CODE> List<ENUM> getEnumList(Class<ENUM> enumType, CODE code) {
        return Arrays.stream(enumType.getEnumConstants()).filter(e -> e.getCode().equals(code)).collect(toList());
    }


    public interface IEnum<CODE, DESC> {
        CODE getCode();
        DESC getDesc();
    }

}
