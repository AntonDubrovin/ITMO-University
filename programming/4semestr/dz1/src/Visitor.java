package info.kgeorgiy.ja.dubrovin.walk;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Visitor extends SimpleFileVisitor<Path> {
    BufferedWriter outputWriter;

    public Visitor(BufferedWriter outputWriter) {
        this.outputWriter = outputWriter;
    }

    private long PJW(byte[] bytes, long hash, long size) {
        long high;
        for (int i = 0; i < size; i++) {
            hash = (hash << 8) + bytes[i];
            if ((high = hash & 0xFF00000000000000L) != 0) {
                hash ^= high >> 48;
            }
            hash &= ~high;
        }
        return hash;
    }

    @Override
    public FileVisitResult visitFile(Path currentFilePath, BasicFileAttributes attrs) throws IOException {
        try (InputStream currentFileReader = Files.newInputStream(currentFilePath)) {
            byte[] bytes = new byte[1024];
            int size;
            long hash = 0;
            while ((size = currentFileReader.read(bytes)) >= 0) {
                hash = PJW(bytes, hash, size);
            }
            outputWriter.write(String.format("%016x", hash) + " " + currentFilePath);
            outputWriter.newLine();
        } catch (IOException e) {
            outputWriter.write(RecursiveWalk.ERROR_HASH + " " + currentFilePath.toString());
            outputWriter.newLine();
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path currentFilePath, IOException e) throws IOException {
        outputWriter.write(RecursiveWalk.ERROR_HASH + " " + currentFilePath.toString());
        outputWriter.newLine();
        return FileVisitResult.CONTINUE;
    }
}

