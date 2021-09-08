package com.basicbug.bikini.feed.model;

import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Embeddable
@Getter
@NoArgsConstructor
public class Location {

    private double latitude;
    private double longitude;
    private String locationName = "";

    public Location(Point point) {
        this.latitude = point.getLatitude();
        this.longitude = point.getLongitude();
    }
}
