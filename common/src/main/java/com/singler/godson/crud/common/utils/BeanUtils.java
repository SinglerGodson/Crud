package com.singler.godson.crud.common.utils;

import org.springframework.cglib.beans.BeanMap;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.Map;

/**
 * Bean 工具类
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/11/3 11:32
 */
public class BeanUtils extends org.springframework.beans.BeanUtils {

    private static final String PARAM_ERROR_MSG = "参数%s数据转换异常";

    public static Map<String, Object> beanToMap(Object bean) {
        return BeanMap.create(bean);
    }

    public static <T> T mapToBean(Map paramsMap, Class<T> clazz) throws MapToBeanException {
        T bean = instantiateClass(clazz);
        if (isSimpleValueType(clazz)) {
            return bean;
        }
        String paramName  = null;
        Object fieldValue = null;
        BeanMap beanMap = BeanMap.create(bean);
        ConversionService conversionService = DefaultConversionService.getSharedInstance();
        try {
            for (Object object : paramsMap.entrySet()) {
                if (object instanceof Map.Entry) {
                    Map.Entry<String, Object> entry = (Map.Entry<String, Object>) object;
                    paramName  = entry.getKey();
                    fieldValue = entry.getValue();
                    Class<?> propertyType = beanMap.getPropertyType(paramName);
                    if (conversionService.canConvert(fieldValue.getClass(), propertyType)) {
                        Object value = conversionService.convert(fieldValue, propertyType);
                        beanMap.put(paramName, value);
                    }
                }
            }
        } catch (Exception e) {
            String msg = String.format(PARAM_ERROR_MSG, paramName);
            throw new MapToBeanException(msg, paramName, fieldValue, e);
        }
        return bean;
    }


    public static class MapToBeanException extends Exception {
        private Object value;
        private String fieldName;

        public MapToBeanException(String message, String fieldName, Object value, Throwable cause) {
            super(message, cause);
            this.value = value;
            this.fieldName = fieldName;
        }

        public Object getValue() {
            return value;
        }

        public String getFieldName() {
            return fieldName;
        }

    }

}
