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
public class TreeEntity<ID>
        extends BasicEntity<ID>
        implements ITreeEntity<ID> {
    private ID parentId;
    private String path;
    private Integer level;

    @Override
    public void setPath(String path) {
        if (path != null) {
            if (!path.startsWith(PATH_SEPARATOR)) {
                path = PATH_SEPARATOR + path;
            }
            if (!path.endsWith(PATH_SEPARATOR)) {
                path = path + PATH_SEPARATOR;
            }
        }
        this.path = path;
    }
}
