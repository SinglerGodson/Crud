package com.singler.godson.crud.web.advisores;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Controller方法返回值封装
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/11/13 11:12
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RespDataController {
}
