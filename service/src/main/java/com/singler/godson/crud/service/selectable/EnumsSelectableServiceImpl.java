package com.singler.godson.crud.service.selectable;


import com.singler.godson.crud.common.enums.BooleanEnum;
import com.singler.godson.crud.common.utils.StringUtils;
import com.singler.godson.crud.domain.dtoes.selectable.Option;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * 带有缓存功能的可选择数据，处理简单不变的数据
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/11/14 17:32
 */
@Service
public class EnumsSelectableServiceImpl implements SelectableService<String> {

    private static final Map<String, List<Option>> OPTIONS = new ConcurrentHashMap<>();

    static {
        putOptions(BooleanEnum.class, BooleanEnum::strValue, BooleanEnum::desc);
    }

    @Override
    public List<Option> options(String enumName) {
        return OPTIONS.get(enumName);
    }

    private static <ENUM extends Enum<?>> void putOptions(Class<ENUM> enumClass,
                                                          Function<? super ENUM, Serializable> valueMapper,
                                                          Function<? super ENUM, String      > labelMapper) {
        String enumName = StringUtils.toLowerCaseFirst(enumClass.getSimpleName()).replace("Enum", "");
        OPTIONS.put(enumName, Arrays.stream(enumClass.getEnumConstants()).map(e -> new Option(valueMapper.apply(e), labelMapper.apply(e))).collect(toList()));
    }
}
