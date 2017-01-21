package io.github.kkysen.quicktrip.apis.google.places;

import io.github.kkysen.quicktrip.apis.QueryField;

/**
 * 
 * 
 * @author Khyber Sen
 */
public abstract class GoogleNearbyRequest<R extends NearbySearch<E>, E extends NearbyResult>
        extends GooglePlaceSearchRequest<R, E> {
    
    @Override
    protected String getSearchRequestType() {
        return "nearby";
    }
    
    @Override
    protected Class<? extends R> getResponseClass() {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * @return keyword
     *         defaults to null
     */
    protected String getKeyword() {
        return null;
    }
    
    /**
     * @return true if results should be ranked by prominence
     *         false if results should be ranked by distance
     *         defaults to true
     */
    protected boolean rankByProminence() {
        return true;
    }
    
    private final @QueryField String keyword;
    private final @QueryField(name = "rankby") String rankBy; // prominence or distance
    
    protected GoogleNearbyRequest() {
        keyword = getKeyword();
        final boolean rankByProminence = rankByProminence();
        rankBy = rankByProminence ? "prominence" : "distance";
        if (!rankByProminence && placeType == null || placeType.isEmpty()) {
            throw new IllegalArgumentException(
                    "if rankBy == distance, then placeType or keyword must be set");
        }
    }
    
}
