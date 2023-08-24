package ru.kurochkin.csv;

import java.io.*;

public class CsvToHtmlConverter {

    public static final String htmlTagTableBegin = "<table border=\"1px\">";
    public static final String htmlTagTableEnd = "</table>";
    public static final String htmlTagTableRowBegin = "<tr>";
    public static final String htmlTagTableRowEnd = "</tr>";
    public static final String htmlTagTableCellBegin = "<td>";
    public static final String htmlTagTableCellEnd = "</td>";
    public static final String htmlTagLineSeparator = "<br/>";
    public static final String tagOffset = "    ";

    public static void writeHtmlCharacter(BufferedWriter writer, int character) throws IOException {
        switch (character) {
            case '<':
                writer.write("&lt;");
                break;
            case '>':
                writer.write("&gt;");
                break;
            case '&':
                writer.write("&amp;");
                break;
            default:
                writer.write(character);
        }
    }

    public static void writeHtmlBegin(BufferedWriter writer) throws IOException {
        writer.write("<!DOCTYPE html>" + System.lineSeparator() + "<html>" + System.lineSeparator() + "<head>" +
                System.lineSeparator() + tagOffset + "<meta charset=\"UTF-8\">" + System.lineSeparator() + "</head>" +
                System.lineSeparator() + "<body>" + System.lineSeparator());
    }

    public static void writeHtmlEnd(BufferedWriter writer) throws IOException {
        writer.write("</body>" + System.lineSeparator() + "</html>");
    }

    public static void convertCsvToHtml(String inputFileName, String outputFileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            writeHtmlBegin(writer);

            writer.write(tagOffset + htmlTagTableBegin);

            boolean isCellBegin = false;
            boolean isRowBegin = false;
            boolean isQuotedCell = false;
            boolean isQuotePreviousCharacter = false;

            String lineSeparator = System.lineSeparator();
            String rowOffset = tagOffset.repeat(2);
            String cellOffset = tagOffset.repeat(3);

            String line;

            while ((line = reader.readLine()) != null) {
                char[] lineCharacters = line.toCharArray();

                if (lineCharacters.length == 0) {
                    continue;
                }

                if ((isQuotePreviousCharacter || !isQuotedCell) && isRowBegin) {
                    writer.write(htmlTagTableCellEnd);
                    writer.write(lineSeparator + rowOffset + htmlTagTableRowEnd);

                    isCellBegin = false;
                    isRowBegin = false;
                    isQuotePreviousCharacter = false;
                    isQuotedCell = false;
                } else if (isRowBegin) {
                    writer.write(htmlTagLineSeparator);
                }

                if (!isRowBegin) {
                    writer.write(lineSeparator + rowOffset + htmlTagTableRowBegin);
                    writer.write(lineSeparator + cellOffset + htmlTagTableCellBegin);

                    isRowBegin = true;
                }

                int i = 0;

                while (i < lineCharacters.length) {
                    if (lineCharacters[i] == '"') {
                        if (isCellBegin) {
                            if (isQuotePreviousCharacter) {
                                writeHtmlCharacter(writer, lineCharacters[i]);

                                isQuotePreviousCharacter = false;
                            } else {
                                isQuotePreviousCharacter = true;
                            }
                        } else {
                            isQuotedCell = true;
                            isCellBegin = true;
                        }
                    } else if (lineCharacters[i] == ',') {
                        if (isQuotePreviousCharacter || !isQuotedCell) {
                            writer.write(htmlTagTableCellEnd);
                            writer.write(lineSeparator + cellOffset + htmlTagTableCellBegin);

                            isCellBegin = false;
                            isQuotedCell = false;
                        } else {
                            writeHtmlCharacter(writer, lineCharacters[i]);
                        }

                        isQuotePreviousCharacter = false;
                    } else {
                        writeHtmlCharacter(writer, lineCharacters[i]);

                        isCellBegin = true;
                    }

                    i++;
                }
            }

            if (isCellBegin) {
                writer.write(htmlTagTableCellEnd);
            }

            if (isRowBegin) {
                writer.write(lineSeparator + rowOffset + htmlTagTableRowEnd);
            }

            writer.write(lineSeparator + tagOffset + htmlTagTableEnd + lineSeparator);

            writeHtmlEnd(writer);
        } catch (IOException exception) {
            throw exception;
        }
    }

    public static void main(String[] args) {
        String inputFileName = null;
        String outputFileName = null;

        for (int i = 0; i < args.length - 1; i += 2) {
            if (args[i].equals("-input")) {
                inputFileName = args[i + 1];
            }

            if (args[i].equals("-output")) {
                outputFileName = args[i + 1];
            }
        }

        StringBuilder errorMessage = new StringBuilder();

        if (inputFileName == null) {
            errorMessage.append("имя файла CSV");
        }

        if (outputFileName == null) {
            if (errorMessage.length() > 0) {
                errorMessage.append(", ");
            }

            errorMessage.append("имя файла HTML");
        }

        if (errorMessage.length() > 0) {
            errorMessage.insert(0, "Не заданы обязательные параметры: ");

            System.out.println(errorMessage);
        } else {
            try {
                convertCsvToHtml(inputFileName, outputFileName);
            } catch (FileNotFoundException exception) {
                System.out.println("Файл " + inputFileName + " не найден");
            } catch (IOException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }
}
