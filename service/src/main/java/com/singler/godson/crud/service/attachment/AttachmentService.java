package com.singler.godson.crud.service.attachment;

import com.singler.godson.crud.domain.dtoes.attachment.AttachmentCountResult;
import com.singler.godson.crud.domain.dtoes.attachment.AttachmentQueryRequest;
import com.singler.godson.crud.domain.dtoes.attachment.AttachmentResult;
import com.singler.godson.crud.domain.dtoes.attachment.AttachmentSaveRequest;
import com.singler.godson.crud.domain.entities.attachment.Attachment;
import com.singler.godson.crud.service.CrudService;
import com.singler.godson.hibatis.orderby.OrderBy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

/**
 * 附件service
 * @author lipengpeng
 * @date 2020/4/18
 */
public interface AttachmentService extends CrudService<Long, Attachment, AttachmentSaveRequest, AttachmentQueryRequest, AttachmentResult> {

    Long UN_RELATED_BIZ_ID = 0L;

    Attachment upload(File file) throws IOException;

    Attachment upload(String fileName, File file) throws IOException;
    Attachment upload(String fileName, byte[] bytes) throws IOException;
    Attachment upload(String fileName, InputStream inputStream) throws IOException;

    Attachment upload(Attachment attachment);
    Attachment upload(Attachment attachment, File file) throws IOException;
    Attachment upload(Attachment attachment, InputStream inputStream) throws IOException;

    Attachment download(Long id);
    Attachment download(Attachment attachment);
    Attachment download(String module, Long type, Long bizId);
    Attachment download(String path, String fileName);

    /**
     * 根据附件id将附件下载到本地服务器，并返回此文件。
     * 注意：此file为本地服务器上的临时文件，使用file后一定要删除。
     * @param id 附件id
     * @return
     */
    File downloadFile(Long id);
    byte[] downloadBytes(Long id);
    InputStream downloadInputStream(Long id);

    /**
     * 根据附件信息将附件下载到本地服务器，并返回此文件。
     * 注意：此file为本地服务器上的临时文件，使用file后一定要删除。
     * @param attachment 附件信息
     * @return
     */
    File downloadFile(Attachment attachment);
    byte[] downloadBytes(Attachment attachment);
    InputStream downloadInputStream(Attachment attachment);

    /**
     * 根据附件模块、类型、业务id将附件下载到本地服务器，并返回此文件。
     * 注意：此file为本地服务器上的临时文件，使用file后一定要删除。
     * @param module 附件所属模块
     * @param type 附件类型
     * @param bizId 附件业务id
     * @return
     */
    File downloadFile(String module, Long type, Long bizId);
    byte[] downloadBytes(String module, Long type, Long bizId);
    InputStream downloadInputStream(String module, Long type, Long bizId);

    int delete(Set<Long> idSet);
    int delete(String module, Long type, Long bizId);

    int update(String module, Long type, Long bizId, Long id);
    int update(List<AttachmentSaveRequest> attachmentEntityList);

    AttachmentResult queryByBizId(String module, Long type, Long bizId);
    List<AttachmentResult> queryByBizIdSet(String module, Long type, Set<Long> bizIdSet);
    List<AttachmentCountResult> countByBizIdSet(String module, Long type, Set<Long> bizIdSet);

    AttachmentResult query(Long bizId);
    AttachmentResult query(Long bizId, String module, Long type);

    List<AttachmentResult> queryByBizId(Long bizId);
    List<AttachmentResult> queryByBizId(Long bizId, OrderBy orderBy);

    List<AttachmentResult> queryByBizId(List<Long> bizIdList);
    List<AttachmentResult> queryByBizIdSet(Set<Long> bizIdSet);

    List<AttachmentResult> queryByIdAndBizId(List<AttachmentSaveRequest> attachmentVoList, Long id);

}
