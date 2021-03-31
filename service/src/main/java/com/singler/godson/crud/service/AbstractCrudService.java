package com.singler.godson.crud.service;

import com.github.pagehelper.Page;
import com.singler.godson.crud.common.exceptions.CrudException;
import com.singler.godson.crud.common.utils.CollectionUtils;
import com.singler.godson.crud.dao.CrudDao;
import com.singler.godson.crud.domain.entities.IBasicEntity;
import com.singler.godson.hibatis.orderby.OrderBy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 基础增删改查服务实现类
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/5/24 16:41
 */
public abstract class AbstractCrudService<ID,
                                          ENTITY extends IBasicEntity<ID>,
                                          SAVE_REQUEST  extends ENTITY,
                                          QUERY_REQUEST extends ENTITY,
                                          QUERY_RESULT  extends ENTITY,
                                          DAO extends CrudDao<ID, ENTITY>>
        implements CrudService<ID, ENTITY, SAVE_REQUEST, QUERY_REQUEST, QUERY_RESULT> {

    @Autowired
    private DAO dao;

    protected final DAO getDao() {
        return dao;
    }

    @Override
    public int insert(SAVE_REQUEST entity) {
        validate(entity);
        return getDao().insert(entity);
    }

    @Override
    public int insert(Collection<SAVE_REQUEST> entities) {
        entities.forEach(this::validate);
        return getDao().insert((Collection) entities);
    }

    @Override
    public int delete(QUERY_REQUEST entity) {
        return getDao().delete(entity);
    }

    @Override
    public int deleteById(ID id) {
        return getDao().deleteById(id);
    }

    @Override
    public int upsert(SAVE_REQUEST entity) {
        return 0;
    }

    @Override
    public int updateById(SAVE_REQUEST entity, ID id) {
        return getDao().updateById(entity, id);
    }

    @Override
    public int count(QUERY_REQUEST entity) {
        return getDao().count(entity);
    }

    @Override
    public boolean exist(QUERY_REQUEST entity) {
        return getDao().exist(entity);
    }

    @Override
    public QUERY_RESULT queryById(ID id) {
        return getDao().selectById(id);
    }

    @Override
    public QUERY_RESULT query(QUERY_REQUEST entity) {
        return getDao().select(entity);
    }

    @Override
    public List<QUERY_RESULT> query(QUERY_REQUEST entity, OrderBy orderBy) {
        return getDao().select(orderBy, entity);
    }

    @Override
    public Page<QUERY_RESULT> page(int pageNum, int pageSize, QUERY_REQUEST entity, OrderBy orderBy) {
        return getDao().select(orderBy, pageNum, pageNum, entity);
    }

    @Override
    public void validate(Collection<SAVE_REQUEST> entities) {
        if (!CollectionUtils.isEmpty(entities)) {
            entities.forEach(entity -> {
                if (entity == null) {
                    throw new CrudException(500, "参数不可为空");
                }
            });
        }
        throw new CrudException(500, "参数不可为空");
    }
}
