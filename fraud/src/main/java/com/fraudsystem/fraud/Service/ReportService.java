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

        Paragraph title = new Paragraph("Fraud Alert Report", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph datePara = new Paragraph("Generated On: " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        datePara.setAlignment(Element.ALIGN_RIGHT);
        document.add(datePara);

        document.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100); // Take full width
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        float[] columnWidths = {1.5f, 1.5f, 3f, 2.5f, 1.5f, 2.5f, 1.5f};
        table.setWidths(columnWidths);

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Stream.of("Suspect No", "Date","Alert Age", "Customer ID", "Customer Name", "Rule", "Priority")
                .forEach(header -> {
                    PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                    cell.setBackgroundColor(Color.LIGHT_GRAY);
                    cell.setPadding(5);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                });

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        for (Alert alert : alerts) {
            table.addCell(createCenteredCell(String.valueOf(alert.getSuspectId()), bodyFont));
            table.addCell(createCenteredCell(dateFormat.format(alert.getDate()), bodyFont));
            table.addCell(createCenteredCell(String.valueOf(alert.getAlertAge()), bodyFont));
            table.addCell(createCenteredCell(String.valueOf(alert.getCustomer().getId()), bodyFont));
            table.addCell(createCenteredCell(alert.getCustomer().getName(), bodyFont));
            table.addCell(createCenteredCell(alert.getRule().getRule_name(), bodyFont));
            table.addCell(createCenteredCell(String.valueOf(alert.getRule().getPriority()), bodyFont));
        }

        document.add(table);
        document.close();

        return baos.toByteArray();
    }
    private PdfPCell createCenteredCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        return cell;
    }

}
