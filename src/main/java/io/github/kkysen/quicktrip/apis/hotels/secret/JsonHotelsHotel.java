package io.github.kkysen.quicktrip.apis.hotels.secret;

import io.github.kkysen.quicktrip.apis.Json;
import io.github.kkysen.quicktrip.app.Hotel;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 
 * 
 * @author Khyber Sen
 */
@Json
//@NoArgsConstructor my own Adapter uses AllArgsConstructor only
@AllArgsConstructor
@Getter
//@Setter final fields
public class JsonHotelsHotel implements Hotel {
    
    /*
     * FIXME this is unfinished
     * these are the main fields I want, but they're not all simple fields, but objects
     * JsonHotelsHotelsAdapter right now only does these fields w/o any error checking
     */
    
    private final String name;
    private final String phoneNumber;
    private final String address;
    private final String imgUrl;
    private final double rating;
    private final double distance;
    private final int price;
    
}
