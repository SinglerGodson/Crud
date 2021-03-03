package com.singler.godson.crud.domain.entities;

import lombok.Data;

import java.util.Date;

/**
 * 基础数据库实体类
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/4/5 23:39
 */
@Data
public class BasicEntity<ID> implements IBasicEntity<ID> {
    private ID id;
    private ID creator;
    private ID modifier;
    private Date created;
    private Date modified;
    private Boolean deleted;
    private Integer version;

    @Override
    public Boolean isDeleted() {
        return deleted;
    }
}
