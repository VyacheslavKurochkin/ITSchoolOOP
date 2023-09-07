package ru.kurochkin.csv;

import java.io.*;

public class CsvToHtmlConverter {
    public static final String HTML_TAG_TABLE_BEGIN = "<table border=\"1px\">";
    public static final String HTML_TAG_TABLE_END = "</table>";
    public static final String HTML_TAG_TABLE_ROW_BEGIN = "<tr>";
    public static final String HTML_TAG_TABLE_ROW_END = "</tr>";
    public static final String HTML_TAG_TABLE_CELL_BEGIN = "<td>";
    public static final String HTML_TAG_TABLE_CELL_END = "</td>";
    public static final String HTML_TAG_LINE_SEPARATOR = "<br/>";
    public static final String TAG_OFFSET = "\t";

    public static void writeHtmlCharacter(BufferedWriter writer, int character) throws IOException {
        switch (character) {
            case '<' -> writer.write("&lt;");
            case '>' -> writer.write("&gt;");
            case '&' -> writer.write("&amp;");
            default -> writer.write(character);
        }
    }

    public static void writeHtmlBegin(BufferedWriter writer) throws IOException {
        writer.write(
                "<!DOCTYPE html>" + System.lineSeparator() +
                        "<html>" + System.lineSeparator() +
                        "<head>" + System.lineSeparator() +
                        TAG_OFFSET + "<meta charset=\"UTF-8\">" +
                        System.lineSeparator() + "</head>" +
                        System.lineSeparator() + "<body>" +
                        System.lineSeparator());
    }

    public static void writeHtmlEnd(BufferedWriter writer) throws IOException {
        writer.write("</body>" + System.lineSeparator() + "</html>");
    }

    public static void convertCsvToHtml(String inputFileName, String outputFileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            writeHtmlBegin(writer);

            writer.write(TAG_OFFSET + HTML_TAG_TABLE_BEGIN);

            boolean isCellBegin = false;
            boolean isRowBegin = false;
            boolean isQuotedCell = false;
            boolean isQuotePreviousCharacter = false;

            String lineSeparator = System.lineSeparator();
            String rowOffset = TAG_OFFSET.repeat(2);
            String cellOffset = TAG_OFFSET.repeat(3);

            String line;

            while ((line = reader.readLine()) != null) {
                if (line.length() == 0) {
                    continue;
                }

                if ((isQuotePreviousCharacter || !isQuotedCell) && isRowBegin) {
                    writer.write(HTML_TAG_TABLE_CELL_END);
                    writer.write(lineSeparator + rowOffset + HTML_TAG_TABLE_ROW_END);

                    isCellBegin = false;
                    isRowBegin = false;
                    isQuotePreviousCharacter = false;
                    isQuotedCell = false;
                } else if (isRowBegin) {
                    writer.write(HTML_TAG_LINE_SEPARATOR);
                }

                if (!isRowBegin) {
                    writer.write(lineSeparator + rowOffset + HTML_TAG_TABLE_ROW_BEGIN);
                    writer.write(lineSeparator + cellOffset + HTML_TAG_TABLE_CELL_BEGIN);

                    isRowBegin = true;
                }

                int i = 0;

                while (i < line.length()) {
                    if (line.charAt(i) == '"') {
                        if (isCellBegin) {
                            if (isQuotePreviousCharacter) {
                                writeHtmlCharacter(writer, line.charAt(i));

                                isQuotePreviousCharacter = false;
                            } else {
                                isQuotePreviousCharacter = true;
                            }
                        } else {
                            isQuotedCell = true;
                            isCellBegin = true;
                        }
                    } else if (line.charAt(i) == ',') {
                        if (isQuotePreviousCharacter || !isQuotedCell) {
                            writer.write(HTML_TAG_TABLE_CELL_END);
                            writer.write(lineSeparator + cellOffset + HTML_TAG_TABLE_CELL_BEGIN);

                            isCellBegin = false;
                            isQuotedCell = false;
                        } else {
                            writeHtmlCharacter(writer, line.charAt(i));
                        }

                        isQuotePreviousCharacter = false;
                    } else {
                        writeHtmlCharacter(writer, line.charAt(i));

                        isCellBegin = true;
                    }

                    i++;
                }
            }

            if (isCellBegin) {
                writer.write(HTML_TAG_TABLE_CELL_END);
            }

            if (isRowBegin) {
                writer.write(lineSeparator + rowOffset + HTML_TAG_TABLE_ROW_END);
            }

            writer.write(lineSeparator + TAG_OFFSET + HTML_TAG_TABLE_END + lineSeparator);

            writeHtmlEnd(writer);
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
            errorMessage.append(System.lineSeparator()).append("Преобразование таблицы из файла формата CSV в файл HTML").
                    append(System.lineSeparator()).append("\t").append("-input имя файла в формате CSV").
                    append(System.lineSeparator()).append("\t").append("-output имя файла результата в формате HTML").
                    append(System.lineSeparator()).append("Пример запуска: -input \"CSV\\\\input.csv\" -output \"CSV\\\\output.html\"");

            System.out.println(errorMessage);
        } else {
            try {
                convertCsvToHtml(inputFileName, outputFileName);
            } catch (FileNotFoundException exception) {
                System.out.println("Файл " + inputFileName + " не найден");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
