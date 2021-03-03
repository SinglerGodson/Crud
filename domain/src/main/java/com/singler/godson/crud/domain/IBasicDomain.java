package com.singler.godson.crud.domain;


/**
 * 基础领域对象
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/5/24 16:58
 */
public interface IBasicDomain<ID> {
    ID getId();
    void setId(ID id);
}
