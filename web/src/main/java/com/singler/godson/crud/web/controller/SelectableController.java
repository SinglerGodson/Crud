package com.singler.godson.crud.web.controller;

import com.singler.godson.crud.common.exceptions.CrudException;
import com.singler.godson.crud.common.utils.BeanUtils;
import com.singler.godson.crud.common.utils.ClassUtils;
import com.singler.godson.crud.domain.dtoes.selectable.Option;
import com.singler.godson.crud.service.selectable.AbstractCacheableSelectableService;
import com.singler.godson.crud.service.selectable.EnumsSelectableServiceImpl;
import com.singler.godson.crud.service.selectable.SelectableService;
import com.singler.godson.crud.common.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 下拉选择框统一请求Controller
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/10/29 15:20
 */
@RestController
@RequestMapping(value = "/form/selectable")
public class SelectableController {

    @Autowired
    private ApplicationContext applicationContext;

    private final Map<Class<?>, Class<?>> genericClassMap = new ConcurrentHashMap<>();

    @GetMapping("/{serviceName}")
    public List<Option> select(HttpServletRequest request,
                               @PathVariable("serviceName") String serviceName) throws BeanUtils.MapToBeanException {
        serviceName = generateServiceName(serviceName);
        SelectableService selectableService = applicationContext.getBean(serviceName, SelectableService.class);
        if (selectableService == null) {
            throw new CrudException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "未实现SelectableService接口");
        }
        return selectableService.options(getParamObject(request, selectableService.getClass()));
    }

    private Object getParamObject(HttpServletRequest request, Class<?> serviceClass) throws BeanUtils.MapToBeanException {
        Class<?> genericClass = genericClassMap.get(serviceClass);
        if (genericClass == null) {
            genericClass = ClassUtils.getGenericClass(serviceClass, SelectableService.class);
            if (genericClass == null) {
                genericClass = AbstractCacheableSelectableService.class;
            }
            genericClassMap.put(serviceClass, genericClass);
        }
        if (!genericClass.equals(AbstractCacheableSelectableService.class)) {
            return BeanUtils.mapToBean(RequestUtils.paramsMap(request), genericClass);
        }
        return null;
    }

    @GetMapping("/enums/{enumName}")
    public List<Option> select(@PathVariable("enumName") String enumName) {
        return applicationContext.getBean(EnumsSelectableServiceImpl.class).options(enumName);
    }

    private String generateServiceName(String serviceName) {
        return serviceName + SelectableService.SERVICE_NAME_SUFFIX;
    }
}
