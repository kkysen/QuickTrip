package io.github.kkysen.quicktrip.apis.google;

import io.github.kkysen.quicktrip.json.Json;
import io.github.kkysen.quicktrip.json.PostProcessable;
import io.github.kkysen.quicktrip.reflect.Reflect;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
 * 
 * 
 * @author Khyber Sen
 */
@Json
@Getter
public class GoogleApiResponse implements PostProcessable {
    
    @SerializedName(value = "status", alternate = {"geocoder_status"}) // for Waypoint
    protected final String status;
    
    private transient boolean ok;
    private transient boolean impossible;
    
    public GoogleApiResponse(final String status) {
        this.status = status;
        postDeserialize();
    }
    
    @Override
    public void postDeserialize() {
        PostProcessable.super.postDeserialize();
        ok = status.equals("OK");
        impossible = status.equals("ZERO_RESULTS");
    }
    
    @Override
    public String toString() {
        return Reflect.toString(this);
    }
    
}
