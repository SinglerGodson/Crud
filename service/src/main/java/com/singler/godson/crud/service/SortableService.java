package com.singler.godson.crud.service;


import com.singler.godson.hibatis.entities.SortableEntity;

/**
 * 排序服务
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/11/10 14:34
 */
public interface SortableService<ID, ENTITY extends SortableEntity> {

    int moveUp(ID id);

    int moveDown(ID id);

    int moveTo(ID id, int to);

    int resetSort(ENTITY param);
}
