package com.ccc.jo.service;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class QRCodeService {
    public byte[] generateQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }
    public byte[] generateQRCodePDF(String text, int width, int height) throws WriterException, DocumentException, IOException {
        byte[] pngImageData = generateQRCodeImage(text, width, height);
        Document document = new Document();
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, pdfOutputStream);
        document.open();
        Image qrCodeImage = Image.getInstance(pngImageData);
        document.add(qrCodeImage);
        document.close();
        return pdfOutputStream.toByteArray();
    }
}