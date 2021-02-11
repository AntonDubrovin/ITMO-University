import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class RecursiveWalk {
    public static void main(String[] args) {
        Path inputFile;
        try {
            inputFile = Paths.get(args[0]);
        } catch (InvalidPathException e) {
            System.out.println("Invalid path to input file");
            return;
        }

        Path outputFile;
        try {
            outputFile = Paths.get(args[1]);
        } catch (InvalidPathException e) {
            System.out.println("Invalid path to output file");
            return;
        }

        try (BufferedReader inputFileReader = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8)) {
            try (BufferedWriter outputWriter = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8)) {
                String currentFile;
                while ((currentFile = inputFileReader.readLine()) != null) {
                    try {
                        Path currentFilePath = Paths.get(currentFile);
                        try {
                            Files.walkFileTree(currentFilePath, new Visitor(outputWriter));
                        } catch (IOException e) {
                            outputWriter.write("0000000000000000 " + currentFilePath);
                            outputWriter.newLine();
                        }
                    } catch (InvalidPathException e) {
                        outputWriter.write("0000000000000000 " + currentFile);
                        outputWriter.newLine();
                    }
                }
            } catch (IOException e) {
                System.out.println("Can't open output file");
            }
        } catch (IOException e) {
            System.out.println("Can't open input file");
        }
    }
}