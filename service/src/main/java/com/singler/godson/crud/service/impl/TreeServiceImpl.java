package com.singler.godson.crud.service.impl;

import com.singler.godson.crud.domain.entities.ITreeEntity;
import com.singler.godson.crud.service.AbstractCrudService;
import com.singler.godson.crud.service.TreeService;

import java.util.List;

/**
 * 树形结构服务类
 * @author maenfang1
 * @date 2021/5/11 11:13
 */
public class TreeServiceImpl<ID,
        TREE extends ITreeEntity<ID>,
        SAVE_DTO   extends TREE,
        QUERY_DTO  extends TREE,
        RESULT_DTO extends TREE,
        TREE_DAO   extends com.singler.godson.crud.dao.CrudDao<ID, TREE>>
        extends AbstractCrudService<ID, TREE, SAVE_DTO, QUERY_DTO, RESULT_DTO, TREE_DAO>
        implements TreeService<ID, TREE, SAVE_DTO, QUERY_DTO, RESULT_DTO> {
    /**
     * 查询 id的父节点，追溯到topParentId
     *
     * @param id
     * @param topParentId
     * @return
     */
    @Override
    public List<TREE> queryParents(ID id, ID topParentId) {
        return null;
    }

    /**
     * 查询 id的子节点，到level层
     *
     * @param id
     * @param level
     * @return
     */
    @Override
    public List<TREE> queryChildren(ID id, Integer level) {
        return null;
    }
}
