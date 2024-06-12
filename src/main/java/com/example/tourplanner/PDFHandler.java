package com.example.tourplanner;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PDFHandler {
    public void createPDF(Tour tour, List<TourLog> tourLogs, String dest) {

    };
    public Tour TourfromPDF(){
        return new Tour();
    };//braucht noch arg aber ka wie man pdf reintut
    //public ObservableList<TourLog> LogsfromPDF(){}; //to do
}
