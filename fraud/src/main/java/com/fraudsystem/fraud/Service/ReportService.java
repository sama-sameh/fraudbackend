package com.fraudsystem.fraud.Service;

import com.fraudsystem.fraud.DTO.AlertDTO;
import com.fraudsystem.fraud.Entity.Alert;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ReportService {

    public byte[] generatePdf(List<Alert> alerts) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate()); // Use landscape for better width
        PdfWriter.getInstance(document, baos);
        document.open();

        // Title
        Paragraph title = new Paragraph("Fraud Alert Report", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph datePara = new Paragraph("Generated On: " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        datePara.setAlignment(Element.ALIGN_RIGHT);
        document.add(datePara);

        document.add(Chunk.NEWLINE);

        // Set table with 7 columns
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100); // Take full width
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Set custom column widths
        float[] columnWidths = {1.5f, 1.5f, 3f, 2.5f, 1.5f, 2.5f, 1.5f};
        table.setWidths(columnWidths);

        // Header styling
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Stream.of("Suspect ID", "Customer ID", "Customer Name", "Rule", "Priority", "Date", "Alert Age")
                .forEach(header -> {
                    PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                    cell.setBackgroundColor(Color.LIGHT_GRAY);
                    cell.setPadding(5);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                });

        // Date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        // Data rows
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        for (Alert alert : alerts) {
            table.addCell(new PdfPCell(new Phrase(String.valueOf(alert.getSuspectId()), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(alert.getCustomer().getId()), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(alert.getCustomer().getName(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(alert.getRule().getRule_name(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(alert.getRule().getPriority()), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(dateFormat.format(alert.getDate()), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(alert.getAlertAge()), bodyFont)));
        }

        document.add(table);
        document.close();

        return baos.toByteArray();
    }

}
