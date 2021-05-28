package com.singler.godson.crud.domain.dtoes.attachment;

import com.singler.godson.crud.domain.entities.attachment.Attachment;
import lombok.Data;

/**
 * 附件信息保存对象
 */
@Data
public class AttachmentSaveRequestVo extends Attachment {
    private String fileTypeCode;
}