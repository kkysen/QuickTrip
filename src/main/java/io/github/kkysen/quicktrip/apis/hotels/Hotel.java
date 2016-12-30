package io.github.kkysen.quicktrip.apis.hotels;

import org.jsoup.nodes.Element;

import lombok.Getter;

/**
 * 
 * 
 * @author Khyber Sen
 */
@Getter
public class Hotel implements io.github.kkysen.quicktrip.app.Hotel {
    
    private final int price;
    private final String name;
    private final String address;
    private final double distance; // miles
    private final double rating;
    private final String imgUrl;
    
    private static int parsePrice(final Element hotelWrap) {
        final String priceStr = hotelWrap.getElementsByClass("pricing").get(0)
                .getElementsByClass("price").get(0).text();
        final String[] prices = priceStr.split("\\p{Sc}"); // split on any currency symbol
        return Integer.parseInt(prices[prices.length - 1]);
    }
    
    private static String parseName(final Element descriptionHCardRespModule) {
        return descriptionHCardRespModule.getElementsByClass("p-name").text();
    }
    
    private static String parseAddress(final Element descriptionHCardRespModule) {
        return descriptionHCardRespModule.getElementsByClass("p-adr").text();
    }
    
    private static double parseDistance(final Element detailsRespModule) {
        final Element locationInfoRespModuleDistanceSortApplied = detailsRespModule
                .getElementsByClass("location-info").get(0);
        final String distanceStr = //
                locationInfoRespModuleDistanceSortApplied.getElementsByTag("ul").get(0).text();
        final int endIndex = distanceStr.indexOf(" miles");
        return Double.parseDouble(distanceStr.substring(0, endIndex));
    }
    
    private static double parseRating(final Element detailsRespModule) {
        if (detailsRespModule.getElementsByClass("reviews-box").size() == 0) {
            return 0; // no rating provided
        }
        final String ratingStr = detailsRespModule.getElementsByClass("ta-logo").get(0).text();
        final int startIndex = ratingStr.lastIndexOf(' ') + 1;
        return Double.parseDouble(ratingStr.substring(startIndex));
    }
    
    private static String parseImgUrl(final Element descriptionHCardRespModule) {
        final String style = descriptionHCardRespModule.getElementsByTag("img").attr("style");
        // style = "background-image:url('https://...jpg');" every time
        return style.substring(22, style.length() - 3);
    }
    
    public Hotel(final Element hotelWrap) throws MissingHotelInformationException {
        try {
            price = parsePrice(hotelWrap);
            final Element descriptionHCardRespModule = //
                    hotelWrap.getElementsByClass("description").get(0);
            name = parseName(descriptionHCardRespModule);
            address = parseAddress(descriptionHCardRespModule);
            final Element detailsRespModule =  //
                    descriptionHCardRespModule.getElementsByClass("details").get(0);
            distance = parseDistance(detailsRespModule);
            rating = parseRating(detailsRespModule);
            imgUrl = parseImgUrl(descriptionHCardRespModule);
        } catch (final IndexOutOfBoundsException e) {
            e.printStackTrace();
            throw new MissingHotelInformationException(e);
        }
    }
    
    @Override
    public String toString() {
        return "Hotel [price=" + price + ", name=" + name + ", address=" + address + ", distance="
                + distance + ", rating=" + rating + ", imgUrl=" + imgUrl + "]";
    }
    
}
