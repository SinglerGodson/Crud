package com.singler.godson.crud.domain.dtoes.attachment;

import lombok.Data;

import java.io.Serializable;

/**
 * 附件请求dto
 * @author lipengpeng
 * @version 1.0
 * @date 2020-04-20 9:37
 */
@Data
public class AttachmentCountResultVo implements Serializable {

    private String bizId;

    private Integer count;
}
