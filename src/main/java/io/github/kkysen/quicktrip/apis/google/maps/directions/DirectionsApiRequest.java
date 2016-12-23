package io.github.kkysen.quicktrip.apis.google.maps.directions;

import io.github.kkysen.quicktrip.apis.google.maps.GoogleMapsApiRequest;

import java.util.Map;

// dummy class for now
// will become a JSON POJO

/**
 * 
 * 
 * @author Khyber Sen
 */
public class DirectionsApiRequest extends GoogleMapsApiRequest<Directions> {

    @Override
    protected String getRequestType() {
        return "directions";
    }

    @Override
    protected Map<String, String> getQuery() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Class<? extends Directions> getPojoClass() {
        return Directions.class;
    }
    
}