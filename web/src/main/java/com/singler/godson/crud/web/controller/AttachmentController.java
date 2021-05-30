package com.singler.godson.crud.web.controller;

import com.singler.godson.crud.common.utils.MultipartFileUtils;
import com.singler.godson.crud.common.utils.ResponseUtils;
import com.singler.godson.crud.domain.dtoes.attachment.AttachmentQueryRequestVo;
import com.singler.godson.crud.domain.dtoes.attachment.AttachmentResultVo;
import com.singler.godson.crud.domain.entities.attachment.Attachment;
import com.singler.godson.crud.enumtype.CharsetEnum;
import com.singler.godson.crud.service.attachment.AttachmentService;
import com.singler.godson.hibatis.orderby.OrderBy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 附件上传
 * @author lipengpeng
 * @date 2020/4/18
 *
 */
@Slf4j
@RestController
@RequestMapping("/attachment")
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public com.singler.godson.crud.domain.entities.attachment.Attachment upload(HttpServletRequest request,
                                                                                @RequestParam("file") MultipartFile file,
                                                                                @ModelAttribute com.singler.godson.crud.domain.entities.attachment.Attachment attachment) throws IOException {
        String encoding = request.getCharacterEncoding();
        if (encoding == null) {
            request.setCharacterEncoding(CharsetEnum.ENCODED_TYPE_UTF8.getCode());
        }
        return attachmentService.upload(attachment, MultipartFileUtils.transferToFile(file));
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<AttachmentResultVo> query(@ModelAttribute AttachmentQueryRequestVo queryReqDTO) {
        return attachmentService.query(queryReqDTO, OrderBy.DEFAULT);
    }

    @RequestMapping(value = "/download/{module}/{type}/{bizId}", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public void download(HttpServletResponse response,
                         @PathVariable("module") String module,
                         @PathVariable("type") Long type,
                         @PathVariable("bizId") Long bizId) {
        Attachment attachment = attachmentService.download(module, type, bizId);
        ResponseUtils.response(response, attachment.getBytes(), attachment.getName(), attachment.getExt());
    }

    @RequestMapping(value = "/download/{id}", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public void download(HttpServletResponse response, @PathVariable("id") Long id) {
         Attachment attachment = attachmentService.download(id);
         ResponseUtils.response(response, attachment.getBytes(), attachment.getName(), attachment.getExt());
    }

    @RequestMapping(value = "/delete/{ids}", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public int delete(@PathVariable("ids") String idStr) {
        return attachmentService.delete(Arrays.stream(idStr.split(",")).map(Long::parseLong).collect(Collectors.toSet()));
    }

    @RequestMapping(value = "/preview/{id}", method = {RequestMethod.GET, RequestMethod.POST}, produces = "text/html;charset=UTF-8")
    public void preview(HttpServletResponse response, @PathVariable("id") Long id) {
        Attachment attachment = attachmentService.download(id);
        ResponseUtils.response(response, attachment.getBytes(), null, attachment.getExt());

    }

    @RequestMapping(value = "/preview/{module}/{type}/{bizId}", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public void preview(HttpServletResponse response,
                         @PathVariable("module") String module,
                         @PathVariable("type") Long type,
                         @PathVariable("bizId") Long bizId) {
        Attachment attachment = attachmentService.download(module, type, bizId);
        ResponseUtils.response(response, attachment.getBytes(), null, attachment.getExt());
    }
}
