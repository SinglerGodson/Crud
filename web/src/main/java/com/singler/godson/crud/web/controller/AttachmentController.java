package com.singler.godson.crud.web.controller;

import com.singler.godson.crud.common.exceptions.CrudException;
import com.singler.godson.crud.common.utils.ResponseUtils;
import com.singler.godson.crud.domain.attachment.DownloadDto;
import com.singler.godson.crud.domain.dtoes.attachment.AttachmentQueryRequestVo;
import com.singler.godson.crud.domain.dtoes.attachment.AttachmentResultVo;
import com.singler.godson.crud.domain.entities.attachment.Attachment;
import com.singler.godson.crud.enumtype.CharsetEnum;
import com.singler.godson.crud.service.attachment.AttachmentService;
import com.singler.godson.hibatis.orderby.OrderBy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
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
    public Attachment upload(HttpServletRequest request,
                             @RequestParam("file") MultipartFile file,
                             @ModelAttribute Attachment attachment) throws IOException {
        String encoding = request.getCharacterEncoding();
        if (encoding == null) {
            request.setCharacterEncoding(CharsetEnum.ENCODED_TYPE_UTF8.getCode());
        }
        return attachmentService.upload(transferToFile(file), attachment);
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
        DownloadDto downloadDto = attachmentService.download(module, type, bizId);
        ResponseUtils.response(response, downloadDto.getBytes(), downloadDto.getFileName(), downloadDto.getExtension());
    }

    @RequestMapping(value = "/download/{id}", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public void download(HttpServletResponse response, @PathVariable("id") Long id) {
         DownloadDto downloadDto = attachmentService.download(id);
         ResponseUtils.response(response, downloadDto.getBytes(), downloadDto.getFileName(), downloadDto.getExtension());
    }

    @RequestMapping(value = "/delete/{ids}", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public int delete(@PathVariable("ids") String idStr) {
        return attachmentService.delete(Arrays.stream(idStr.split(",")).map(Long::parseLong).collect(Collectors.toSet()));
    }

    @RequestMapping(value = "/preview/{id}", method = {RequestMethod.GET, RequestMethod.POST}, produces = "text/html;charset=UTF-8")
    public void preview(HttpServletResponse response, @PathVariable("id") Long id) {
        DownloadDto downloadDto = attachmentService.download(id);
        ResponseUtils.response(response, downloadDto.getBytes(), null, downloadDto.getExtension());

    }

    @RequestMapping(value = "/preview/{module}/{type}/{bizId}", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public void preview(HttpServletResponse response,
                         @PathVariable("module") String module,
                         @PathVariable("type") Long type,
                         @PathVariable("bizId") Long bizId) {
        DownloadDto downloadDto = attachmentService.download(module, type, bizId);
        ResponseUtils.response(response, downloadDto.getBytes(), null, downloadDto.getExtension());
    }

    private File transferToFile(MultipartFile multipartFile) {
        if (multipartFile != null) {
            String fileName = multipartFile.getOriginalFilename();
            if (!StringUtils.isEmpty(fileName)) {
                String[] filename = fileName.split("\\.");
                try {
                    File file = File.createTempFile(filename[0]  + "_temp", "." + filename[1]);
                    multipartFile.transferTo(file);
                    return file;
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new CrudException(500, "文件转换失败！");
                }
            }
        }
        return null;
    }
}
