package com.singler.godson.crud.service.selectable;

import com.singler.godson.crud.domain.dtoes.selectable.Option;

import java.util.ArrayList;
import java.util.List;

/**
 * 带有缓存功能的可选择数据，处理简单不变的数据
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/11/14 17:32
 */
public abstract class AbstractCacheableSelectableService implements SelectableService<Void> {

    private final List<Option> options = new ArrayList<>();

    @Override
    public final List<Option> options(Void param) {
        if (options.isEmpty()) {
            synchronized (options) {
                options.addAll(options());
            }
        }
        return options;
    }

    /**
     * 返回需要缓存的下拉列表的数据
     * @return
     */
    protected abstract List<Option> options();
}
