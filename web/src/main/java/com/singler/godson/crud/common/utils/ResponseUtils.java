package com.singler.godson.crud.common.utils;

import com.singler.godson.crud.common.exceptions.CrudException;
import com.singler.godson.crud.common.enums.ContentTypeEnum;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * 响应工具类
 *
 * @author maenfang1
 * @date 2021/5/25 08:59
 */
public class ResponseUtils {

    private ResponseUtils(){}


    public static void response(HttpServletResponse response, File file, String downloadFileName, ContentTypeEnum contentTypeEnum) {
        try {
            response(response, new FileInputStream(file), downloadFileName, contentTypeEnum);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void response(HttpServletResponse response, InputStream inputStream, String downloadFileName, ContentTypeEnum contentTypeEnum) {
        response(response, inputStream, downloadFileName, contentTypeEnum.getExtension());
    }

    public static void response(HttpServletResponse response, InputStream inputStream, String downloadFileName, String extension) {
        if (inputStream != null) {
            try {
                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes);
                response(response, bytes, downloadFileName, extension);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void response(HttpServletResponse response, byte[] bytes, String downloadFileName, String extension) {
        response(response, bytes, downloadFileName, ContentTypeEnum.getByExtension(extension));
    }

    public static void response(HttpServletResponse response, byte[] bytes, String downloadFileName, ContentTypeEnum contentTypeEnum) {
        if (response != null) {
            response.reset();
            response.setContentType(contentTypeEnum.getContentType());
            response.addHeader("Content-Length", String.valueOf(bytes.length));
            try (OutputStream out = response.getOutputStream()) {
                if (!StringUtils.isEmpty(downloadFileName)) {
                    response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(downloadFileName, "UTF-8") );
                }
                if (PoiUtils.isOfficeFile(contentTypeEnum.getExtension()) && StringUtils.isEmpty(downloadFileName)) {
                    bytes = PoiUtils.officeToHtml(bytes, contentTypeEnum.getExtension());
                }
                out.write(bytes);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static HttpServletResponse getResponse() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getResponse();
        }
        throw new CrudException("1002", "无效request，无法获取response");
    }
}
