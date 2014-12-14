package com.samsmith.worldofairports;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Airport {
    private String id, name;
    private double lat, lon;

    public Airport(String id, String name, Double lat, Double lon) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public String getId() {
        return id;
    }

    public MarkerOptions getMarkerOptions() {
        return new MarkerOptions()
                .title(name)
                .position(getLatLong())
                .flat(true);
    }

    private LatLng getLatLong() {
        return new LatLng(lat, lon);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Airport) {
            Airport other = (Airport) o;
            return (id == other.id);
        }
        return false;
    }
}
