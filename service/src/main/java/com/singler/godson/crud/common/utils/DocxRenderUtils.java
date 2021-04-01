package com.singler.godson.crud.common.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 文档处理服务工具类
 *
 * @author maenfang1
 * @version 1.0
 * @date 2021/3/31 20:29
 */
public class DocxRenderUtils {


    /**
     * 方法描述: 从docx文件里获取document.xml放到临时文件里
     * @param
     * @return <br/>
     * 创建者: maenfang1@jd.com<br/>
     * 创建时间: 2021/4/1 9:03<br/>
     * 更新者: maenfang1@jd.com<br/>
     * 更新时间: 2021/4/1 9:03<br/>
     * 备注:
     */
    private static File getDocumentXml(File docxFile) throws IOException {
        ZipFile zipFile = new ZipFile(docxFile);
        Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
        while (zipEntries.hasMoreElements()) {
            ZipEntry next = zipEntries.nextElement();
            if ("word/document.xml".equals(next.toString())) {
                int len;
                byte[] buffer = new byte[1024];
                File tempFile = File.createTempFile("crud_docx_template", "");
                InputStream is = zipFile.getInputStream(next);
                FileOutputStream output = new FileOutputStream(tempFile);
                while ((len = is.read(buffer)) != -1) {
                    output.write(buffer, 0, len);
                }
                is.close();
                output.close();
                return tempFile;
            }
        }
        return null;
    }

    /**
     * 方法描述: 渲染document.xml文件模板
     * 创建者: maenfang1@jd.com<br/>
     * 创建时间: 2021/4/1 9:04<br/>
     * 更新者: maenfang1@jd.com<br/>
     * 更新时间: 2021/4/1 9:04<br/>
     * 备注:
     */
    private static void render(File templateDocumentXml, Object dataModel) throws IOException, TemplateException {
        render(templateDocumentXml, dataModel, "GBK");
    }


    private static void render(File templateDocumentXml, Object dataModel, String outputEncoding) throws IOException, TemplateException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        cfg.setDirectoryForTemplateLoading(templateDocumentXml.getParentFile());
        Template template = cfg.getTemplate(templateDocumentXml.getName());
        template.setOutputEncoding(outputEncoding);
        Writer out = new FileWriter(templateDocumentXml);
        template.process(dataModel, out);
        out.close();
    }

    /**
     * @Description 将渲染好的document.xml模板文件，放入docx文档内。
     * @author belle.wang
     * @throws Exception
     */
    private static File generate(File docxFile, File tmpXmlFile) throws IOException {
        // 带数据的xml生成docx
        ZipFile zipFile = new ZipFile(docxFile);
        Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
        String docxFileName = docxFile.getName();
        int lastDotIndex = docxFileName.lastIndexOf(".");
        String prefix = docxFileName.substring(0, lastDotIndex);
        // File.createTempFile 当prefix长度小于3时，抛异常。
        if (prefix.length() < 3) {
            prefix += "_temp";
        }
        String suffix = docxFileName.substring(lastDotIndex);
        File targetFile = File.createTempFile(prefix, suffix);
        ZipOutputStream zipout = new ZipOutputStream(new FileOutputStream(targetFile));
        int len = -1;
        byte[] buffer = new byte[1024];
        while (zipEntries.hasMoreElements()) {
            ZipEntry next = zipEntries.nextElement();
            InputStream is = zipFile.getInputStream(next);
            // 把输入流的文件传到输出流中 如果是word/document.xml由我们输入
            zipout.putNextEntry(new ZipEntry(next.toString()));
            if ("word/document.xml".equals(next.toString())) {
                InputStream in = new FileInputStream(tmpXmlFile);
                while ((len = in.read(buffer)) != -1) {
                    zipout.write(buffer, 0, len);
                }
                in.close();
            } else {
                while ((len = is.read(buffer)) != -1) {
                    zipout.write(buffer, 0, len);
                }
                is.close();
            }
        }
        zipout.close();
        tmpXmlFile.delete();
        return targetFile;
    }

    public static File exportDocx(File templateDocx, Map<String, Object> data) throws IOException, TemplateException {
        File documentXml = getDocumentXml(templateDocx);
        render(documentXml, data);
        return generate(templateDocx, documentXml);
    }


    public static void main(String[] args) throws Exception {
        File docxFile = new File("E:/a.docx");
        Map<String, Object> map = new HashMap<>();
        map.put("totalPrice", 123456789);
        exportDocx(docxFile, map);
    }
}
