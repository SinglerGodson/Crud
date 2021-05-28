package com.singler.godson.crud.domain.dtoes.attachment;

import com.singler.godson.crud.domain.entities.attachment.Attachment;
import lombok.Data;

import java.util.Set;

/**
 * 附件信息查询条件
 */
@Data
public class AttachmentQueryRequestVo extends Attachment {

//    @In("bizId")
    private Set<Long> bizIdInSet;

//    @In("module")
    private Set<String> moduleInSet;

//    @In("type")
    private Set<Long> typeInSet;

//    @In("id")
    private Set<Long> idInSet;

//    @NotIn("id")
    private Set<Long> idNotInSet;

}