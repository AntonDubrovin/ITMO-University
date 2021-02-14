package info.kgeorgiy.ja.dubrovin.walk;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Visitor extends SimpleFileVisitor<Path> {
    BufferedWriter outputWriter;

    public Visitor(BufferedWriter outputWriter) {
        this.outputWriter = outputWriter;
    }

    private long PJW(long hash, int symbol) {
        long high;
        hash = (hash << 8) + symbol;
        if ((high = hash & 0xFF00000000000000L) != 0) {
            hash ^= high >> 48;
        }
        hash &= ~high;
        return hash;
    }

    @Override
    public FileVisitResult visitFile(Path currentFilePath, BasicFileAttributes attrs) throws IOException {
        try (BufferedInputStream currentFileReader = new BufferedInputStream(Files.newInputStream(currentFilePath))) {
            int symbol;
            long hash = 0;
            while ((symbol = currentFileReader.read()) >= 0) {
                hash = PJW(hash, symbol);
            }
            outputWriter.write(String.format("%016x", hash) + " " + currentFilePath);
        } catch (IOException e) {
            outputWriter.write(RecursiveWalk.ERROR_HASH + " " + currentFilePath);
        }
        outputWriter.newLine();
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path currentFilePath, IOException e) throws IOException {
        outputWriter.write(RecursiveWalk.ERROR_HASH + " " + currentFilePath);
        outputWriter.newLine();
        return FileVisitResult.CONTINUE;
    }
}

