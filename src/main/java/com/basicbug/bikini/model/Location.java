package com.basicbug.bikini.model;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Embeddable
@Getter
@NoArgsConstructor
public class Location {

    @Embedded
    private Point point;

    private String locationName = "";

    public Location(Point point) {
        this.point = point;
    }
}
