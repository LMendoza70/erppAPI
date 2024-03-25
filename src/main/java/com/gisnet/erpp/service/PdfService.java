package com.gisnet.erpp.service;

import java.io.ByteArrayOutputStream;

import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImage;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfIndirectObject;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.itextpdf.text.pdf.PdfStamper;

@Service
public class PdfService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Agrega una imágen a una pagina nueva del PDF
     * @param pdfBytes PDF En forma de bytes[]
     * @param imgBytes Imagen en forma de bytes []
     * @return PDF con la imagen incrustada, en forma de bytes
     * @throws Exception
     */
    public byte[] appendImage(byte[] pdfBytes, byte[] imgBytes) throws Exception {

        try {

            /* ByteStream de salida */
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            /* ByteReader para iterpretar el PDF  */
            PdfReader reader = new PdfReader(pdfBytes);

            /* Stamper para modificar el contenido del PDF   (Análogo al PdfWriter)*/
            PdfStamper stamper = new PdfStamper(reader, byteArrayOutputStream);

            Image    image  = Image.getInstance(imgBytes);
            PdfImage stream = new PdfImage(image, "", null);

            stamper.insertPage(reader.getNumberOfPages() + 1, reader.getPageSizeWithRotation(1));

            stream.put(new PdfName("ITXT_6545623134"), new PdfName("654565/465231"));
            PdfIndirectObject ref = stamper.getWriter().addToBody(stream);
            image.setDirectReference(ref.getIndirectReference());
            image.setAbsolutePosition(0, 0);

            PdfContentByte over = stamper.getOverContent(reader.getNumberOfPages());
            over.addImage(image);

            stamper.close();
            reader.close();

            return byteArrayOutputStream.toByteArray();

        } catch (Exception except) {
            except.printStackTrace();
            throw new Exception(except);
        }

    }

    /**
     * concatena un PDF en una pagina nueva del PDF original
     * @param pdfBytes PDF En forma de bytes[]
     * @param pdfBytes2 PDF En forma de bytes[]
     * @return PDF con concatenado, en forma de bytes
     * @throws Exception
     */
    public byte[] appendPDF(byte[] pdfBytes, byte[] pdfBytes2) throws Exception {

        /* ByteStream de salida */
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();


        try {
            Document document = new Document();
            PdfCopy  copy     = new PdfSmartCopy(document, byteArrayOutputStream);


            PdfReader reader1 = new PdfReader(pdfBytes);
            PdfReader reader2 = new PdfReader(pdfBytes2);
            PdfReader.unethicalreading = true;

            int pageCount   = 0;
            int currentPage = 0;

            PdfImportedPage tmpPage = null;

            document.open();

            /* Recorrer 1er PDF*/
            pageCount = reader1.getNumberOfPages();

            while (currentPage < pageCount) {
                currentPage += 1;
                tmpPage = copy.getImportedPage(reader1, currentPage);
                copy.addPage(tmpPage);
            }

            /* Recorrer 2o PDF*/
            pageCount = reader2.getNumberOfPages();
            currentPage = 0;

            while (currentPage < pageCount) {
                currentPage += 1;
                tmpPage = copy.getImportedPage(reader2, currentPage);
                copy.addPage(tmpPage);
            }

            document.close();

            return byteArrayOutputStream.toByteArray();

        } catch (Exception except) {
            except.printStackTrace();
            throw new Exception(except);
        }
    }

}
