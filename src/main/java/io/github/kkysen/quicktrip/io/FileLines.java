package io.github.kkysen.quicktrip.io;

import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class FileLines implements Iterable<String> {
    
    private final Path path;
    private final Charset charset;
    
    public FileLines(final Path path, final Charset charset) {
        this.path = path;
        this.charset = charset;
    }
    
    @Override
    public FileLineIterator iterator() {
        return new FileLineIterator(path, charset);
    }
    
}
