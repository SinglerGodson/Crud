package com.singler.godson.crud.service.attachment.impl;

import com.singler.godson.crud.common.exceptions.CrudException;
import com.singler.godson.crud.common.utils.CollectionUtils;
import com.singler.godson.crud.common.utils.FileUtils;
import com.singler.godson.crud.dao.attachment.AttachmentDao;
import com.singler.godson.crud.domain.dtoes.attachment.AttachmentCountResultVo;
import com.singler.godson.crud.domain.dtoes.attachment.AttachmentQueryRequestVo;
import com.singler.godson.crud.domain.dtoes.attachment.AttachmentResultVo;
import com.singler.godson.crud.domain.dtoes.attachment.AttachmentSaveRequestVo;
import com.singler.godson.crud.domain.entities.attachment.Attachment;
import com.singler.godson.crud.enumtype.ContentTypeEnum;
import com.singler.godson.crud.service.AbstractCrudService;
import com.singler.godson.crud.service.attachment.AttachmentService;
import com.singler.godson.crud.service.attachment.UploadedService;
import com.singler.godson.hibatis.orderby.OrderBy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 附件serviceImpl
 *
 * @author lipengpeng
 * @date 2020/4/18
 */
@Service
@Slf4j
public class AttachmentServiceImpl extends AbstractCrudService<Long, Attachment,
        AttachmentSaveRequestVo, AttachmentQueryRequestVo, AttachmentResultVo, AttachmentDao>
        implements AttachmentService {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Attachment upload(File file) throws IOException {
        return upload(file.getName(), file);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Attachment upload(String fileName, File file) throws IOException {
        return upload(new Attachment(fileName), file);
    }

    @Override
    public Attachment upload(String fileName, byte[] bytes) throws IOException {
        return upload(new Attachment(fileName, bytes));
    }

    @Override
    public Attachment upload(String fileName, InputStream inputStream) throws IOException {
        return upload(new Attachment(fileName), inputStream);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Attachment upload(Attachment attachment, File file) throws IOException {
        byte[] bytes = FileUtils.fileToBytes(file);
        if (StringUtils.isEmpty(attachment.getName())) {
            attachment.setName(file.getName());
        }
        attachment.setBytes(bytes);
        return upload(attachment);
    }

    @Override
    public Attachment upload(Attachment attachment, InputStream inputStream) throws IOException {
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        attachment.setBytes(bytes);
        return upload(attachment);
    }

    @Override
    public Attachment upload(Attachment attachment) {
        if (attachment.getBizId() == null) {
            attachment.setBizId(UN_RELATED_BIZ_ID);
        }
        attachment.setSize((long) attachment.getBytes().length);
        attachment.setMd5(DigestUtils.md5Hex(attachment.getBytes()));
        String fileName = attachment.getName();
        if (!StringUtils.isEmpty(fileName)) {
            attachment.setExt(fileName.substring(fileName.lastIndexOf(FileUtils.POINT) + 1));
        }
//        attachment.setUrl(ossClientProxy.uploadFile(attachment.getPlatformId(), bytes, fileName));
        getDao().insert(attachment);
        return uploaded(attachment);
    }

    private Attachment uploaded(Attachment attachment) {
        UploadedService uploadedService = getUploadedService(attachment);
        if (uploadedService != null) {
            uploadedService.uploaded(attachment);
        }
        return attachment;
    }

    private static final String UPLOADED_SERVICE_NAME = "%s%dUploadedServiceImpl";

    private UploadedService getUploadedService(Attachment attachment) {
        String uploadedServiceName = String.format(UPLOADED_SERVICE_NAME, attachment.getModule(), attachment.getType());
        try {
            return applicationContext.getBean(uploadedServiceName, UploadedService.class);
        } catch (BeansException e) {
            return null;
        }
    }

    @Override
    public Attachment download(Long id) {
        return download(this.queryById(id));
    }

    @Override
    public Attachment download(Attachment attachment) {
        return new Attachment(downloadBytes(attachment), attachment.getName(), ContentTypeEnum.getContentType(attachment.getExt()));
    }

    @Override
    public Attachment download(String module, Long type, Long bizId) {
        AttachmentQueryRequestVo queryVo = new AttachmentQueryRequestVo();
        queryVo.setType(type);
        queryVo.setBizId(bizId);
        queryVo.setModule(module);
        return download(this.query(queryVo));
    }


    private static final String TEMPLATE_PATH = getTemplatePath();
    private static String getTemplatePath() {
        try {
            return ResourceUtils.getURL(ResourceUtils.CLASSPATH_URL_PREFIX).getPath() + "templates/%s/%s";
        } catch (FileNotFoundException e) {
            log.error("获取资源路径失败。", e);
        }
        return "";
    }

    private static final Map<String, Attachment> ATTACHMENT_MAP = new ConcurrentHashMap<>();
    @Override
    public Attachment download(String packageName, String fileName) {
        String filePath = String.format(TEMPLATE_PATH, packageName, fileName);
        Attachment attachment = ATTACHMENT_MAP.get(filePath);
        if (attachment == null) {
            synchronized (ATTACHMENT_MAP) {
                attachment = new Attachment();
                try (InputStream inputStream = new FileInputStream(filePath)) {
                    byte[] bytes = new byte[inputStream.available()];
                    inputStream.read(bytes);
                    attachment.setBytes(bytes);
                } catch (IOException e) {
                    log.error("文件加载失败。", e);
                }
                attachment.setName(fileName);
                attachment.setExt(FileUtils.extensionOf(fileName));
                ATTACHMENT_MAP.put(filePath, attachment);
            }
        }
        return attachment;
    }


    /**
     * 根据附件id将附件下载到本地服务器，并返回此文件。
     * 注意：此file为本地服务器上的临时文件，使用file后一定要删除。
     * @param id 附件id
     * @return
     */
    @Override
    public File downloadFile(Long id) {
        return downloadFile(queryById(id));
    }
    /**
     * 根据附件模块、类型、业务id将附件下载到本地服务器，并返回此文件。
     * 注意：此file为本地服务器上的临时文件，使用file后一定要删除。
     * @param module 附件所属模块
     * @param type 附件类型
     * @param bizId 附件业务id
     * @return
     */
    @Override
    public File downloadFile(String module, Long type, Long bizId) {
        return downloadFile(query(bizId, module, type));
    }
    /**
     * 根据附件信息将附件下载到本地服务器，并返回此文件。
     * 注意：此file为本地服务器上的临时文件，使用file后一定要删除。
     * @param attachment 附件信息
     * @return
     */
    @Override
    public File downloadFile(Attachment attachment) {
        try {
            return FileUtils.bytesToFile(downloadBytes(attachment), attachment.getName(), attachment.getExt());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] downloadBytes(Long id) {
        return downloadBytes(queryById(id));
    }

    @Override
    public InputStream downloadInputStream(Long id) {
        return downloadInputStream(queryById(id));
    }

    @Override
    public byte[] downloadBytes(Attachment attachment) {
        if (attachment == null || StringUtils.isEmpty(attachment.getUrl())) {
            throw new CrudException(1003, "参数不能为空");
        }
//        return ossClientProxy.getObjByteArr(attachment.getUrl());
        return null;
    }

    @Override
    public InputStream downloadInputStream(Attachment attachment) {
        return new ByteArrayInputStream(downloadBytes(attachment));
    }

    @Override
    public byte[] downloadBytes(String module, Long type, Long bizId) {
        return downloadBytes(query(bizId, module, type));
    }

    @Override
    public InputStream downloadInputStream(String module, Long type, Long bizId) {
        return downloadInputStream(query(bizId, module, type));
    }

    @Override
    public int delete(String module, Long type, Long bizId) {
        if (StringUtils.isEmpty(module)) {
            throw new IllegalArgumentException("module can not be empty");
        }
        if (type == null) {
            throw new IllegalArgumentException("type can not be empty");
        }

        return delete(transfer(module, type, bizId, null));
    }

    @Override
    public int delete(Set<Long> idSet) {
        if (!CollectionUtils.isEmpty(idSet)) {
            AttachmentQueryRequestVo queryVo = new AttachmentQueryRequestVo();
            queryVo.setIdInSet(idSet);
            return delete(queryVo);
        }
        return 0;
    }

    @Override
    public int update(List<AttachmentSaveRequestVo> attachmentList) {
        if (!CollectionUtils.isEmpty(attachmentList)) {
            boolean emptyBizIdOrId  = attachmentList.stream().anyMatch(this::emptyAttachment);
            if (emptyBizIdOrId) {
                throw new CrudException(500, "bizId与id不能全为空");
            }

            boolean emptyModule = attachmentList.stream().anyMatch(a -> StringUtils.isEmpty(a.getModule()));
            if (!emptyModule) {
                // module\emptyBizId不为空时，重置不在id范围内的数据的bizId（即清除不在id范围内的关联数据，模拟先删后增效果）。
                AttachmentQueryRequestVo queryVo = new AttachmentQueryRequestVo();
                queryVo.setIdNotInSet (attachmentList.stream().map(AttachmentSaveRequestVo::getId    ).filter(Objects::nonNull).collect(Collectors.toSet()));
                queryVo.setTypeInSet  (attachmentList.stream().map(AttachmentSaveRequestVo::getType  ).filter(Objects::nonNull).collect(Collectors.toSet()));
                queryVo.setBizIdInSet (attachmentList.stream().map(AttachmentSaveRequestVo::getBizId ).filter(Objects::nonNull).collect(Collectors.toSet()));
                queryVo.setModuleInSet(attachmentList.stream().map(AttachmentSaveRequestVo::getModule).filter(Objects::nonNull).collect(Collectors.toSet()));
                AttachmentSaveRequestVo clareVo = new AttachmentSaveRequestVo();
                clareVo.setBizId(UN_RELATED_BIZ_ID);
                getDao().update(clareVo, queryVo);
            }
            return getDao().updateBatch(attachmentList);
        }
        return 0;
    }

    /**
     * 判断是否为空的附件
     * @param attachment
     * @return
     */
    private boolean emptyAttachment(Attachment attachment) {
        return attachment.getType() == null
            && attachment.getBizId() == null
            && StringUtils.isEmpty(attachment.getModule());
    }


    @Override
    public int update(String module, Long type, Long bizId, Long id) {
        AttachmentSaveRequestVo queryVo = new AttachmentSaveRequestVo();
        queryVo.setType(type);
        queryVo.setBizId(bizId);
        queryVo.setModule(module);
        return updateById(queryVo, id);
    }

    @Override
    public List<AttachmentResultVo> queryByBizIdSet(String module, Long type, Set<Long> bizIdSet) {
        return query(transfer(module, type, null, bizIdSet), OrderBy.DEFAULT);
    }

    @Override
    public AttachmentResultVo queryByBizId(String module, Long type, Long bizId) {
        AttachmentQueryRequestVo vo = new AttachmentQueryRequestVo();
        vo.setBizId(bizId);
        vo.setModule(module);
        vo.setType(type);
        return this.query(vo);
    }

    @Override
    public List<AttachmentCountResultVo> countByBizIdSet(String module, Long type, Set<Long> bizIdSet) {
        return getDao().countByBizIdSet(transfer(module, type, null, bizIdSet));
    }

    @Override
    public void checkIdExist(List<AttachmentSaveRequestVo> attachmentVoList, List<AttachmentResultVo> attachmentList) {
        // 附件表信息Map
        Map<Long, AttachmentResultVo> attachmentResultMap
                = attachmentList.stream().filter(Objects::nonNull).collect(Collectors.toMap(AttachmentResultVo::getId, info -> info, (v1, v2) -> v1));
        attachmentVoList.forEach(info -> {
            Long fileId = info.getId();
            if (null != info.getId()) {
                if (!attachmentResultMap.containsKey(fileId)) {
                    throw new CrudException(500, "附件类型:"+info.getFileTypeCode()+",上传附件信息不存在!");
                }
            }
        });
    }

    @Override
    public AttachmentResultVo query(Long bizId) {
        AttachmentQueryRequestVo vo = new AttachmentQueryRequestVo();
        vo.setBizId(bizId);
        return this.query(vo);
    }

    @Override
    public AttachmentResultVo query(Long bizId,String module,Long type) {
        AttachmentQueryRequestVo vo = new AttachmentQueryRequestVo();
        vo.setBizId(bizId);
        vo.setModule(module);
        vo.setType(type);
        return this.query(vo);
    }

    @Override
    public List<AttachmentResultVo> queryByBizId(Long bizId) {
        return this.queryByBizId(bizId,OrderBy.DEFAULT);
    }

    @Override
    public List<AttachmentResultVo> queryByBizId(List<Long> bizIdList) {
        if (!CollectionUtils.isEmpty(bizIdList)) {
            Set<Long> bizIdSet = new HashSet<>(bizIdList);
            AttachmentQueryRequestVo vo = new AttachmentQueryRequestVo();
            vo.setBizIdInSet(bizIdSet);
            return this.query(vo,OrderBy.DEFAULT);
        }
        return Lists.newArrayList();
    }

    @Override
    public List<AttachmentResultVo> queryByBizId(Long bizId, OrderBy orderBy) {
        if (bizId == null) {
            AttachmentQueryRequestVo vo = new AttachmentQueryRequestVo();
            vo.setBizId(bizId);
            return this.query(vo, orderBy);
        }
        return Lists.newArrayList();
    }

    @Override
    public List<AttachmentResultVo> queryByIdAndBizId(List<AttachmentSaveRequestVo> attachmentVoList, Long bizId) {
        if (null == bizId) {
            throw new CrudException(500,"协议数据不存在！");
        }
        Set<Long> saveIdList = attachmentVoList.stream().map(AttachmentSaveRequestVo::getId).filter(Objects::nonNull).collect(Collectors.toSet());
        // 查询附件上传表
        List<AttachmentResultVo> attachmentResultVos = this.queryByBizIdSet(new HashSet<Long>() {{add(bizId);}});
        List<AttachmentResultVo> queryAttachmentList = this.query(new AttachmentQueryRequestVo() {{setIdInSet(saveIdList);}}, OrderBy.DEFAULT);
        if (CollectionUtils.isEmpty(attachmentResultVos)) {
            attachmentResultVos = queryAttachmentList;
        } else {
            attachmentResultVos.addAll(queryAttachmentList);
        }
        return attachmentResultVos;
    }

    @Override
    public List<AttachmentResultVo> queryByBizIdSet(Set<Long> idSet) {
        return getDao().select(OrderBy.DEFAULT, transfer(null, null, null, idSet));
    }

    private AttachmentQueryRequestVo transfer(String module, Long type, Long bizId, Set<Long> bizIdSet) {
        AttachmentQueryRequestVo queryVo = new AttachmentQueryRequestVo();
        queryVo.setType(type);
        queryVo.setBizId(bizId);
        queryVo.setModule(module);
        queryVo.setBizIdInSet(bizIdSet);
        return queryVo;
    }


}
