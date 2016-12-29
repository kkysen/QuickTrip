package io.github.kkysen.quicktrip.apis;

import io.github.kkysen.quicktrip.web.Internet;

import java.io.IOException;

import org.jsoup.nodes.Document;

/**
 * 
 * 
 * @author Khyber Sen
 */
public abstract class HtmlRequest<R> extends AbstractHtmlRequest<R> {
    
    @Override
    protected final Document getDocument(final String url) throws IOException {
        return Internet.getDocument(url);
    }
    
}
