package com.singler.godson.crud.enumtype;

import com.singler.godson.crud.common.utils.EnumUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应类型枚举类
 *
 * @author maenfang1
 * @date 2021/5/21 15:46
 */
@Getter
@AllArgsConstructor
public enum ContentTypeEnum {

    DOC ("doc" , "application/msword"),
    DOCX("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    PDF ("pdf" , "application/pdf"),
    XLS ("xls" , "application/vnd.ms-excel"),
    XLSX("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    PPT ("ppt" , "application/vnd.ms-powerpoint"),
    PPTX("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"),
    BMP ("bmp" , "image/bmp"),
    GIF ("gif" , "image/gif"),
    JPEG("jpeg", "image/jpeg"),
    JPG ("jpg" , "image/jpeg"),
    PNG ("png" , "image/png"),
    TIFF("tiff", "image/tiff"),
    TIF ("tif" , "image/tif"),
    XML ("xml" , "text/xml"),
    HTML("html", "text/html"),
    TXT ("txt" , "text/plain")
    ;

    private final String extension;
    private final String contentType;

    public static String getContentType(String extension) {
        ContentTypeEnum contentTypeEnum = getByExtension(extension);
        if (contentTypeEnum != null) {
            return contentTypeEnum.getExtension();
        }
        return null;
    }

    public static ContentTypeEnum getByExtension(String extension) {
        return EnumUtils.getEnum(ContentTypeEnum.class, e -> e.getExtension().equals(extension));
    }
}
