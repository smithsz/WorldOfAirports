package com.samsmith.worldofairports;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by samsmith on 14/12/14.
 */
public class Airport {
    private String id, name;
    private double lat, lon;

    Airport(String airportId, String airportName, Double airportLat, Double airportLon) {
        this.id = airportId;
        this.name = airportName;
        this.lat = airportLat;
        this.lon = airportLon;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public LatLng getLatLong() {
        return new LatLng(this.lat, this.lon);
    }
}
