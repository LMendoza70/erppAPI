package com.gisnet.erpp.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorkerHelper;
 
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class HtmlToPDF {

	public static final String DEST = "C:/Users/USUARIO/Desktop/Test.pdf";
 
    public static void createPdf(String destino,String stringHtml) throws IOException, DocumentException {
        File file = new File(destino);
        file.getParentFile().mkdirs();
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(destino));
        document.open();
        StringBuilder sb = new StringBuilder();
        sb.append(stringHtml);
 
        PdfPTable table = new PdfPTable(1);
        PdfPCell cell = new PdfPCell();
        ElementList list = XMLWorkerHelper.parseToElementList(sb.toString(), null);
        for (Element element : list) {
            cell.setBorder(0);
            cell.addElement(element);
        }
        table.addCell(cell);
        document.add(table);
 
        document.close();
    }

}
