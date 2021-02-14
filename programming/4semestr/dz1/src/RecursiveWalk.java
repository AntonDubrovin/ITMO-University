package info.kgeorgiy.ja.dubrovin.walk;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class RecursiveWalk {
    public static final String ERROR_HASH = String.format("%016x", 0) ;

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
                            outputWriter.write(ERROR_HASH + " " + currentFilePath);
                            outputWriter.newLine();
                        }
                    } catch (InvalidPathException e) {
                        outputWriter.write(ERROR_HASH + " " + currentFile);
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
