package io.github.kkysen.quicktrip.apis.google;

import io.github.kkysen.quicktrip.json.Json;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
public class LatLng implements Clusterable {
    
    public static final LatLng NYC = new LatLng("40.7128", "-74.0059");
    public static final LatLng LONDON = new LatLng("51.5074", "-0.1278");
    public static final LatLng LA = new LatLng("34.0522", "-118.2437");
    
    public static final DistanceMeasure HAVERSINE_DISTANCE = (final double[] a,
            final double[] b) -> {
        return haversineDistance(a, b, Unit.KILOMETERS);
    };
    
    @SerializedName("lat")
    private final String latitude;
    
    @SerializedName("lng")
    private final String longitude;
    
    private final double latitudeDouble;
    private final double longitudeDouble;
    
    private final double[] coords;
    
    public LatLng(final String latitude, final String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        latitudeDouble = Double.parseDouble(latitude);
        longitudeDouble = Double.parseDouble(longitude);
        coords = new double[] {latitudeDouble, longitudeDouble};
    }
    
    public LatLng(final double latitude, final double longitude) {
        latitudeDouble = latitude;
        longitudeDouble = longitude;
        coords = new double[] {latitudeDouble, longitudeDouble};
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
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static enum Unit {
        
        METERS(111322, 6371000),
        MILES(69.172, 3959),
        KILOMETERS(111.322, 6371);
        
        private final double equatorDist; //The distance of 1 degree longitude at the equator
        private final double earthRadius;
        
        /**
         * 1 degree latitude is almost always the same. Returns the same
         * value. It's a separate method for readability
         */
        private double getLatitudeLength() {
            return equatorDist;
        }
        
    }
    
    private static double distance(final double ptX1, final double ptY1,
            final double ptX2, final double ptY2) {
        return Math.sqrt(
                Math.pow(ptX1 - ptX2, 2)
                        + Math.pow(ptY1 - ptY2, 2));
    }
    
    private static double haversine(final double angle) {
        return Math.pow(Math.sin(angle / 2), 2);
    }
    
    private static double cosProduct(final double a, final double b) {
        return Math.cos(a) * Math.cos(b);
    }
    
    private static final double DEG_TO_RAD = Math.PI / 180;
    
    /**
     * Calculates the distance between two latitude, longitude points using the
     * Haversine fomrula.
     * 
     * Credits to
     * http://www.colorado.edu/geography/gcraft/warmup/aquifer/html/distance.html
     * http://www.movable-type.co.uk/scripts/latlong.html
     * 
     * @param pt1 the first point
     * @param pt2 the second point
     * @param unit the output unit (meters, miles, or kilometers)
     * 
     * @return the distance between the two coordinates
     */
    private static double haversineDistance(final double[] pt1, final double[] pt2,
            final Unit unit) {
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
        
        final double pt1X = pt1[0] * DEG_TO_RAD;
        final double pt1Y = pt1[1] * DEG_TO_RAD;
        final double pt2X = pt2[0] * DEG_TO_RAD;
        final double pt2Y = pt2[1] * DEG_TO_RAD;
        
        final double a = haversine(pt1X - pt2X)
                + cosProduct(pt1X, pt2X) * haversine(pt1Y - pt2Y);
        final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        final double d = unit.getEarthRadius() * c;
        
        //System.out.println(pt1.toString() + " -> " + pt2.toString() + " = " + d);
        return d;
    }
    
    public static double distanceBetween(final LatLng pt1, final LatLng pt2,
            final Unit unit) {
        return haversineDistance(pt1.coords, pt2.coords, unit);
    }
    
    /**
     * @param other other LatLng latLng
     * @return approximate distance to the other latLng, rounded up
     */
    public double approximateDistanceTo(final LatLng other) {
        return distanceTo(other); // FIXME only for now for testing
    }
    
    public double distanceTo(final LatLng other, final Unit unit) {
        return distanceBetween(this, other, unit);
    }
    
    public double distanceTo(final LatLng other) {
        return distanceTo(other, Unit.KILOMETERS);
    }
    
    /**
     * @param other other latLng
     * @param radius radius from this latLng in meters
     * @return if other is in this radius
     */
    public boolean inRadius(final LatLng other, final double radius) {
        return distanceTo(other) < radius;
    }
    
    /**
     * @param other other latLng
     * @param radius radius from this latLng in meters
     * @return if other is in this radius
     *         if it's borderline, then it will return true
     */
    public boolean approximateInRadius(final LatLng other, final double radius) {
        return approximateDistanceTo(other) < radius;
    }
    
    public static void main(final String[] args) {
        final LatLng one = new LatLng(-33, 6.0);
        final LatLng two = new LatLng(33.0, -1.0);
        
        System.out.println(distanceBetween(one, two, Unit.MILES));
        System.out.println(distanceBetween(one, two, Unit.KILOMETERS));
        System.out.println(NYC.distanceTo(LONDON, Unit.MILES)); // should be 3470
    }
    
    @Override
    public double[] getPoint() {
        return coords;
    }
    
}
