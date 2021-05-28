package com.singler.godson.crud.domain.entities.attachment;

import com.singler.godson.crud.domain.entities.BasicEntity;
import lombok.Data;

/**
 * 附件信息
 */
@Data
public class Attachment extends BasicEntity<Long> {
    private static final long serialVersionUID = 1L;

    /**
     * 业务id
     */
    private Long bizId;
    /**
     * 业务模块
     */
    private String module;
    /**
     * 业务类型
     */
    private Long type;
    /**
     * 文件名称
     */
    private String name;
    /**
     * 扩展类型
     */
    private String ext;
    /**
     * 访问地址
     */
    private String url;
    /**
     * 文件大小
     */
    private Long size;
    /**
     * 文件md5校验码
     */
    private String md5;
    /**
     * 业务类型名称
     */
    private String typeName;
    /**
     * 附件备注
     */
    private String remark;

    /** 附件的二进制流**/
    private byte[] bytes;

}