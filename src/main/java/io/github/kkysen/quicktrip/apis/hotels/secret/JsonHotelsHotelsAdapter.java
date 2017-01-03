package io.github.kkysen.quicktrip.apis.hotels.secret;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class JsonHotelsHotelsAdapter extends TypeAdapter<List<JsonHotelsHotel>> {
    
    private JsonReader in;
    
    private void readUntilName(final String name) throws IOException {
        while (in.hasNext()) {
            if (in.nextName().equals(name)) {
                break;
            } else {
                in.skipValue();
            }
        }
    }
    
    private void readUntilEnd() throws IOException {
        readUntilName(null);
    }
    
    private String nextStringNamed(final String name) throws IOException {
        readUntilName(name);
        return in.nextString();
    }
    
    private String nextString() throws IOException {
        in.nextName();
        return in.nextString();
    }
    
    private static String joinNonEmpty(final String delimiter, final String... strings) {
        final StringJoiner sj = new StringJoiner(delimiter);
        for (final String s : strings) {
            if (!(s == null || s.isEmpty())) {
                sj.add(s);
            }
        }
        return sj.toString();
    }
    
    public JsonHotelsHotel readHotel() throws IOException {
        final String name;
        final String phoneNumber;
        final String address;
        final String imgUrl;
        final double rating;
        final double distance;
        final int price;
        
        in.beginObject();
        
        // name
        name = nextStringNamed("name");
        
        // phoneNumer
        readUntilName("telephone");
        in.beginObject();
        phoneNumber = nextStringNamed("number");
        readUntilEnd();
        in.endObject();
        
        // address
        readUntilName("address");
        in.beginObject();
        final String streetAddress = nextString();
        final String extendedAddress = nextString();
        final String locality = nextString();
        final String zipCode = nextString();
        final String region = nextString();
        final String country = nextString();
        address = joinNonEmpty(", ", streetAddress, extendedAddress, locality,
                region, zipCode, country);
        readUntilEnd();
        in.endObject();
        
        // imgUrl
        imgUrl = nextStringNamed("thumbnailUrl");
        
        // rating
        rating = Double.parseDouble(nextStringNamed("starRating"));
        
        // distance
        readUntilName("landmarks");
        in.beginArray();
        in.beginObject();
        distance = Double.parseDouble(nextStringNamed("distance"));
        in.endObject();
        in.skipValue();
        in.endArray();
        
        // price
        readUntilName("ratePlan");
        in.beginObject();
        readUntilName("price");
        in.beginObject();
        price = Integer.parseInt(nextStringNamed("current").substring(1));
        readUntilEnd();
        in.endObject();
        readUntilEnd();
        in.endObject();
        
        in.endObject();
        
        return new JsonHotelsHotel(name, phoneNumber, address, imgUrl, rating, distance, price);
    }
    
    @Override
    public List<JsonHotelsHotel> read(final JsonReader in) throws IOException {
        final List<JsonHotelsHotel> hotels = new ArrayList<>();
        this.in = in;
        in.beginArray();
        while (in.hasNext()) {
            hotels.add(readHotel());
        }
        in.endArray();
        return hotels;
    }
    
    @Override
    public void write(final JsonWriter out, final List<JsonHotelsHotel> value) throws IOException {
        // not used
    }
    
}
