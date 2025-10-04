package com.mindhealth.mindhealth.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;

public class PdfGenerator {
    public byte[] generateSimpleTicket(String title, String attendee, String qrCode) {
        try (PDDocument doc = new PDDocument(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            doc.addPage(page);
            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 18);
                cs.newLineAtOffset(50, 700);
                cs.showText("Ticket: " + title);
                cs.endText();

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 12);
                cs.newLineAtOffset(50, 670);
                cs.showText("Attendee: " + attendee);
                cs.endText();

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 10);
                cs.newLineAtOffset(50, 640);
                cs.showText("QR: " + qrCode);
                cs.endText();
            }
            doc.save(baos);
            return baos.toByteArray();
        } catch (Exception e) {
            return new byte[0];
        }
    }
}

