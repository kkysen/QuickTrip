package io.github.kkysen.quicktrip.apis.google.places;

import io.github.kkysen.quicktrip.apis.ApiRequestException;
import io.github.kkysen.quicktrip.apis.QueryField;
import io.github.kkysen.quicktrip.apis.google.LatLng;

import java.nio.file.Path;
import java.util.List;

/**
 * 
 * 
 * @author Khyber Sen
 * @param <R> type of PlaceSearch
 * @param <E> type of PlaceResult
 */
public abstract class GooglePlaceSearchRequest<R extends PlaceSearch<E>, E extends PlaceResult> extends GooglePlacesRequest<R> {
    
    protected abstract String getSearchRequestType();
    
    @Override
    protected String getPlacesRequestType() {
        return getSearchRequestType() + "search";
    }
    
    @Override
    protected Path getRelativeCachePath() {
        return super.getRelativeCachePath().resolve("search");
    }
    
    protected abstract LatLng getLocation();
    
    /**
     * @return radius in meters
     */
    protected abstract int getRadius();
    
    /**
     * @return a place placeType as specified below
     * 
     * @see <a href=
     *      "https://developers.google.com/places/web-service/supported_types">
     *      https://developers.google.com/places/web-service/supported_types</a>
     */
    protected abstract String getPlaceType();
    
    private final @QueryField String location;
    private final @QueryField int radius;
    private final @QueryField String language = "en"; // American English
    protected final @QueryField(name = "type") String placeType;
    
    protected GooglePlaceSearchRequest() {
        location = getLocation().toString();
        radius = getRadius();
        placeType = getPlaceType();
    }
    
    public List<E> getResults() throws ApiRequestException {
        final PlaceSearch<E> response = getResponse();
        if (response.isOk()) {
            return response.getResults();
        } else {
            throw new NullPointerException("no results");
        }
    }
    
}
