package com.example.tourplanner;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import javafx.collections.ObservableList;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.listener.SimpleTextExtractionStrategy;
import java.time.LocalDate;
import java.util.regex.Pattern;
import java.util.regex.Matcher;



public class PDFHandler {
    public void createPDF(Tour tour, List<TourLogs> tourLogs, String dest) {
        try {
            PdfWriter pdfWriter = new PdfWriter(dest);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);
            Paragraph headline = new Paragraph("Tour Report")
                    .setFontSize(18)
                    .setBold()
                    .setMarginBottom(20);
            document.add(headline);
            document.add(new Paragraph("Name: " + tour.getName()));
            document.add(new Paragraph("Description: " + tour.getDescription()));
            document.add(new Paragraph("From: " + tour.getFromLocation()));
            document.add(new Paragraph("To: " + tour.getToLocation()));
            document.add(new Paragraph("Transport Type: " + tour.getTransportType()));
            document.add(new Paragraph("Distance: " + tour.getDistance()));
            document.add(new Paragraph("Estimated Time: " + String.format("%.0f",tour.getEstimatedTime())));
            document.add(new Paragraph("Popularity: " +  String.format("%.0f",tour.getPopularity())));
            document.add(new Paragraph("Child Friendliness: " +  String.format("%.1f",tour.getChildFriendliness())));
            document.add(new Paragraph("\n"));

            // Add tour logs
            document.add(new Paragraph("Tour Logs"));

            // Create table for logs
            Table table = new Table(UnitValue.createPercentArray(new float[]{2, 2, 2, 1, 1, 1, 1}));
            table.setWidth(UnitValue.createPercentValue(100));
            table.addHeaderCell("Date");
            table.addHeaderCell("Time");
            table.addHeaderCell("Comment");
            table.addHeaderCell("Difficulty");
            table.addHeaderCell("Total Distance");
            table.addHeaderCell("Total Time");
            table.addHeaderCell("Rating");

            for (TourLogs log : tourLogs) {
                table.addCell(log.getDate().toString());
                table.addCell(String.format("%.0f",log.getTime()));
                table.addCell(log.getComment());
                table.addCell(String.format("%.0f",log.getDifficulty()));
                table.addCell(String.format("%.1f",log.getTotalDistance()));
                table.addCell(String.format("%.0f",log.getTotalTime()));
                table.addCell( String.format("%.0f",log.getRating()));
            }

            document.add(table);

            // Close document
            document.close();

            System.out.println("PDF created successfully");
        }
        catch(Exception e){
            e.printStackTrace();
        }

    };
    public static Tour readPDF(String filePath) {
        Tour tour = null;
        List<TourLogs> tourLogs = new ArrayList<>();

        try (PdfDocument pdfDoc = new PdfDocument(new PdfReader(filePath))) {
            StringBuilder text = new StringBuilder();

            // Extract text from all pages
            for (int page = 1; page <= pdfDoc.getNumberOfPages(); page++) {
                text.append(PdfTextExtractor.getTextFromPage(pdfDoc.getPage(page), new SimpleTextExtractionStrategy()));
            }

            String[] lines = text.toString().split("\n");

            // Extract tour information
            String tourName = extractValue(lines, "Name:");
            String description = extractValue(lines, "Description:");
            String from = extractValue(lines, "From:");
            String to = extractValue(lines, "To:");
            String transportType = extractValue(lines, "Transport Type:");
            float distance = extractFloatValue(lines, "Distance:");
            float estimatedTime = extractFloatValue(lines, "Estimated Time:");

            tour = new Tour(tourName, description);
            tour.setFromLocation(from);
            tour.setToLocation(to);
            tour.setTransportType(transportType);
            tour.setDistance(distance);
            tour.setEstimatedTime(estimatedTime);

            TourManager.getInstance().addTour(tour);
            /*beim extracten aus der tabelle mit nem stream isses abissl kacke, weil wenn das comment beim PDF schreiben
            zu lang fuer das tabellenfeld is, hauts da ein \n hin
            hat man zb im lines Array (jede Zeile neuer entry):
            2024-06-13 0 no comment,
            außer halt dieses
            ur lange comment,
            das fix headaches
            bereiten wird, tut
            mir voll leid lel
            3 0.0 0 5
            2024-06-13 0 no comment,
            außer halt dieses
            ur lange comment,
            das fix headaches
            bereiten wird, tut
            mir voll leid lel
            3 0.0 0 5
            isses unmöglich festzustellen, ob das 1 oder 2 logs waren, weil das 3 0.0 0 5 könnte ein lineend (mit regex) anzeigen, könnt aber auch einfach
            teil vom comment sein... wir umgehn das, indem wir im comment field bei der input validation einfach keine zahlen zulassen
             */
            //Schritt 1: mach alles in einen String
            StringBuilder everything = new StringBuilder();
            for (int i = 17; i < lines.length; i++) {
                everything.append(lines[i] + " ");
            }
            String concatenatedText = everything.toString();
            concatenatedText = concatenatedText.replaceAll("\\s{2,}", " ");//getrs rid of double blanks from within the comment
            String regex = "(\\d{4}-\\d{2}-\\d{2})\\s+(\\d+)\\s+([^\\d]+?)\\s+(\\d+)\\s+(\\d+\\.\\d+)\\s+(\\d+)\\s+(\\d+)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(concatenatedText);

            List<String> matches = new ArrayList<>();

            while (matcher.find()) {
                matches.add(matcher.group());
            }

            String[] matchedArray = matches.toArray(new String[0]);

            // Process each matched substring
            for (String match : matchedArray) {
                // Extract the fields from the matched substring
                Matcher fieldMatcher = pattern.matcher(match);
                if (fieldMatcher.matches()) {
                    String date = fieldMatcher.group(1);
                    float time = Float.parseFloat(fieldMatcher.group(2));
                    String comment = fieldMatcher.group(3);
                    System.out.println(comment);
                    float difficulty = Float.parseFloat(fieldMatcher.group(4));
                    float totalDistance = Float.parseFloat(fieldMatcher.group(5));
                    float totalTime = Float.parseFloat(fieldMatcher.group(6));
                    float rating = Float.parseFloat(fieldMatcher.group(7));

                    // Create a new TourLog and set its fields
                    TourLogs log = new TourLogs();
                    log.setDate(LocalDate.parse(date));
                    log.setTime(time);
                    log.setComment(comment);
                    log.setDifficulty(difficulty);
                    log.setTotalDistance(totalDistance);
                    log.setTotalTime(totalTime);
                    log.setRating(rating);
                    log.setId(TourLogManager.getInstance().getNewLogID());
                    log.setTour(tour);
                    log.setTourId(tour.getId());

                    tourLogs.add(log);
                }
            }
            }catch (IOException e) {
            e.printStackTrace();
        }

        TourLogManager tourLogManager = TourLogManager.getInstance();
        for (TourLogs log : tourLogs) {
            tourLogManager.addTourLog(log);
        }

        return tour;
    }

    private static String extractValue(String[] lines, String prefix) {
        for (String line : lines) {
            if (line.startsWith(prefix)) {
                return line.substring(prefix.length()).trim();
            }
        }
        return "";
    }

    private static float extractFloatValue(String[] lines, String prefix) {
        for (String line : lines) {
            if (line.startsWith(prefix)) {
                String value = line.substring(prefix.length()).trim().split(" ")[0];
                return Float.parseFloat(value);
            }
        }
        return 0.0f;
    };//braucht noch arg aber ka wie man pdf reintut
    //public ObservableList<TourLog> LogsfromPDF(){}; //to do
}
