package com.singler.godson.crud.service;

import com.singler.godson.crud.common.exceptions.CrudException;
import com.singler.godson.crud.common.utils.BeanUtils;
import com.singler.godson.crud.common.utils.ClassUtils;
import com.singler.godson.crud.common.utils.StringUtils;
import com.singler.godson.crud.domain.excel.CellMapping;
import com.singler.godson.crud.domain.excel.ExcelImport;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toMap;

/**
 * excel 模板导入统一处理
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/10/29 18:56
 */
public abstract class AbstractImportExcelService<ENTITY> {

    private Class<ENTITY> genericType;
    private final Map<String , String> titleFieldMap = new ConcurrentHashMap<>();
    private final Map<Integer, String> fieldIndexMap = new ConcurrentHashMap<>();

    private static final String  ROW_ERROR_MSG = "第%d行%s";
    private static final String CELL_ERROR_MSG = "%s列数据异常";

    public static final String IMPORT_EXCEL_SERVICE_NAME_SUFFIX = "ImportExcelServiceImpl";

    public List<ENTITY> importExcel(InputStream is, String fileName) {
        Workbook workbook = getWorkbook(is, fileName);
        Sheet sheet = workbook.getSheetAt(getSheetIndex());
        List<Map<String, Object>> dataMapList = getSheetDataList(sheet);
        List<ENTITY> dataBeanList = getDataList(dataMapList);
        return imported(dataBeanList);
    }

    protected ENTITY mapToBean(Map<String, Object> fieldsMap) throws BeanUtils.MapToBeanException {
        return BeanUtils.mapToBean(fieldsMap, getGenericType());
    }

    protected int getSheetIndex() {
        return 0;
    }

    protected int getTitleRowNum() {
        return getFirstDataRowNum() - 1;
    }

    protected int getFirstDataRowNum() {
        return 1;
    }

    protected List<ENTITY> getDataList(List<Map<String, Object>> data) {
        List<ENTITY> dataList = new ArrayList<>();
        int rowNum = 0;
        try {
            for (; rowNum < data.size(); rowNum++) {
                dataList.add(mapToBean(data.get(rowNum)));
            }
        } catch (BeanUtils.MapToBeanException e) {
            String titleName = getTitleCellName(e.getFieldName());
            String msg = String.format(CELL_ERROR_MSG, titleName);
            throw new RuntimeException(String.format(ROW_ERROR_MSG, getFirstDataRowNum() + rowNum + 1, msg));
        }
        return dataList;
    }

    protected Map<String, String> getTitleFieldMapping() {
        if (titleFieldMap.size() == 0) {
            synchronized (titleFieldMap) {
                titleFieldMap.putAll(Arrays.stream(getGenericType().getFields())
                        .filter(field -> field.isAnnotationPresent(CellMapping.class))
                        .map   (field -> field.getAnnotation(CellMapping.class))
                        .collect(toMap(CellMapping::title, CellMapping::field)));
                // 用定义在类上的注解，覆盖定义在Field上的注解。
                ExcelImport excelImport = getGenericType().getAnnotation(ExcelImport.class);
                if (excelImport != null) {
                    CellMapping[] cellMappings = excelImport.cellMappings();
                    titleFieldMap.putAll(Arrays.stream(cellMappings).collect(toMap(CellMapping::title, CellMapping::field)));
                }
            }
        }
        return titleFieldMap;
    }

    protected Map<Integer, String> getFieldIndexMap(Row titleRow) {
        if (fieldIndexMap.size() == 0) {
            synchronized (fieldIndexMap) {
                Map<String, String> titleFieldMapping = getTitleFieldMapping();
                for (int i = 0; i < titleRow.getLastCellNum(); i++) {
                    Cell cell = titleRow.getCell(i, Row.MissingCellPolicy.RETURN_NULL_AND_BLANK);
                    String title = cell.getStringCellValue();
                    fieldIndexMap.put(i, titleFieldMapping.get(title));
                }
            }
        }
        return fieldIndexMap;
    }

    protected abstract List<ENTITY> imported(List<ENTITY> dataList);

    private String templateUrl;
    @Value("${import.excel.template.url.property.name:import.excel.template.url.%s}")
    private String templateUrlProperty;
    @Autowired
    private Environment environment;
    public String getTemplateUrl() {
        if (templateUrl == null) {
            String className = getClass().getSimpleName();
            int index = className.indexOf(IMPORT_EXCEL_SERVICE_NAME_SUFFIX);
            char[] chars = className.toCharArray();
            chars[0]  = Character.toLowerCase(chars[0]);
            className = String.copyValueOf(chars, 0, index);
            className = String.format(templateUrlProperty, className);
            templateUrl = environment.getProperty(className);
            if (StringUtils.isEmpty(templateUrl)) {
                throw new CrudException(501, "未找到" + className + "配置项");
            }
        }
        return templateUrl;
    }

    private static final String EXCEL_SUFFIX_XLS  = ".xls";
    private static final String EXCEL_SUFFIX_XLSX = ".xlsx";
    private Workbook getWorkbook(InputStream is, String fileName) {
        try {
            if (fileName.toLowerCase().endsWith(EXCEL_SUFFIX_XLS)) {
                return new HSSFWorkbook(is);
            } else if (fileName.toLowerCase().endsWith(EXCEL_SUFFIX_XLSX)) {
                return new XSSFWorkbook(is);
            } else {
                throw new RuntimeException("文档格式不正确!");
            }
        } catch (IOException e) {
            throw new RuntimeException("文档格式不正确!");
        }
    }

    private List<Map<String, Object>> getSheetDataList(Sheet sheet) {
        int firstDataRowNum = getFirstDataRowNum();
        Row titleRow = sheet.getRow(getTitleRowNum());
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<Integer, String> fieldIndexMap = getFieldIndexMap(titleRow);
        for (int rowNum = firstDataRowNum; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);
            Map<String, Object> valueMap = new HashMap<>(row.getLastCellNum());
            for (int i = 0; i < row.getLastCellNum(); i++) {
                Cell cell = row.getCell(i, Row.MissingCellPolicy.RETURN_NULL_AND_BLANK);
                valueMap.put(fieldIndexMap.get(i), getCellValue(cell));
            }
            dataList.add(valueMap);
        }
        return dataList;
    }

    private Class<ENTITY> getGenericType() {
        if (genericType == null) {
            genericType = (Class<ENTITY>) ClassUtils.getGenericClass(getClass());
        }
        return genericType;
    }

    private Object getCellValue(Cell cell) {
        if(cell == null) {
            return null;
        }
        switch (cell.getCellTypeEnum()) {
            case BLANK: {
                return "";
            }
            case STRING: {
                return cell.getStringCellValue();
            }
            case BOOLEAN: {
                return cell.getBooleanCellValue();
            }
            case ERROR: {
                return cell.getErrorCellValue();
            }
            case FORMULA: {
                return cell.getCellFormula();
            }
            case NUMERIC: {
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    cell.setCellType(CellType.STRING);
                    return cell.getStringCellValue();
                }
            }
            case _NONE:
            default: {
                return null;
            }
        }
    }

    private String getTitleCellName(String fieldName) {
        Map<String, String> titleFieldNameMap = getTitleFieldMapping();
        for(Map.Entry<String, String> entry : titleFieldNameMap.entrySet()) {
            if (entry.getValue().equals(fieldName)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
