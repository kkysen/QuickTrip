package io.github.kkysen.quicktrip.apis.google;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * 
 * 
 * @author Khyber Sen
 * @param <R> type of response
 */
public abstract class GoogleApiPostRequest<R> extends GoogleApiRequest<R> {
    
    @Override
    protected R deserializeFromUrl(final String url) throws ClientProtocolException, IOException {
        final String jsonRequestBody = GSON.toJson(this);
        StringEntity entity;
        try {
            entity = new StringEntity(jsonRequestBody);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("shouldn't happend", e); // shouldn't happen with JSON
        }
        final HttpPost postRequest = new HttpPost(url);
        postRequest.setEntity(entity);
        postRequest.addHeader("Content-type", "application/json");
        postRequest.addHeader("Accept", "application/json");
        final HttpClient httpClient = HttpClientBuilder.create().build();
        final HttpResponse response = httpClient.execute(postRequest);
        InputStream in;
        try {
            in = response.getEntity().getContent();
        } catch (final UnsupportedOperationException e) {
            throw new RuntimeException("shouldn't happend", e); // shouldn't happen with JSON
        } catch (final IOException e) {
            throw new RuntimeException("not sure why there was an IOException here", e);
        }
        return deserializeFromReader(new InputStreamReader(in));
    }
    
    @Override
    protected final void reflectQuery() {
        // do nothing
    }
    
    @Override
    protected final void modifyQuery(
            final io.github.kkysen.quicktrip.apis.CachedApiRequest.QueryParams query) {
        super.modifyQuery(query);
    }
    
}