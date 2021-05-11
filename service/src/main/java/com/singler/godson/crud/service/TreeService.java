package com.singler.godson.crud.service;

import com.singler.godson.crud.domain.entities.ITreeEntity;

import java.util.List;

/**
 * 树形结构服务
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/11/10 14:19
 */
public interface TreeService<ID, TREE extends ITreeEntity<ID>,
        SAVE_DTO extends TREE,
        QUERY_DTO extends TREE,
        RESULT_DTO extends TREE> extends CrudService<ID, TREE, SAVE_DTO, QUERY_DTO, RESULT_DTO> {

    /**
     * 查询 id的父节点，追溯到topParentId
     * @param id
     * @param topParentId
     * @return
     */
    List<TREE> queryParents(ID id, ID topParentId);

    /**
     * 查询 id的子节点，到level层
     * @param id
     * @param level
     * @return
     */
    List<TREE> queryChildren(ID id, Integer level);
}
