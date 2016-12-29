package io.github.kkysen.quicktrip.apis;

import io.github.kkysen.quicktrip.web.Internet;

import java.io.IOException;

import org.jsoup.nodes.Document;

/**
 * 
 * 
 * @author Khyber Sen
 */
public abstract class RenderedHtmlRequest<R> extends AbstractHtmlRequest<R> {
    
    @Override
    protected Document getDocument(final String url) throws IOException {
        return Internet.getRenderedDocument(url);
    }
    
}
