package com.singler.godson.crud.dao;

import com.singler.godson.crud.domain.entities.IBasicEntity;
import com.singler.godson.hibatis.dao.HibatisDao;

/**
 *  基础增删改查Dao
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/5/24 14:14
 */
public interface CrudDao<ID, ENTITY extends IBasicEntity<ID>> extends HibatisDao<ENTITY, ID> {
    String ID = "id";
    String VERSION  = "version";
    String DELETED  = "deleted";
    String CREATED  = "created";
    String CREATOR  = "creator";
    String MODIFIED = "modified";
    String MODIFIER = "modifier";

}
