package io.github.kkysen.quicktrip.apis;

import java.io.IOException;

import org.jsoup.nodes.Document;

/**
 * 
 * 
 * @author Khyber Sen
 */
public abstract class AbstractHtmlRequest<R> extends AbstractJsonRequest<R> {
    
    @Override
    protected final R parseFromUrl(final String url) throws IOException {
        return parseHtml(getDocument(url));
    }
    
    protected abstract Document getDocument(String url) throws IOException;
    
    protected abstract R parseHtml(final Document doc);
    
}
