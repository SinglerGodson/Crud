package com.singler.godson.crud.service.attachment;

import com.singler.godson.crud.domain.attachment.DownloadDto;
import com.singler.godson.crud.domain.dtoes.attachment.AttachmentCountResultVo;
import com.singler.godson.crud.domain.dtoes.attachment.AttachmentQueryRequestVo;
import com.singler.godson.crud.domain.dtoes.attachment.AttachmentResultVo;
import com.singler.godson.crud.domain.dtoes.attachment.AttachmentSaveRequestVo;
import com.singler.godson.crud.domain.entities.attachment.Attachment;
import com.singler.godson.crud.enumtype.ContentTypeEnum;
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
public interface AttachmentService extends CrudService<Long, Attachment, AttachmentSaveRequestVo, AttachmentQueryRequestVo, AttachmentResultVo> {

    Long UN_RELATED_BIZ_ID = 0L;

    Attachment upload(File file) throws IOException;

    Attachment upload(File file, String fileName) throws IOException;
    Attachment upload(byte[] bytes, String fileName) throws IOException;
    Attachment upload(InputStream inputStream, String fileName) throws IOException;

    Attachment upload(File file, Attachment attachment) throws IOException;
    Attachment upload(byte[] bytes, Attachment attachment) throws IOException;
    Attachment upload(InputStream inputStream, Attachment attachment) throws IOException;

    Attachment upload(File file, String module, Long type, Long bizId) throws IOException;
    Attachment upload(byte[] bytes, String module, Long type, Long bizId) throws IOException;
    Attachment upload(InputStream inputStream, String module, Long type, Long bizId) throws IOException;


    DownloadDto download(Long id);
    DownloadDto download(Attachment attachment);
    DownloadDto download(String module, Long type, Long bizId);

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
    int update(List<AttachmentSaveRequestVo> attachmentEntityList);

    AttachmentResultVo queryByBizId(String module,Long type, Long bizId);
    List<AttachmentResultVo> queryByBizIdSet(String module, Long type, Set<Long> bizIdSet);
    List<AttachmentCountResultVo> countByBizIdSet(String module, Long type, Set<Long> bizIdSet);

    void checkIdExist(List<AttachmentSaveRequestVo> attachmentVoList, List<AttachmentResultVo> attachmentList);

    AttachmentResultVo query(Long bizId);
    AttachmentResultVo query(Long bizId,String module,Long type);

    List<AttachmentResultVo> queryByBizId(Long bizId);
    List<AttachmentResultVo> queryByBizId(Long bizId, OrderBy orderBy);

    List<AttachmentResultVo> queryByBizId(List<Long> bizIdList);
    List<AttachmentResultVo> queryByBizIdSet(Set<Long> bizIdSet);

    List<AttachmentResultVo> queryByIdAndBizId(List<AttachmentSaveRequestVo> attachmentVoList, Long id);

}
