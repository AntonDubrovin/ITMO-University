import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Visitor extends SimpleFileVisitor<Path> {
    BufferedWriter outputWriter;

    public Visitor(BufferedWriter outputWriter) {
        this.outputWriter = outputWriter;
    }

    private long PJW(char[] chars, long hash) {
        long high;
        for (char currentChar : chars) {
            hash = (hash << 4) + currentChar;
            if ((high = hash & 0xF0000000) != 0) {
                hash ^= high >> 24;
            }
            hash &= ~high;
        }
        return hash;
    }

    @Override
    public FileVisitResult visitFile(Path currentFilePath, BasicFileAttributes attrs) throws IOException {
        try (BufferedReader currentFileReader = Files.newBufferedReader(currentFilePath, StandardCharsets.UTF_8)) {
            char[] chars = new char[1024];
            int c = 0;
            long hash = 0;
            while ((c = currentFileReader.read(chars)) >= 0) {
                hash = PJW(chars, hash);
            }
            outputWriter.write(String.format("%016x", hash) + " " + currentFilePath);
            outputWriter.newLine();
        } catch (IOException e) {
            outputWriter.write("0000000000000000 " + currentFilePath.toString());
            outputWriter.newLine();
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path currentFilePath, IOException e) throws IOException {
        outputWriter.write("0000000000000000 " + currentFilePath.toString());
        outputWriter.newLine();
        return FileVisitResult.CONTINUE;
    }
}
