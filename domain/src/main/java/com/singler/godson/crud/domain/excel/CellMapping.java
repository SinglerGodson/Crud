package com.singler.godson.crud.domain.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 单元格映射
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/11/2 13:01
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CellMapping {
    String title();
    String field() default "";
}
