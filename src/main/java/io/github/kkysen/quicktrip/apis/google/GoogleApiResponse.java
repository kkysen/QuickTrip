package io.github.kkysen.quicktrip.apis.google;

import io.github.kkysen.quicktrip.json.Json;

import lombok.Getter;

/**
 * 
 * 
 * @author Khyber Sen
 */
@Json
public class GoogleApiResponse {
    
    protected final @Getter String status;
    
    private Boolean ok;
    
    public GoogleApiResponse(final String status) {
        this.status = status;
        isOk();
    }
    
    public boolean isOk() {
        if (ok == null) {
            ok = status.equals("OK");
        }
        return ok;
    }
    
}
