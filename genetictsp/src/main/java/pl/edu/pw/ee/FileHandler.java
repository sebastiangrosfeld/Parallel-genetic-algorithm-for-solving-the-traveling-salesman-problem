package pl.edu.pw.ee;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class FileHandler {

    private final static char DECIMAL_SEPARATOR = ',';
    private final static String DECIMAL_FORMAT_PATTERN = "0.0";

    public static List<Node> readNodesFromFile(String filePath) throws FileNotFoundException {
        final var file = new File(filePath);
        final var scanner = new Scanner(file);

        final var numberOfNodes = readNumberOfNodes(scanner);
        final List<Node> nodes = new ArrayList<>();

        while (nodes.size() < numberOfNodes && scanner.hasNext()) {
            final var node = readNode(scanner);
            nodes.add(node);
        }

        return nodes;
    }

    public static void writeResultNodesToFile(List<Node> nodes, String filename) {
        final var stringBuilder = new StringBuilder();
        stringBuilder.append(nodes.size()).append("\n");

        for (final var node : nodes) {
            stringBuilder.append(formatDoubleWithComma(node.getXCoordinate()))
                    .append(" ")
                    .append(formatDoubleWithComma(node.getYCoordinate()))
                    .append("\n");
        }

        try (final var writer = new FileWriter(filename)) {
            writer.write(stringBuilder.toString());
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new RuntimeException("Error in writing to file.");
        }
    }

    private static int readNumberOfNodes(Scanner scanner) {
        if (scanner.hasNextInt()) {
            return scanner.nextInt();
        }

        printFileFormatMessageAdvice();

        if (scanner.hasNext()) {
            throw new IllegalArgumentException("First character in file must be number of nodes, expected integer.");
        }

        throw new IllegalArgumentException("File cannot be empty");
    }

    private static Node readNode(Scanner scanner) {
        if (scanner.hasNextDouble()) {
            final var x = scanner.nextDouble();

            if (scanner.hasNextDouble()) {
                final var y = scanner.nextDouble();
                return new Node(x, y);
            } else {
                printFileFormatMessageAdvice();
                throw new IllegalArgumentException("Value of y must be double.");
            }
        }

        printFileFormatMessageAdvice();
        throw new IllegalArgumentException("Value of x must be double.");
    }

    private static void printFileFormatMessageAdvice() {
        System.out.println("""
                Provided input file format is incorrect. Below is example file.\s
                Example file:\s
                3\s
                1.0 1.0\s
                2.0 2.0\s
                3.0 3.0\s
                """);
    }

    private static String formatDoubleWithComma(double number) {
        final var locale = Locale.getDefault();
        final var symbols = new DecimalFormatSymbols(locale);
        symbols.setDecimalSeparator(DECIMAL_SEPARATOR);

        final var decimalFormat = new DecimalFormat(DECIMAL_FORMAT_PATTERN, symbols);
        return decimalFormat.format(number);
    }

}
