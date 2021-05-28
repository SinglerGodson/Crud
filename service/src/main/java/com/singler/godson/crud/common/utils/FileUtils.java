package com.singler.godson.crud.common.utils;

import java.io.*;

/**
 * 文件工具
 *
 * @author maenfang1
 * @date 2021/5/25 09:48
 */
public class FileUtils {

    public static final String POINT = ".";

    private FileUtils() {}

    public static String extensionOf(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * byte数组转File
     * @param byteArray 字节数组
     * @param name 文件名称
     */
    public static File bytesToFile(byte[] byteArray, String name, String extension) throws IOException {
        File file = null;
        FileOutputStream fos = null;
        InputStream in = new ByteArrayInputStream(byteArray);
        try {
            if (!extension.startsWith(FileUtils.POINT)) {
                extension = FileUtils.POINT + extension;
            }
            file = File.createTempFile(name, extension);
            fos = new FileOutputStream(file);
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = in.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
        } finally {
            if(null != file){
                try{
                    file.deleteOnExit();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    public static byte[] fileToBytes(File file) throws IOException {
        if (file == null || !file.exists()) {
            throw new FileNotFoundException("file not exists");
        }
        int fileSize = (int) file.length();
        BufferedInputStream in = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            int len = 0;
            byte[] buffer = new byte[fileSize];
            in = new BufferedInputStream(new FileInputStream(file));
            while (-1 != (len = in.read(buffer, 0, fileSize))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            bos.close();
        }
    }

}
