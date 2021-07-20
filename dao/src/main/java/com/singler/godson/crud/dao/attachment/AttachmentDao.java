package com.singler.godson.crud.dao.attachment;

import com.singler.godson.crud.dao.CrudDao;
import com.singler.godson.crud.domain.dtoes.attachment.AttachmentCountResult;
import com.singler.godson.crud.domain.dtoes.attachment.AttachmentQueryRequest;
import com.singler.godson.crud.domain.dtoes.attachment.AttachmentResult;
import com.singler.godson.crud.domain.dtoes.attachment.AttachmentSaveRequest;
import com.singler.godson.crud.domain.entities.attachment.Attachment;
import com.singler.godson.hibatis.where.WhereClause;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 附件信息
 *
 * @author maenfang1
 * @date 2021-3-22 16:44:35
 */
@Repository
public interface AttachmentDao extends CrudDao<Long, Attachment> {

    String ATTACHMENT_LIST = "attachmentList";

    @Override
    AttachmentResult select(@Param(WHERE_CLAUSES) WhereClause... whereClauses);

    int updateBatch(@Param(ATTACHMENT_LIST) List<AttachmentSaveRequest> attachmentList);

    List<AttachmentCountResult> countByBizIdSet(AttachmentQueryRequest queryRequestVo);

}
