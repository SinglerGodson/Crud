package com.singler.godson.crud.service.impl;

import com.github.pagehelper.Page;
import com.singler.godson.crud.common.exceptions.CrudException;
import com.singler.godson.crud.common.exceptions.CrudExceptionEnum;
import com.singler.godson.crud.common.utils.CollectionUtils;
import com.singler.godson.crud.dao.CrudDao;
import com.singler.godson.crud.domain.entities.IBasicEntity;
import com.singler.godson.crud.service.CrudService;
import com.singler.godson.hibatis.orderby.OrderBy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;

/**
 * 基础增删改查服务实现类
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/5/24 16:41
 */
public abstract class AbstractCrudService<ID,
                                          ENTITY extends IBasicEntity<ID>,
                                          SAVE_DTO   extends ENTITY,
                                          QUERY_DTO  extends ENTITY,
                                          RESULT_DTO extends ENTITY,
                                          DAO extends CrudDao<ID, ENTITY>>
        implements CrudService<ID, ENTITY, SAVE_DTO, QUERY_DTO, RESULT_DTO> {

    @Autowired
    private DAO dao;

    protected final DAO getDao() {
        return dao;
    }

    @Override
    public int insert(SAVE_DTO entity) {
        validate(entity);
        return getDao().insert(entity);
    }

    @Override
    public int insert(Collection<SAVE_DTO> entities) {
        entities.forEach(this::validate);
        return getDao().insert((Collection) entities);
    }

    @Override
    public int delete(QUERY_DTO entity) {
        return getDao().delete(entity);
    }

    @Override
    public int deleteById(ID id) {
        return getDao().deleteById(id);
    }

    @Override
    public int upsert(SAVE_DTO entity) {
        return entity.getId() == null ? insert(entity) : updateById(entity, entity.getId());
    }

    @Override
    public int updateById(SAVE_DTO entity, ID id) {
        return getDao().updateById(entity, id);
    }

    @Override
    public int count(QUERY_DTO entity) {
        return getDao().count(entity);
    }

    @Override
    public boolean exist(QUERY_DTO entity) {
        return getDao().exist(entity);
    }

    @Override
    public RESULT_DTO queryById(ID id) {
        return getDao().selectById(id);
    }

    @Override
    public RESULT_DTO query(QUERY_DTO entity) {
        return getDao().select(entity);
    }

    @Override
    public List<RESULT_DTO> query(QUERY_DTO entity, OrderBy orderBy) {
        return getDao().select(orderBy, entity);
    }

    @Override
    public Page<RESULT_DTO> page(int pageNum, int pageSize, QUERY_DTO entity, OrderBy orderBy) {
        return getDao().select(orderBy, pageNum, pageNum, entity);
    }

    @Override
    public void validate(Collection<SAVE_DTO> entities) {
        if (!CollectionUtils.isEmpty(entities)) {
            entities.forEach(entity -> {
                if (entity == null) {
                    throw new CrudException(CrudExceptionEnum.EMPTY_ENTITY);
                }
            });
        }
        throw new CrudException(CrudExceptionEnum.EMPTY_ENTITY);
    }
}
