package com.singler.godson.crud.domain.attachment;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 附件下载信息
 *
 * @author maenfang1
 * @date 2021/5/28 14:43
 */
@Data
@AllArgsConstructor
public class DownloadDto {
    private byte[] bytes;
    private String fileName;
    private String extension;
}
