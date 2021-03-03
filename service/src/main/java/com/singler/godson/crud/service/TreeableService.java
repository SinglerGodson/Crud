package com.singler.godson.crud.service;

import com.singler.godson.crud.domain.entities.ITreeableEntity;

import java.util.List;

/**
 * 树形结构服务
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/11/10 14:19
 */
public interface TreeableService<ID, T extends ITreeableEntity<ID>> {

    List<T> queryParents(ID id, ID parentId);

    List<T> queryChildren(ID parentId, int level);
}
