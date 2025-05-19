package service.electionCommissions;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.util.List;
import config.databaseConnection;

import java.io.FileOutputStream;
import java.sql.*;

public class electionResultsExportService {

    public void exportAllResultsToPdf(String filePath) {
        String electionsQuery = "SELECT id, name FROM elections";
        String candidateNameQuery = "SELECT full_name FROM candidates WHERE login = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement electionsStmt = conn.prepareStatement(electionsQuery);
             ResultSet electionsRs = electionsStmt.executeQuery()) {


            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            BaseFont baseFont = BaseFont.createFont("c:/windows/fonts/times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font titleFont = new Font(baseFont, 18, Font.BOLD);
            Font tableHeader = new Font(baseFont, 12, Font.BOLD);
            Font cellFont = new Font(baseFont, 12);

            Paragraph title = new Paragraph("Таблица результатов голосования", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            while (electionsRs.next()) {
                int electionId = electionsRs.getInt("id");
                String electionName = electionsRs.getString("name");


                Paragraph electionTitle = new Paragraph("Выборы: " + electionName, tableHeader);
                electionTitle.setSpacingBefore(15);
                electionTitle.setSpacingAfter(10);
                document.add(electionTitle);


                PdfPTable table = new PdfPTable(2);
                table.setWidthPercentage(100);
                table.addCell(new PdfPCell(new Phrase("Имя кандидата", tableHeader)));
                table.addCell(new PdfPCell(new Phrase("Количество голосов", tableHeader)));

                String votesQuery = String.format("SELECT login, votes FROM elections_%d ORDER BY votes DESC", electionId);

                try (PreparedStatement votesStmt = conn.prepareStatement(votesQuery);
                     ResultSet votesRs = votesStmt.executeQuery();
                     PreparedStatement candidateNameStmt = conn.prepareStatement(candidateNameQuery)) {

                    while (votesRs.next()) {
                        String candidateLogin = votesRs.getString("login");
                        int votes = votesRs.getInt("votes");


                        candidateNameStmt.setString(1, candidateLogin);
                        String fullName = candidateLogin;

                        try (ResultSet nameRs = candidateNameStmt.executeQuery()) {
                            if (nameRs.next()) {
                                fullName = nameRs.getString("full_name");
                            }
                        }


                        table.addCell(new PdfPCell(new Phrase(fullName, cellFont)));
                        table.addCell(new PdfPCell(new Phrase(String.valueOf(votes), cellFont)));
                    }
                }

                document.add(table);
            }

            document.close();
            System.out.println("PDF успешно создан: " + filePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void exportSelectedResultsToPdf(String filePath, List<String> electionIds, boolean saveInOneFile) {
        try (Connection conn = databaseConnection.getConnection()) {

            Document document = null;
            PdfWriter writer = null;

            if (saveInOneFile) {
                document = new Document();
                writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();
            }

            BaseFont baseFont = BaseFont.createFont("c:/windows/fonts/times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font titleFont = new Font(baseFont, 16, Font.BOLD);
            Font tableHeader = new Font(baseFont, 12, Font.BOLD);
            Font cellFont = new Font(baseFont, 12);

            for (String electionId : electionIds) {
                String tableName = "elections_" + electionId;


                String electionName = "";
                try (PreparedStatement ps = conn.prepareStatement("SELECT name FROM elections WHERE id = ?")) {
                    ps.setInt(1, Integer.parseInt(electionId));
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            electionName = rs.getString("name");
                        }
                    }
                }


                String resultQuery = String.format("SELECT login, votes FROM %s", tableName);
                try (PreparedStatement stmt = conn.prepareStatement(resultQuery);
                     ResultSet rs = stmt.executeQuery()) {

                    if (!saveInOneFile) {
                        document = new Document();
                        writer = PdfWriter.getInstance(document, new FileOutputStream(filePath.replace(".pdf", "_" + electionId + ".pdf")));
                        document.open();
                    }


                    Paragraph title = new Paragraph("Результаты голосования: " + electionName, titleFont);
                    title.setAlignment(Element.ALIGN_CENTER);
                    title.setSpacingAfter(20);
                    document.add(title);

                    // Таблица результатов
                    PdfPTable table = new PdfPTable(3);
                    table.setWidthPercentage(100);
                    table.addCell(new PdfPCell(new Phrase("ФИО кандидата", tableHeader)));
                    table.addCell(new PdfPCell(new Phrase("Логин кандидата", tableHeader)));
                    table.addCell(new PdfPCell(new Phrase("Количество голосов", tableHeader)));

                    PreparedStatement getName = conn.prepareStatement("SELECT full_name FROM candidates WHERE login = ?");

                    while (rs.next()) {
                        String login = rs.getString("login");
                        int votes = rs.getInt("votes");

                        String fullName = "";
                        getName.setString(1, login);
                        try (ResultSet nameRS = getName.executeQuery()) {
                            if (nameRS.next()) {
                                fullName = nameRS.getString("full_name");
                            }
                        }

                        table.addCell(new PdfPCell(new Phrase(fullName, cellFont)));
                        table.addCell(new PdfPCell(new Phrase(login, cellFont)));
                        table.addCell(new PdfPCell(new Phrase(String.valueOf(votes), cellFont)));
                    }

                    document.add(table);

                    if (!saveInOneFile) {
                        document.close();
                        writer.close();
                    }
                }
            }

            if (saveInOneFile) {
                document.close();
                writer.close();
            }

            System.out.println("Экспорт завершен!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
