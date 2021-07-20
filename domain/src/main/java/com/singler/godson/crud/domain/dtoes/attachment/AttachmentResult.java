package com.singler.godson.crud.domain.dtoes.attachment;

import com.singler.godson.crud.domain.entities.attachment.Attachment;
import lombok.Data;

/**
 * 附件信息查询结果
 * @author maenfang1
 * @version 1.0
 * @date 2020-04-20 9:37
 */
@Data
public class AttachmentResult extends Attachment {

    /**
     * 前缀
     */
    private String prefix;
    /**
     * 后缀
     */
    private String suffix;
}