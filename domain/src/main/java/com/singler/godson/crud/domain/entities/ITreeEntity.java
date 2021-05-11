package com.singler.godson.crud.domain.entities;


/**
 * 树形结构基础实体类
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/11/10 14:15
 */
public interface ITreeEntity<ID> extends IBasicEntity<ID> {

    String PATH_SEPARATOR = ",";

    ID getParentId();
    void setParentId(ID id);

    String getPath();
    void setPath(String path);

    Integer getLevel();
    void setLevel(Integer level);

}
