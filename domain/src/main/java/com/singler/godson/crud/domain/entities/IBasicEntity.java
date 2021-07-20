package com.singler.godson.crud.domain.entities;

import com.singler.godson.crud.domain.IBasicDomain;

import java.util.Date;

/**
 * 基础实体类
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/4/5 23:43
 */
public interface IBasicEntity<ID> extends IBasicDomain<ID> {

    Integer getVersion();
    void setVersion(Integer version);

    Date getCreated();
    void setCreated(Date created);

    ID getCreator();
    void setCreator(ID creator);

    Date getModified();
    void setModified(Date modified);

    ID getModifier();
    void setModifier(ID modifier);

}
