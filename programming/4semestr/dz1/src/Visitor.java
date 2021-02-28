package info.kgeorgiy.ja.dubrovin.walk;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static info.kgeorgiy.ja.dubrovin.walk.RecursiveWalk.write;

public class Visitor extends SimpleFileVisitor<Path> {
    final BufferedWriter outputWriter;

    public Visitor(BufferedWriter outputWriter) {
        this.outputWriter = outputWriter;
    }

    private long PJW(byte[] bytes, long hash, int size) {
        long high;
        for (int i = 0; i < size; i++) {
            hash = (hash << 8) + (bytes[i] & 0xFF);
            if ((high = hash & 0xFF00000000000000L) != 0) {
                hash ^= high >> 48;
            }
            hash &= ~high;
        }
        return hash;
    }

    @Override
    public FileVisitResult visitFile(Path currentFilePath, BasicFileAttributes attrs) {
        try (InputStream currentFileReader = Files.newInputStream(currentFilePath)) {
            byte[] bytes = new byte[2048];
            int size;
            long hash = 0;
            while ((size = currentFileReader.read(bytes)) >= 0) {
                hash = PJW(bytes, hash, size);
            }
            write(outputWriter, hash, currentFilePath.toString());
        } catch (IOException e) {
            write(outputWriter, 0, currentFilePath.toString());
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path currentFilePath, IOException e) {
        write(outputWriter, 0, currentFilePath.toString());
        return FileVisitResult.CONTINUE;
    }
}
