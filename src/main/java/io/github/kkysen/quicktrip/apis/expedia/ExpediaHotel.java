package io.github.kkysen.quicktrip.apis.expedia;

import org.jsoup.nodes.Element;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class ExpediaHotel {
    
    private final String pictureUrl;
    private String name;
    private double rating;
    private String phoneNumber;
    
    private static String parsePictureUrl(final Element flexLinkWrap) {
        final Element flexFigure = flexLinkWrap.getElementsByClass("flex-figure").get(0);
        final Element hotelThumbnail = flexFigure.getElementsByClass("hotel-thumbnail").get(0);
        return hotelThumbnail.attr("data-image").substring(2);
    }
    
    public ExpediaHotel(final Element flexLinkWrap) {
        pictureUrl = parsePictureUrl(flexLinkWrap);
    }
    
}
