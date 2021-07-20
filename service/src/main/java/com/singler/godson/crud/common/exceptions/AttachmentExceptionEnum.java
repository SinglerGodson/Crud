package com.singler.godson.crud.common.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 附件上传相关异常信息
 *
 * @author maenfang1
 * @date 2021/7/20 22:11
 */
@Getter
@AllArgsConstructor
public enum AttachmentExceptionEnum implements ExceptionInfo {

    EMPTY_URL("1001", "url不能为空"),
    EMPTY_BID("1002", "bizId不能为空"),
    EMPTY_ID_BIZ_ID("1003", "bizId与id不能全为空"),
    ;

    private final String code;
    private final String desc;
}
