package com.singler.godson.crud.service.attachment;

import com.singler.godson.crud.domain.entities.attachment.Attachment;

/**
 * 文件上传后续处理逻辑
 *
 * @author maenfang1
 * @date 2021/5/28 17:43
 */
public interface UploadedService {
    void uploaded(Attachment attachment);
}
