package com.singler.godson.crud.domain.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel导入
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/11/2 12:59
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelImport {
    CellMapping[] cellMappings();
}
