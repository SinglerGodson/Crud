package com.singler.godson.crud.domain.entities;


/**
 * 树形结构
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/11/10 14:15
 */
public interface ITreeableEntity<ID> extends IBasicEntity<ID> {
    ID getParentId();
    void setParentId(ID id);

    Boolean hasLeaf();
    void setHasLeaf(Boolean hasLeaf);

    Integer getLevel();
    void setLevel(Integer level);

}
