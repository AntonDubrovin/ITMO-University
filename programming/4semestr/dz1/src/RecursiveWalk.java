package info.kgeorgiy.ja.dubrovin.walk;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class RecursiveWalk {
    public static void main(String[] args) {
        if (args == null || args.length != 2 || args[0] == null || args[1] == null) {
            System.err.println("Invalid program arguments");
            return;
        }

        Path inputFile;
        try {
            inputFile = Paths.get(args[0]);
        } catch (InvalidPathException e) {
            System.err.println("Invalid path to input file");
            return;
        }

        Path outputFile;
        try {
            outputFile = Paths.get(args[1]);
        } catch (InvalidPathException e) {
            System.err.println("Invalid path to output file");
            return;
        }

        try {
            if (Files.notExists(outputFile.getParent())) {
                Files.createDirectories(outputFile.getParent());
            }
        } catch (NullPointerException | IOException e) {
            System.err.println("Can't create output file directory");
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
                            write(outputWriter, 0, currentFilePath);
                        }
                    } catch (InvalidPathException e) {
                        write(outputWriter, 0, Path.of(currentFile));
                    }
                }
            } catch (IOException e) {
                System.err.println("Something went wrong with output file");
            }
        } catch (IOException e) {
            System.err.println("Something went wrong with input file");
        }
    }

    public static void write(BufferedWriter outputWriter, long outputString, Path currentFilePath) {
        try {
            outputWriter.write(String.format("%016x", outputString) + " " + currentFilePath);
            outputWriter.newLine();
        } catch (IOException e) {
            System.err.println("Something went wrong with output in file");
        }
    }
}
