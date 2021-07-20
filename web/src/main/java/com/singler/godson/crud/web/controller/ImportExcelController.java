package com.singler.godson.crud.web.controller;

import com.singler.godson.crud.common.exceptions.CrudException;
import com.singler.godson.crud.service.poi.AbstractImportExcelService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 统一上传文件处理
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/10/29 18:48
 */
@RestController
@RequestMapping(value = "/excel")
public class ImportExcelController {

    @Autowired
    private ApplicationContext applicationContext;

    @PostMapping("/upload/{serviceName}")
    public <T> List<T> uploadExcel(@RequestParam("excel") MultipartFile file,
                                   @PathVariable("serviceName") String serviceName,
                                   @RequestParam(value = "returnData", defaultValue = "false") boolean returnData) throws IOException {
        serviceName = applyServiceName(serviceName);
        AbstractImportExcelService<T> importService = getServiceImpl(serviceName);
        List<T> dataList = importService.importExcel(file.getInputStream(), file.getOriginalFilename());
        return returnData ? dataList : null;
    }

    @GetMapping("/template/url/{serviceName}")
    public String templateUrl(@PathVariable("serviceName") String serviceName) {
        return getServiceImpl(serviceName).getTemplateUrl();
    }

    private <T> AbstractImportExcelService<T> getServiceImpl(String serviceName) {
        try {
            serviceName = applyServiceName(serviceName);
            return applicationContext.getBean(serviceName, AbstractImportExcelService.class);
        } catch (BeansException e) {
            throw new CrudException("500", "未实现AbstractExcelImportService抽象类");
        }
    }

    private String applyServiceName(String serviceName) {
        return serviceName + AbstractImportExcelService.IMPORT_EXCEL_SERVICE_NAME_SUFFIX;
    }
}
