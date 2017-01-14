package io.github.kkysen.quicktrip.apis.google;

import io.github.kkysen.quicktrip.json.Json;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
 * 
 * 
 * @author Khyber Sen
 * @author Stanley Lin
 */
@JsonAdapter(LatLngAdapter.class)
@Json
//@RequiredArgsConstructor
@Getter
public class LatLng {
    
    @SerializedName("lat")
    private final String latitude;
    
    @SerializedName("lng")
    private final String longitude;
    
    private final double latitudeDouble;
    private final double longitudeDouble;
    
    public LatLng(final String latitude, final String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        latitudeDouble = Double.parseDouble(latitude);
        longitudeDouble = Double.parseDouble(longitude);
    }
    
    public LatLng(final double latitude, final double longitude) {
        latitudeDouble = latitude;
        longitudeDouble = longitude;
        this.latitude = String.valueOf(latitude);
        this.longitude = String.valueOf(longitude);
    }
    
    @Override
    public String toString() {
        return latitude + "," + longitude;
    }
    
    /**
     * Denotes a unit of measure. Used with
     * {@link #distanceBetween(LatLng, LatLng, LatLng.Unit) distanceBetween}
     * 
     * @author Stanley Lin
     *
     */
    public static enum Unit {
    	MILES(69.172, 3959),
    	KILOMETERS(111.322, 6371);
    	
    	private double longDistAtEquator;
    	private double earthRadius;
    	
    	Unit(final double num, final double radius) {
    		longDistAtEquator = num;
    		earthRadius = radius;
    	}
    	
    	//The distance of 1 degree longitude at the equator
    	private double getEquatorDist() {return longDistAtEquator;}
    	
    	/*
    	 * 1 degree latitude is almost always the same. Returns the same
    	 * value. It's a separate method for readability
    	 * */
    	private double getLatitudeLength() {return longDistAtEquator;}
    	
    	private double getEarthRadius() { return earthRadius; }
    }
    
    private static double distance(double ptX1, double ptY1,
    		double ptX2, double ptY2) {
    	return Math.sqrt(
    			Math.pow((ptX1 - ptX2), 2) +
    			Math.pow((ptY1 - ptY2), 2)
    			);
    }
    
    /**
     * Calculates the distance between two latitude, longitude points using the
     * Haversine fomrula.
     * 
     * Credits to 
     * http://www.colorado.edu/geography/gcraft/warmup/aquifer/html/distance.html
     * http://www.movable-type.co.uk/scripts/latlong.html
     * 
     * 
     * 
     * @param	pt1			The first point
     * @param	pt2			The second point
     * @param	conversion	The output scale (Miles or Kilometers)
     * 
     * @return The distance between the two coordinates
     */
    public static double distanceBetween(LatLng pt1, LatLng pt2, 
    		LatLng.Unit conversion) {
    	/*
    	 * The second part of the conversion equation depends on the unit of measure,
    	 * so I'm calculating the first part separately.
    	 * 
    	final double oneDegLong1 = Math.cos(pt1.latitudeDouble);
    	final double oneDegLong2 = Math.cos(pt2.latitudeDouble);
    	
    	final double ptY1 = oneDegLong1 * conversion.getEquatorDist() *
    			pt1.longitudeDouble;
    	final double ptY2 = oneDegLong2 * conversion.getEquatorDist() *
    			pt2.longitudeDouble;
    	
    	final double ptX1 = pt1.latitudeDouble * conversion.getLatitudeLength();
    	final double ptX2 = pt2.latitudeDouble * conversion.getLatitudeLength();
    	
    	
    	//Now it boils down to distance
    	return distance(ptX1, ptY1, ptX2, ptY2);*/
    	
    	final double pt1X = pt1.latitudeDouble * (Math.PI/180);
    	final double pt1Y = pt1.longitudeDouble * (Math.PI/180);
    	final double pt2X = pt2.latitudeDouble * (Math.PI/180);
    	final double pt2Y = pt2.longitudeDouble * (Math.PI/180);
    	
    	double a = Math.pow(
    			Math.sin((pt1X - pt2X)/2), 2) +
    			Math.cos(pt1X) * Math.cos(pt2X) *
    			Math.pow(Math.sin((pt1Y - pt2Y)/2), 2);
    	double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    	double d = conversion.getEarthRadius() * c;
    	
    	return d;
    }
    
    public static void main(String[] args) {
    	LatLng one = new LatLng(-33, 6.0);
    	LatLng two = new LatLng(33.0, -1.0);
    	
    	System.out.println(distanceBetween(one, two, LatLng.Unit.MILES));
    	System.out.println(distanceBetween(one, two, Unit.KILOMETERS));
    }
}
