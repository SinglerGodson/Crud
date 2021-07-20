package com.singler.godson.crud.service.selectable;


import com.singler.godson.crud.domain.dtoes.selectable.Option;

import java.util.List;

/**
 * 下拉列表数据服务
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/10/29 15:29
 */
public interface SelectableService<T> {
    String SERVICE_NAME_SUFFIX = "SelectableServiceImpl";

    /**
     * 返回符合条件的下拉列表的数据
     * @param param
     * @return
     */
    List<Option> options(T param);
}
