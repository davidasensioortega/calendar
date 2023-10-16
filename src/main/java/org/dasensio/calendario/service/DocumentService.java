package org.dasensio.calendario.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.dasensio.calendario.domain.Client;
import org.dasensio.calendario.util.CalendarException;

public class DocumentService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("EEEE', 'dd' de 'MMMM", new Locale("es", "es"));
    private static final DateTimeFormatter MONTH_FORMAT = DateTimeFormatter.ofPattern("MMMM''yy", new Locale("es", "es"));

    public ByteArrayOutputStream generateDocument(final Map<LocalDate, Collection<Client>> calendar) {
        try {
            XWPFDocument document = new XWPFDocument();

            this.insertTitle(document, calendar);

            this.addSeparator(document);

            final Integer[] id = new Integer[]{1};

            calendar.entrySet().stream().filter(entry -> !LocalDate.MAX.equals(entry.getKey())).sorted(Entry.comparingByKey())
                .forEach(entry -> entry.getValue().forEach(client -> this.generateTable(document, id, entry.getKey(), client)));

            this.addSeparator(document);

            calendar.entrySet().stream().filter(entry -> LocalDate.MAX.equals(entry.getKey()))
                .forEach(entry -> this.printNotGrouped(document, entry.getValue()));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.write(baos);
            baos.close();
            document.close();

            return baos;
        } catch (IOException exception) {
            throw new CalendarException(exception);
        }
    }

    private void insertTitle(XWPFDocument document, Map<LocalDate, Collection<Client>> calendar) {
        XWPFTable titleTable = document.createTable();
        titleTable.setWidth("100%");

        XWPFParagraph title = titleTable.getRow(0).getCell(0).getParagraphs().get(0);
        title.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun titleRun = title.createRun();
        titleRun.setText(getTitle(calendar.keySet()));
        titleRun.setBold(true);
        titleRun.setFontSize(20);
    }

    private void addSeparator(XWPFDocument document) {
        XWPFParagraph p = document.createParagraph();
        XWPFRun pRun = p.createRun();
        pRun.setText("");
    }

    private void generateTable(XWPFDocument document, Integer[] id, LocalDate date, Client client) {
        XWPFTable table = document.createTable();
        table.setWidth("100%");
        table.setCellMargins(0, 50, 0, 0);

        XWPFTableRow row = table.getRow(0);
        XWPFTableCell cell;

        cell = row.getCell(0);
        cell.setWidth("5%");
        this.addFormattedText(cell, id[0].toString(), ParagraphAlignment.CENTER);
        id[0] = id[0] + 1;

        cell = row.addNewTableCell();
        cell.setWidth("35%");
        this.addFormattedText(cell, DATE_FORMAT.format(date), ParagraphAlignment.LEFT);

        cell = row.addNewTableCell();
        cell.setWidth("10%");
        this.addFormattedText(cell, String.valueOf(date.getYear()), ParagraphAlignment.CENTER);

        cell = row.addNewTableCell();
        cell.setWidth("5%");
        this.addFormattedText(cell, client.getId().toString(), ParagraphAlignment.CENTER);

        cell = row.addNewTableCell();
        cell.setWidth("45%");
        this.addFormattedText(cell, client.getName(), ParagraphAlignment.LEFT);

        this.addSeparator(document);
    }

    private void addFormattedText(XWPFTableCell cell, String text, ParagraphAlignment alignment) {
        XWPFParagraph p = cell.getParagraphs().get(0);
        p.setAlignment(alignment);

        XWPFRun pRun = p.createRun();
        pRun.setText(text);
        pRun.setBold(true);
        pRun.setFontSize(12);
    }

    private void printNotGrouped(XWPFDocument document, Collection<Client> clients) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.BOTH);
        XWPFRun paragraphRun = paragraph.createRun();
        paragraphRun.setText("RESERVAS: " + clients.stream().map(Client::getName).collect(Collectors.joining(", ")));
    }

    private String getTitle(final Collection<LocalDate> dates) {
        LocalDate minDate = dates.stream().min(LocalDate::compareTo).orElse(LocalDate.MIN);
        LocalDate maxDate = dates.stream().filter(d -> !LocalDate.MAX.equals(d)).max(LocalDate::compareTo).orElse(LocalDate.MAX);
        return "CALENDARIO " + MONTH_FORMAT.format(minDate) + " - " + MONTH_FORMAT.format(maxDate);
    }

}
