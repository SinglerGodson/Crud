package com.singler.godson.crud.domain.dtoes.attachment;

import com.singler.godson.crud.domain.entities.attachment.Attachment;
import lombok.Data;

/**
 * 附件信息查询结果
 */
@Data
public class AttachmentResultVo extends Attachment {

    /**
     * 前缀
     */
    private String prefix;
    /**
     * 后缀
     */
    private String suffix;
}