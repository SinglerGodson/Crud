package com.singler.godson.crud.common.utils;

import com.singler.godson.crud.common.exceptions.CrudException;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * multipartFile utils
 *
 * @author maenfang1
 * @date 2021/5/28 18:36
 */
public class MultipartFileUtils {

    private MultipartFileUtils() {}

    public static File transferToFile(MultipartFile multipartFile) {
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
                    throw new CrudException("500", "文件转换失败！");
                }
            }
        }
        return null;
    }
}
