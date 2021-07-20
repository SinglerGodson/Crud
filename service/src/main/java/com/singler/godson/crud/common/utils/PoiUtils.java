package com.singler.godson.crud.common.utils;

import com.singler.godson.crud.common.enums.ContentTypeEnum;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLConverter;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLOptions;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Enumeration;
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
public class PoiUtils {


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
    private static File getDocumentXmlDocx(File docxFile) throws IOException {
        try (ZipFile zipFile = new ZipFile(docxFile)) {
            Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
            while (zipEntries.hasMoreElements()) {
                ZipEntry next = zipEntries.nextElement();
                if ("word/document.xml".equals(next.toString())) {
                    int len;
                    byte[] buffer = new byte[1024];
                    File tempFile = File.createTempFile("jcy_docx_template", "");
                    try (InputStream is = zipFile.getInputStream(next); FileOutputStream output = new FileOutputStream(tempFile)) {
                        while ((len = is.read(buffer)) != -1) {
                            output.write(buffer, 0, len);
                        }
                    }
                    return tempFile;
                }
            }
        }
        return null;
    }

    /**
     * @Description 将渲染好的document.xml模板文件，放入docx文档内。
     * @author belle.wang
     * @throws Exception
     */
    private static File generate(File docxFile, File tmpXmlFile) throws IOException {
        // 带数据的xml生成docx
        File targetFile;
        try (ZipFile zipFile = new ZipFile(docxFile)) {
            Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
            String docxFileName = docxFile.getName();
            int lastDotIndex = docxFileName.lastIndexOf(".");
            String prefix = docxFileName.substring(0, lastDotIndex);
            // File.createTempFile 当prefix长度小于3时，抛异常。
            if (prefix.length() < 3) {
                prefix += "_temp";
            }
            String suffix = docxFileName.substring(lastDotIndex);
            targetFile = File.createTempFile(prefix, suffix);
            try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(targetFile))) {
                int len = -1;
                byte[] buffer = new byte[1024];
                while (zipEntries.hasMoreElements()) {
                    ZipEntry next = zipEntries.nextElement();
                    InputStream is = zipFile.getInputStream(next);
                    // 把输入流的文件传到输出流中 如果是word/document.xml由我们输入
                    zos.putNextEntry(new ZipEntry(next.toString()));
                    if ("word/document.xml".equals(next.toString())) {
                        try (InputStream in = new FileInputStream(tmpXmlFile)) {
                            while ((len = in.read(buffer)) != -1) {
                                zos.write(buffer, 0, len);
                            }
                        }
                    } else {
                        while ((len = is.read(buffer)) != -1) {
                            zos.write(buffer, 0, len);
                        }
                        is.close();
                    }
            }
            }
        }
        tmpXmlFile.deleteOnExit();
        return targetFile;
    }

    public static final String DOC = "doc";
    public static final String DOCX = "docx";
    public static File exportWord(File templateWord, String outputEncoding, Object data) throws IOException, TemplateException {
        String wordPath = templateWord.getCanonicalPath();
        File documentXml = null;
        if (wordPath.endsWith(DOCX)) {
            documentXml = renderDocx(templateWord, outputEncoding, data);
        } else if (wordPath.endsWith(DOC)) {
            documentXml = renderDoc(templateWord, outputEncoding, data);
        }
        return documentXml;
    }

    public static File renderDocx(File docxFile, String outputEncoding, Object data) throws IOException, TemplateException {
        File documentXml = getDocumentXmlDocx(docxFile);
        if (documentXml != null) {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
            cfg.setDirectoryForTemplateLoading(documentXml.getParentFile());
            Template template = cfg.getTemplate(documentXml.getName(), "UTF-8");
            template.setOutputEncoding(outputEncoding);
            try (Writer out = new FileWriter(documentXml)) {
                template.process(data, out);
            }
            documentXml = generate(docxFile, documentXml);
        }
        return documentXml;
    }

    public static File renderDoc(File ftlFile, String outputEncoding, Object data) throws IOException, TemplateException {
        File docFile = File.createTempFile(ftlFile.getName(), ".doc");
        try (Writer docWriter = new FileWriter(docFile)) {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
            cfg.setDirectoryForTemplateLoading(ftlFile.getParentFile());
            Template template = cfg.getTemplate(ftlFile.getName(), "UTF-8");
            template.setOutputEncoding(outputEncoding);
            template.process(data, docWriter);
            docWriter.flush();
        }
        return docFile;
    }


    /**
     * doc文件读取
     * @param file
     * @return 返回字符串url
     * @throws IOException
     */
    public static String readDocText(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return new WordExtractor(fis).getText();
        }
    }


    /**
     * docx文件读取
     * @return 返回字符串url
     * @throws IOException
     */
    public static String readDocxText(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return new XWPFWordExtractor(new XWPFDocument(fis)).getText();
        }
    }

    /**
     * doc文档转HTMl文档
     * @param docFile
     * @return
     * @throws Exception
     */
    public static File docToHtml(File docFile) {
        try (FileInputStream inputStream = new FileInputStream(docFile)) {
            return outputStreamToHtmlFile(docFile.getPath(), xlsToHtml(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] docToHtml(byte[] bytes) {
        try (InputStream inputStream = new ByteArrayInputStream(bytes);
             ByteArrayOutputStream outputStream = docToHtml(inputStream)) {
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ByteArrayOutputStream docToHtml(InputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (HWPFDocument wordDocument = new HWPFDocument(inputStream)) {
            org.w3c.dom.Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(document);
            wordToHtmlConverter.processDocument(wordDocument);
            org.w3c.dom.Document htmlDocument = wordToHtmlConverter.getDocument();
            DOMSource domSource = new DOMSource(htmlDocument);
            StreamResult streamResult = new StreamResult(byteArrayOutputStream);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(domSource, streamResult);
        } catch (IOException | ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream;
    }


    public static File docxToHtml(File docxFile) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(docxFile)) {
            return outputStreamToHtmlFile(docxFile.getPath(), xlsToHtml(inputStream));
        }
    }

    public static byte[] docxToHtml(byte[] bytes) {
        try (InputStream inputStream = new ByteArrayInputStream(bytes);
             ByteArrayOutputStream outputStream = docxToHtml(inputStream)) {
            return outputStream == null ? null : outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ByteArrayOutputStream docxToHtml(InputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (XWPFDocument doc = new XWPFDocument(inputStream)) {
            XHTMLOptions options = XHTMLOptions.create();
            XHTMLConverter.getInstance().convert(doc, byteArrayOutputStream, options);
            return byteArrayOutputStream;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Workbook生成HTML方法
     * @param bytes excel生成的Workbook  (可读取io转换生成workbook)
     * @return
     */
    public static byte[] xlsToHtml(byte[] bytes) {
        try (InputStream inputStream = new ByteArrayInputStream(bytes);
             ByteArrayOutputStream outputStream = xlsToHtml(inputStream)) {
            return outputStream == null ? null : outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File xlsToHtml(File xlsFile) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(xlsFile)) {
            return outputStreamToHtmlFile(xlsFile.getPath(), xlsToHtml(inputStream));
        }
    }

    private static File outputStreamToHtmlFile(String path, ByteArrayOutputStream outputStream) {
        FileOutputStream fileOutputStream = null;
        try {
            File htmlFile = File.createTempFile(path, ".html");
            fileOutputStream = new FileOutputStream(htmlFile);
            if (outputStream != null) {
                fileOutputStream.write(outputStream.toByteArray());
            }
            return htmlFile;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
            }
        }
        return null;
    }


    public static ByteArrayOutputStream xlsToHtml(InputStream inputStream) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()){
            ExcelToHtmlConverter converter = new ExcelToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
            converter.setOutputRowNumbers(false);
            converter.setOutputColumnHeaders(false);
            converter.processWorkbook(new HSSFWorkbook(inputStream));
            org.w3c.dom.Document htmlDocument = converter.getDocument();
            DOMSource domSource = new DOMSource(htmlDocument);
            StreamResult streamResult = new StreamResult(out);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(domSource, streamResult);
            return out;
        } catch (TransformerException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static byte[] xlsxToHtml(byte[] bytes) {
        try (InputStream inputStream = new ByteArrayInputStream(bytes);
             ByteArrayOutputStream outputStream = xlsxToHtml(inputStream)) {
            return outputStream == null ? null : outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Excel(xlxs格式)转html
     *
     */
    public static File xlsxToHtml(File xlsxFile) throws IOException {
        try (InputStream inputStream = new FileInputStream(xlsxFile);
             ByteArrayOutputStream outputStream = xlsxToHtml(inputStream)) {
            return outputStreamToHtmlFile(xlsxFile.getPath(), outputStream);
        }
    }


    public static ByteArrayOutputStream xlsxToHtml(InputStream inputStream) {
        try (Workbook workbook = new XSSFWorkbook(inputStream)){
            StringBuilder html = new StringBuilder("");
            for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
                Sheet sheet = workbook.getSheetAt(numSheet);
                if (sheet == null) {
                    continue;
                }
                html.append(sheet.getSheetName()).append("<br><br>");
                int firstRowIndex = sheet.getFirstRowNum();
                int lastRowIndex = sheet.getLastRowNum();
                html.append("<table border='1' align='left'>");
                Row firstRow = sheet.getRow(firstRowIndex);
                for (int i = firstRow.getFirstCellNum(); i <= firstRow.getLastCellNum(); i++) {
                    Cell cell = firstRow.getCell(i);
                    String cellValue = getCellValue(cell);
                    html.append("<th>").append(cellValue).append("</th>");
                }

                // 行
                for (int rowIndex = firstRowIndex + 1; rowIndex <= lastRowIndex; rowIndex++) {
                    Row currentRow = sheet.getRow(rowIndex);
                    html.append("<tr>");
                    if (currentRow != null) {
                        int firstColumnIndex = currentRow.getFirstCellNum();
                        int lastColumnIndex = currentRow.getLastCellNum();
                        // 列
                        for (int columnIndex = firstColumnIndex; columnIndex <= lastColumnIndex; columnIndex++) {
                            Cell currentCell = currentRow.getCell(columnIndex);
                            String currentCellValue = getCellValue (currentCell);
                            html.append("<td>").append(currentCellValue).append("</td>");
                        }
                    } else {
                        html.append(" ");
                    }
                    html.append("</tr>");
                }
                html.append("</table>");
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                DOMSource domSource = new DOMSource();
                StreamResult streamResult = new StreamResult(outStream);
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer serializer = tf.newTransformer();
                serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
                serializer.setOutputProperty(OutputKeys.INDENT, "yes");
                serializer.setOutputProperty(OutputKeys.METHOD, "html");
                serializer.transform(domSource, streamResult);
                outStream.write(html.toString().getBytes("GBK"));
                return outStream;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isOfficeFile(String extension) {
       return isOfficeFile(ContentTypeEnum.getByExtension(extension));
    }
    public static boolean isOfficeFile(ContentTypeEnum contentTypeEnum) {
        return contentTypeEnum == ContentTypeEnum.DOC
            || contentTypeEnum == ContentTypeEnum.DOCX
            || contentTypeEnum == ContentTypeEnum.XLS
            || contentTypeEnum == ContentTypeEnum.XLSX;
    }

    public static byte[] officeToHtml(byte[] bytes, String extension) {
        return officeToHtml(bytes, ContentTypeEnum.getByExtension(extension));
    }

    public static byte[] officeToHtml(byte[] bytes, ContentTypeEnum contentTypeEnum) {
        switch (contentTypeEnum) {
            case XLS : return xlsToHtml(bytes);
            case XLSX: return xlsxToHtml(bytes);
            case DOC : return docToHtml(bytes);
            case DOCX: return docxToHtml(bytes);
            default: return new byte[0];
        }
    }
    /**
     *
     * @param cell
     * @return
     */
    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        if (cell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        } else {
            return String.valueOf(cell.getStringCellValue());
        }
    }

    public static void main(String[] args) throws Exception {
//        File docxFile = new File("C:/Users/ext.houmeina/AppData/Local/Temp/b.docx");
//        AgreTemplateWordExportVo vo = new AgreTemplateWordExportVo();
//        LoginUserInfo loginUserInfo = new LoginUserInfo();
//        loginUserInfo.setAccountName("setAccountName");
//        AgreementInfo agreementInfo = new AgreementInfo();
//        agreementInfo.setPartACode("PartACode");
//        agreementInfo.setPartAName("setPartAName");
//        agreementInfo.setPartBCode("setPartBCode");
//        agreementInfo.setPartBName("setPartBName");
//        agreementInfo.setAgreementNo("setAgreementNo");
//        agreementInfo.setAgreementName("agreementName");
//        agreementInfo.setAcapitalTotalPrice(new BigDecimal(0));
//        agreementInfo.setTotalPrice(new BigDecimal(1));
//        agreementInfo.setExcludingTax(new BigDecimal(2));
//        agreementInfo.setAcapitalExcludingTax(new BigDecimal(3));
//        Date date = new Date();
//        agreementInfo.setCreated(date);
//        agreementInfo.setEndTime(date);
//        agreementInfo.setStartTime(date);
//        AgreementInfoPartyResultVo agreementInfoPartyA = new AgreementInfoPartyResultVo();
//        agreementInfoPartyA.setEnterpriseCode("setEnterpriseCode");
//        agreementInfoPartyA.setEnterpriseName("setEnterpriseName");
//        agreementInfoPartyA.setLegalRepresentative("setLegalRepresentative");
//        agreementInfoPartyA.setAddress("setAddress");
//        agreementInfoPartyA.setMobile("setMobile");
//        agreementInfoPartyA.setAccountBankBranchName("setAccountBankBranchName");
//        agreementInfoPartyA.setBankAccountName("setBankAccountName");
//        agreementInfoPartyA.setBankAccountNo("setBankAccountNo");
//        agreementInfoPartyA.setAgrePartyDesc("setAgrePartyDesc");
//        agreementInfoPartyA.setAgrePartyCode("setAgrePartyCode");
//        agreementInfoPartyA.setAgrePartyName("setAgrePartyName");
//        AgreementInfoPartyResultVo agreementInfoPartyB = new AgreementInfoPartyResultVo();
//        agreementInfoPartyB.setEnterpriseCode("setEnterpriseCode");
//        agreementInfoPartyB.setEnterpriseName("setEnterpriseName");
//        agreementInfoPartyB.setLegalRepresentative("setLegalRepresentative");
//        agreementInfoPartyB.setAddress("setAddress");
//        agreementInfoPartyB.setMobile("setMobile");
//        agreementInfoPartyB.setAccountBankBranchName("setAccountBankBranchName");
//        agreementInfoPartyB.setBankAccountName("setBankAccountName");
//        agreementInfoPartyB.setBankAccountNo("setBankAccountNo");
//        agreementInfoPartyB.setAgrePartyDesc("setAgrePartyDesc");
//        agreementInfoPartyB.setAgrePartyCode("setAgrePartyCode");
//        agreementInfoPartyB.setAgrePartyName("setAgrePartyName");
//        List<AgreementInfoPartyResultVo> partyList = Lists.newArrayList(agreementInfoPartyB,agreementInfoPartyA);
//        // 标的物
//        AgreSubMatterFtlVo agreSubMatterFltVo = new AgreSubMatterFtlVo();
//        agreSubMatterFltVo.setLineNum(1);
//        agreSubMatterFltVo.setMaterialCode("setMaterialCode");
//        agreSubMatterFltVo.setName("setName");
//        agreSubMatterFltVo.setModel("setModel");
//        agreSubMatterFltVo.setUnit("setUnit");
//        agreSubMatterFltVo.setCount(2);
//        agreSubMatterFltVo.setTaxRate(new BigDecimal(4));
//        agreSubMatterFltVo.setUnitPrice(new BigDecimal(5));
//        agreSubMatterFltVo.setMaterialCategory("setMaterialCatejory");
//        agreSubMatterFltVo.setUnitCode("setUnitCode");
//        agreSubMatterFltVo.setTaxType("setTaxType");
//        agreSubMatterFltVo.setCurrencyCode("setCurrenryCode");
//        agreSubMatterFltVo.setCurrencyName("setCurrenryName");
//        agreSubMatterFltVo.setNoTaxLinePrice(new BigDecimal(6));
//        agreSubMatterFltVo.setTaxLinePrice(new BigDecimal(7));
//        agreSubMatterFltVo.setDeliveryDate(new Date());
//        List<AgreSubMatterFtlVo> matterFltVoList = Lists.newArrayList(agreSubMatterFltVo);
//        List<AgreementInfoClauseResultVo> agreementInfoClauseList = new LinkedList<>();
//        AgreementInfoClauseResultVo clause1 = new AgreementInfoClauseResultVo();
//        clause1.setAgreClauseName("setAgreClauseName1");
//        clause1.setAgreClauseContent("setAgreClauseContent1");
//        AgreementInfoClauseResultVo clause2 = new AgreementInfoClauseResultVo();
//        clause2.setAgreClauseName("setAgreClauseName2");
//        clause2.setAgreClauseContent("setAgreClauseContent2");
//        AgreementInfoClauseResultVo clause3 = new AgreementInfoClauseResultVo();
//        clause3.setAgreClauseName("setAgreClauseName3");
//        clause3.setAgreClauseContent("setAgreClauseContent3");
//        AgreementInfoClauseResultVo clause4 = new AgreementInfoClauseResultVo();
//        clause4.setAgreClauseName("setAgreClauseName4");
//        clause4.setAgreClauseContent("setAgreClauseContent4");
//        AgreementInfoClauseResultVo clause5 = new AgreementInfoClauseResultVo();
//        clause5.setAgreClauseName("setAgreClauseName5");
//        clause5.setAgreClauseContent("setAgreClauseContent5");
//        AgreementInfoClauseResultVo clause6 = new AgreementInfoClauseResultVo();
//        clause6.setAgreClauseName("setAgreClauseName6");
//        clause6.setAgreClauseContent("setAgreClauseContent6");
//
//        agreementInfoClauseList.add(clause1);
//        agreementInfoClauseList.add(clause2);
//        agreementInfoClauseList.add(clause3);
//        agreementInfoClauseList.add(clause4);
//        agreementInfoClauseList.add(clause5);
//        agreementInfoClauseList.add(clause6);
//        vo.setLoginUserInfo(loginUserInfo);
//        vo.setAgreementInfo(agreementInfo);
//        vo.setAgreementInfoClauseList(agreementInfoClauseList);
//        vo.setAgreementInfoPartyList(partyList);
//        vo.setAgreSubMatterFltVoList(matterFltVoList);
//        File gbk = exportWord(docxFile, "GBK", vo);
//        System.out.println(gbk.getPath());
//        Map<String, String> data = new HashMap<>();
//        data.put("test", "my test");
//        File wordFile =new File("/Users/maenfang1/vim1.doc");
//        render(wordFile, "GBK", data);
//        doc2Pdf(new File("/Users/maenfang1/vim.doc"));
//        docx2Pdf(new File("/Users/maenfang1/vim.docx"));
//        System.out.println(readDocText(new File("/Users/maenfang1/vim_test.doc")));
//        System.out.println(readDocxText(new File("/Users/maenfang1/vim.docx")));
    }
}
