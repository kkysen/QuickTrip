package io.github.kkysen.quicktrip.apis.google;

import io.github.kkysen.quicktrip.json.Json;
import io.github.kkysen.quicktrip.reflect.Reflect;

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
    private Boolean impossible;
    
    public GoogleApiResponse(final String status) {
        this.status = status;
        isOk();
        isImpossible();
    }
    
    public boolean isOk() {
        if (ok == null) {
            ok = status.equals("OK");
        }
        return ok;
    }
    
    public boolean isImpossible() {
        if (impossible == null) {
            impossible = status.equals("ZERO_RESULTS");
        }
        return impossible;
    }
    
    @Override
    public String toString() {
        return Reflect.toString(this);
    }
    
}
