package io.github.kkysen.quicktrip.io;

import io.github.kkysen.quicktrip.Constants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.StreamSupport;

import org.apache.commons.io.FileUtils;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class MyFiles {
    
    private static final Charset CHARSET = Constants.CHARSET;
    private static final String ILLEGAL_FILE_CHARS = "\\\\" + "/" + "\\?" + "%" + "\\*"
            + ":" + "\\|" + "\"" + "<" + ">";
    
    public static String read(final Path path) throws IOException {
        return new String(Files.readAllBytes(path), CHARSET);
    }
    
    public static void write(final Path path, final String contents) throws IOException {
        Files.write(path, contents.getBytes(CHARSET));
    }
    
    public static void write(final Path path, final BufferedReader reader) throws IOException {
        final BufferedWriter writer = Files.newBufferedWriter(path);
        String line;
        while ((line = reader.readLine()) != null) {
            writer.write(line);
            writer.newLine();
        }
        writer.close();
    }
    
    public static <T> void write(final Path path, final Iterable<T> iter) throws IOException {
        final Iterable<String> lines = StreamSupport.stream(iter.spliterator(), false)
                .map(T::toString)::iterator;
        Files.write(path, lines, CHARSET);
    }
    
    public static <T> void write(final Path path, final T t) throws IOException {
        write(path, t.toString());
    }
    
    public static void append(final Path path, final String contents) throws IOException {
        write(path, read(path) + contents);
    }
    
    public static <T> void append(final Path path, final T t) throws IOException {
        append(path, t.toString());
    }
    
    public static void modify(final Path readPath, final Path writePath,
            final Consumer<byte[]> modifier)
            throws IOException {
        final byte[] bytes = Files.readAllBytes(readPath);
        modifier.accept(bytes);
        Files.write(writePath, bytes);
    }
    
    public static void modify(final Path path, final Consumer<byte[]> modifier) throws IOException {
        modify(path, path, modifier);
    }
    
    public static void modifyLines(final Path readPath, final Path writePath,
            final Function<String, String> modifier)
            throws IOException {
        final List<String> lines = readLines(readPath);
        for (int i = 0; i < lines.size(); i++) {
            lines.set(i, modifier.apply(lines.get(i)));
        }
        Files.write(writePath, lines, CHARSET);
    }
    
    public static void modifyLines(final Path path, final Function<String, String> modifier)
            throws IOException {
        modifyLines(path, path, modifier);
    }
    
    public static <R> R apply(final Path path, final Function<byte[], R> function)
            throws IOException {
        final byte[] bytes = Files.readAllBytes(path);
        final R r = function.apply(bytes);
        Files.write(path, bytes);
        return r;
    }
    
    public static List<String> readLines(final Path path) throws IOException {
        return Files.readAllLines(path, CHARSET);
    }
    
    public static FileLines readLinesAsIterable(final Path path) {
        return new FileLines(path, CHARSET);
    }
    
    private static void walkDirectory(final List<Path> paths, final Path dir, final int maxDepth)
            throws IOException {
        if (maxDepth > 0) {
            final DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dir);
            for (final Path file : directoryStream) {
                if (Files.isDirectory(file)) {
                    walkDirectory(paths, file, maxDepth - 1);
                } else {
                    paths.add(file);
                }
            }
        }
    }
    
    public static List<Path> walkDirectory(final Path dir, final int maxDepths) throws IOException {
        final List<Path> paths = new ArrayList<>();
        walkDirectory(paths, dir, maxDepths);
        return paths;
    }
    
    public static String fixFileName(final String fileName) {
        return fileName.replaceAll("[" + ILLEGAL_FILE_CHARS + "]", "_");
    }
    
    public static void deleteDirectory(final Path dir) throws IOException {
        FileUtils.deleteDirectory(new File(dir.toString()));
    }
    
    public static void createDirectoryOverwriting(final Path dir) throws IOException {
        deleteDirectory(dir);
        Files.createDirectory(dir);
    }
    
    public static boolean createFileIfNonExists(final Path path) throws IOException {
        if (Files.exists(path)) {
            return false;
        }
        Files.createFile(path);
        return true;
    }
    
    public static void main(final String[] args) throws Exception {
        final Path path = Paths.get("C:/Users/kkyse/Desktop/lit", "stories");
        createDirectoryOverwriting(path);
    }
    
}
