package com.singler.godson.crud.domain.dtoes.attachment;

import com.singler.godson.crud.domain.entities.attachment.Attachment;
import com.singler.godson.hibatis.annotation.operator.In;
import com.singler.godson.hibatis.annotation.operator.NotIn;
import lombok.Data;

import java.util.Set;

/**
 * 附件信息查询条件
 * @author maenfang1
 * @version 1.0
 * @date 2020-04-20 9:37
 */
@Data
public class AttachmentQueryRequest extends Attachment {

    @In("bizId")
    private Set<Long> bizIdInSet;

    @In("module")
    private Set<String> moduleInSet;

    @In("type")
    private Set<Long> typeInSet;

    @In("id")
    private Set<Long> idInSet;
    @NotIn("id")

    private Set<Long> idNotInSet;

}