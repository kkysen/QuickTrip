package io.github.kkysen.quicktrip.apis.google.geocoding;

import io.github.kkysen.quicktrip.apis.google.LatLng;
import io.github.kkysen.quicktrip.apis.google.LatLngAdapter;
import io.github.kkysen.quicktrip.json.MissingInformationException;
import io.github.kkysen.quicktrip.json.TypeReaderAdapter;

import java.io.IOException;
import java.util.Map;

import com.google.gson.stream.JsonReader;


/**
 * 
 * 
 * @author Khyber Sen
 */
public class GeolocationAdapter extends TypeReaderAdapter<Geolocation> {

    private static final LatLngAdapter latLngAdapter = new LatLngAdapter();
    
    private Geolocation geolocation;
    
    private String readStatus() throws IOException {
        return in.nextString();
    }
    
    private LatLng readLatLng(final JsonReader in) throws IOException {
        in.beginObject();
        if (!in.nextName().equals("location")) {
            throw new MissingInformationException("location");
        }
        final LatLng location = latLngAdapter.read(in);
        readUntilEnd();
        in.endObject();
        return location;
    }
    
    private String readAddress(final JsonReader in) throws IOException {
        return in.nextString();
    }
    
    private String readPlaceId(final JsonReader in) throws IOException {
        return in.nextString();
    }
    
    @Override
    protected void addPropertyReaders() {
        addPropertyReader("geometry", this::readLatLng);
        addPropertyReader("formatted_address", this::readAddress);
        addPropertyReader("place_id", this::readPlaceId);
    }

    
    @Override
    public void read() throws IOException, MissingInformationException {
        final String resultsName = in.nextName();
        if (!resultsName.equals("results")) {
            throw new MissingInformationException("results");
        }
        in.beginArray();
        in.beginObject();
        final Map<String, Object> readResult = readObj();
        in.endObject();
        while (in.hasNext()) {
            in.skipValue();
        }
        final String statusName = in.nextName();
        if (!statusName.equals("status")) {
            throw new MissingInformationException("status");
        }
        final String status = readStatus();
        final LatLng location = (LatLng) readResult.get("geometry");
        final String address = (String) readResult.get("formatted_addresss");
        final String[] addressParts = address.split(", ");
        final String country = addressParts[addressParts.length - 1];
        final String placeId = (String) readResult.get("place_id");
        geolocation = new Geolocation(status, location, country, placeId);
    }
    
    @Override
    public Geolocation get() {
        return geolocation;
    }
    
}
