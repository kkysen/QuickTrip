package io.github.kkysen.quicktrip.app;

import java.util.List;

import lombok.Getter;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * 
 * 
 * @author Khyber Sen
 */
@Getter
public class ItineraryScreenView {
    
    private final GridPane grid = new GridPane();
    private final GridRows rows = new GridRows(grid);
    
    public ItineraryScreenView() {
        
    }
    
    private void addDestination(final Destination dest) {
        final Hotel hotel = dest.getHotel();
        final String imgUrl = hotel.getImgUrl();
        Node img;
        if (imgUrl.isEmpty()) {
            img = new Text("No Image Found");
        } else {
            img = new ImageView(imgUrl);
        }
        final Label name = new Label(hotel.getName());
        final Label address = new Label(hotel.getAddress());
        final Label numDays = new Label("(" + dest.getNumDays() + " days)");
        rows.add(img, name);
        rows.add(address, numDays);
        rows.add(new Label());
    }
    
    public void addDestinations(final List<Destination> dests) {
        dests.forEach(this::addDestination);
    }
    
}
