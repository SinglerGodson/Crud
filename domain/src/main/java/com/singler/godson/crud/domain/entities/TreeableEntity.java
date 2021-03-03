package com.singler.godson.crud.domain.entities;

import lombok.Data;

/**
 * 树形结构对象
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/11/13 16:47
 */
@Data
public class TreeableEntity<ID>
        extends BasicEntity<ID>
        implements ITreeableEntity<ID> {

    private ID parentId;
    private Integer level;
    private Boolean hasLeaf;

    @Override
    public Boolean hasLeaf() {
        return hasLeaf;
    }
}
