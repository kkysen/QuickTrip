package io.github.kkysen.quicktrip.apis.hotels.secret;

import io.github.kkysen.quicktrip.json.MissingInformationException;
import io.github.kkysen.quicktrip.json.TypeReaderAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import com.google.gson.stream.JsonReader;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class JsonHotelsHotelsAdapter extends TypeReaderAdapter<List<JsonHotelsHotel>> {
    
    private List<JsonHotelsHotel> hotels;
    
    private static String joinNonEmpty(final String delimiter, final String... strings) {
        final StringJoiner sj = new StringJoiner(delimiter);
        for (final String s : strings) {
            if (!(s == null || s.isEmpty())) {
                sj.add(s);
            }
        }
        return sj.toString();
    }
    
    /*public JsonHotelsHotel readHotel1() throws IOException, MissingHotelInformationException {
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
        final String distanceStr = nextStringNamed("distance");
        distance = Double.parseDouble(distanceStr.substring(0, distanceStr.indexOf(' ')));
        readUntilEnd();
        in.endObject();
        in.skipValue();
        in.endArray();
        
        // price
        readUntilName("ratePlan");
        try {
            in.beginObject();
            readUntilName("price");
            in.beginObject();
            price = Integer.parseInt(nextStringNamed("current").substring(1));
            readUntilEnd();
            in.endObject();
            readUntilEnd();
            in.endObject();
        } catch (final IOException e) { // no ratePlan
            throw new MissingHotelInformationException("price", e);
        }
        
        try {
            readUntilEnd();
            in.endObject();
        } catch (final IllegalStateException e) {
            System.out.println(new JsonHotelsHotel(name, phoneNumber, address, imgUrl, rating,
                    distance, price));
            System.out.println(in);
            throw new IOException(e);
        }
        
        return new JsonHotelsHotel(name, phoneNumber, address, imgUrl, rating, distance, price);
    }*/
    
    private String readName(final JsonReader in) throws IOException {
        return in.nextString();
    }
    
    private String readPhoneNumber(final JsonReader in) throws IOException {
        in.beginObject();
        final String phoneNumber = nextStringNamed("number");
        readUntilEnd();
        in.endObject();
        return phoneNumber;
    }
    
    private String readAddress(final JsonReader in) throws IOException {
        in.beginObject();
        final String streetAddress = nextString();
        final String extendedAddress = nextString();
        final String locality = nextString();
        final String zipCode = nextString();
        final String region = nextString();
        final String country = nextString();
        final String address = joinNonEmpty(", ", streetAddress, extendedAddress, locality,
                region, zipCode, country);
        readUntilEnd();
        in.endObject();
        return address;
    }
    
    private String readImgUrl(final JsonReader in) throws IOException {
        return in.nextString();
    }
    
    private double readRating(final JsonReader in) throws IOException {
        return in.nextDouble();
    }
    
    private double readDistance(final JsonReader in) throws IOException {
        in.beginArray();
        in.beginObject();
        final String distanceStr = nextStringNamed("distance");
        final double distance = Double
                .parseDouble(distanceStr.substring(0, distanceStr.indexOf(' ')));
        readUntilEnd();
        in.endObject();
        in.skipValue();
        in.endArray();
        return distance;
    }
    
    private int readPrice(final JsonReader in) throws IOException {
        in.beginObject();
        readUntilName("price");
        in.beginObject();
        final int price = Integer.parseInt(nextStringNamed("current").substring(1));
        readUntilEnd();
        in.endObject();
        readUntilEnd();
        in.endObject();
        return price;
    }
    
    @Override
    protected void addPropertyReaders() {
        addPropertyReader("name", this::readName);
        addPropertyReader("telephone", this::readPhoneNumber);
        addPropertyReader("address", this::readAddress);
        addPropertyReader("thumbnailUrl", this::readImgUrl);
        addPropertyReader("starRating", this::readRating);
        addPropertyReader("landmarks", this::readDistance);
        addPropertyReader("ratePlan", this::readPrice);
    }
    
    private JsonHotelsHotel readHotel() throws IOException, MissingInformationException {
        final Map<String, Object> readObj = readObj();
        final String name = (String) readObj.get("name");
        final String phoneNumber = (String) readObj.get("telephone");
        final String address = (String) readObj.get("address");
        final String imgUrl = (String) readObj.get("thumbnailUrl");
        final double rating = (double) readObj.get("starRating");
        final double distance = (double) readObj.get("landmarks");
        final int price = (int) readObj.get("ratePlan");
        return new JsonHotelsHotel(name, phoneNumber, address, imgUrl, rating, distance, price);
    }
    
    @Override
    public void read() throws IOException {
        hotels = new ArrayList<>();
        in.beginArray();
        while (in.hasNext()) {
            try {
                hotels.add(readHotel());
            } catch (final MissingInformationException e) {
                // skip hotel if missing info (price)
            }
        }
        in.endArray();
    }
    
    @Override
    public List<JsonHotelsHotel> get() {
        return hotels;
    }
    
}
