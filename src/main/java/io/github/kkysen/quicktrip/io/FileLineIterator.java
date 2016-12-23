package io.github.kkysen.quicktrip.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class FileLineIterator implements Iterator<String> {
    
    private BufferedReader reader;
    private String line;
    
    public FileLineIterator(final Path path, final Charset charset) {
        try {
            reader = Files.newBufferedReader(path, charset);
        } catch (final IOException e) {
            e.printStackTrace();
            throw new NullPointerException("BufferedReader is null");
        }
    }
    
    @Override
    public boolean hasNext() {
        try {
            line = reader.readLine();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return line != null;
    }
    
    @Override
    public String next() {
        return line;
    }
    
}
