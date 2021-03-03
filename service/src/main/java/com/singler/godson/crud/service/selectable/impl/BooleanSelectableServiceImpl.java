package com.singler.godson.crud.service.selectable.impl;

import com.singler.godson.crud.common.enumtype.BooleanEnum;
import com.singler.godson.crud.domain.form.Option;
import com.singler.godson.crud.service.selectable.AbstractCacheableSelectableService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 布尔值类型选择项数据
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/11/10 14:03
 */
@Service
public class BooleanSelectableServiceImpl extends AbstractCacheableSelectableService {

    @Override
    public List<Option> options() {
        return Arrays.stream(BooleanEnum.values()).map(bool -> new Option(bool.strValue(), bool.desc())).collect(Collectors.toList());
    }
}
