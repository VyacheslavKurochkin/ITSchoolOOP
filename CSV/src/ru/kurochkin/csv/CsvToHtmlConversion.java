package ru.kurochkin.csv;

import java.io.*;
import java.util.Scanner;

public class CsvToHtmlConversion {
    public static void writeHtmlRow(PrintWriter writer, StringBuilder htmlRow) {
        writer.println(htmlRow.insert(0, "<tr>").append("</tr>"));
        htmlRow.delete(0, htmlRow.length());
    }

    public static void addHtmlCell(StringBuilder htmlRow, StringBuilder htmlCell) {
        htmlRow.append("<td>").append(htmlCell.toString()).append("</td>");
        htmlCell.delete(0, htmlCell.length());
    }

    public static void addHtmlCharacter(StringBuilder htmlCell, char character) {
        switch (character) {
            case '<':
                htmlCell.append("&lt;");
                break;
            case '>':
                htmlCell.append("&gt;");
                break;
            case '&':
                htmlCell.append("&amp;");
                break;
            default:
                htmlCell.append(character);
        }
    }

    public static void convertCsvToHtml(String inputFileName, String outputFileName) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new FileInputStream(inputFileName));
             PrintWriter writer = new PrintWriter(outputFileName)) {

            StringBuilder htmlRow = new StringBuilder();
            StringBuilder htmlCell = new StringBuilder();

            writer.println("<table>");

            boolean isCellBegin = false;
            boolean isQuotedCell = false;
            boolean isQuotePreviousCharacter = false;

            while (scanner.hasNextLine()) {
                String readLine = scanner.nextLine();
                int readLineLength = readLine.length();
                int i = 0;

                while (i < readLineLength) {
                    char currentCharacter = readLine.charAt(i);

                    if (currentCharacter == '"') {
                        if (isCellBegin) {
                            if (isQuotePreviousCharacter) {
                                htmlCell.append(currentCharacter);
                                isQuotePreviousCharacter = false;
                            } else {
                                isQuotePreviousCharacter = true;
                            }
                        } else {
                            isQuotedCell = true;
                            isCellBegin = true;
                        }
                    } else if (currentCharacter == ',') {
                        if (isQuotePreviousCharacter || !isQuotedCell) {
                            addHtmlCell(htmlRow, htmlCell);
                            isCellBegin = false;
                            isQuotedCell = false;
                        } else {
                            addHtmlCharacter(htmlCell, currentCharacter);
                        }

                        isQuotePreviousCharacter = false;
                    } else {
                        addHtmlCharacter(htmlCell, currentCharacter);
                        isCellBegin = true;
                    }

                    i++;
                }

                if (isQuotePreviousCharacter || !isQuotedCell) {
                    addHtmlCell(htmlRow, htmlCell);
                    writeHtmlRow(writer, htmlRow);
                    isCellBegin = false;
                } else {
                    htmlCell.append("<br/>");
                }
            }

            writer.println("</table>");
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        convertCsvToHtml("CSV\\input.csv", "CSV\\output.html");
    }
}
