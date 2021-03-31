package com.singler.godson.crud.service;

import com.github.pagehelper.Page;
import com.singler.godson.crud.domain.entities.BasicEntity;
import com.singler.godson.crud.domain.entities.IBasicEntity;
import com.singler.godson.hibatis.orderby.OrderBy;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 基础增删改查服务类
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/4/5 23:40
 */
public interface CrudService<ID, ENTITY extends IBasicEntity<ID>,
        SAVE_REQUEST  extends ENTITY,
        QUERY_REQUEST extends ENTITY,
        QUERY_RESULT  extends ENTITY> {

    /**
     * 添加实体信息
     *
     * @param entity 实体
     */
    int insert(SAVE_REQUEST entity);

    /**
     * 批量添加实体信息
     *
     * @param entities 实体集合
     * @return
     */
    int insert(Collection<SAVE_REQUEST> entities);

    int delete(QUERY_REQUEST entity);

    int deleteById(ID id);

    int upsert(SAVE_REQUEST entity);

    /**
     * 更新实体信息
     *
     * @param entity
     */
    int updateById(SAVE_REQUEST entity, ID id);

    /**
     * 方法描述: 统计数据
     * 创建者: maenfang1@jd.com<br/>
     * 创建时间: 2021/1/8 9:26<br/>
     * 更新者: maenfang1@jd.com<br/>
     * 更新时间: 2021/1/8 9:26<br/>
     * 备注:
     */
    int count(QUERY_REQUEST params);

    boolean exist(QUERY_REQUEST params);
    /**
     * 通过id查询
     * @param id
     * @return
     */
    QUERY_RESULT queryById(ID id);

    /**
     * 查询对象信息
     * @param param
     * @return
     */
    QUERY_RESULT query(QUERY_REQUEST param);

    List<QUERY_RESULT> query(QUERY_REQUEST params, OrderBy orderBy);

    Page<QUERY_RESULT> page(int pageNum, int pageSize, QUERY_REQUEST params, OrderBy orderBy);

    default void validate(SAVE_REQUEST entity) {
        validate(Arrays.asList(entity));
    }

    void validate(Collection<SAVE_REQUEST> entities);
}
