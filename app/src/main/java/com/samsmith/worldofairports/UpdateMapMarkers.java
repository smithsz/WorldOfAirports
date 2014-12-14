package com.samsmith.worldofairports;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UpdateMapMarkers extends AsyncTask<String, Void, List<Airport>> {

    private GoogleMap mapIn;
    private HashMap<String, Marker> markersCache;
    private ProgressDialog Dialog;
    private LatLngBounds queryBounds;

    public UpdateMapMarkers(Context context, GoogleMap map, HashMap<String, Marker> currentMarkers) {
        mapIn = map;
        markersCache = currentMarkers;
        Dialog = new ProgressDialog(context);
    }

    @Override
    protected List<Airport> doInBackground(String... arg0) {
        AirportQuery query = new AirportQuery(markersCache);
        return query.getAirports(queryBounds);
    }

    protected void onPreExecute() {
            Dialog.setMessage("Getting airport data...");
            Dialog.setTitle("Loading");
            Dialog.setCancelable(false);
            Dialog.show();
            queryBounds = mapIn.getProjection().getVisibleRegion().latLngBounds;
            clearOffScreenMarkers(queryBounds);
    }

    @Override
    protected void onPostExecute(List<Airport> newAirports){
        for (int i=0; i<newAirports.size(); i++) {
            Airport airport = newAirports.get(i);
            markersCache.put(airport.getId(), mapIn.addMarker(airport.getMarkerOptions()));
        }
        // hide spinner
        Dialog.dismiss();
    }

    private void clearOffScreenMarkers(LatLngBounds bounds) {
        if (!markersCache.isEmpty()) {
            Iterator<Map.Entry<String, Marker>> iter = markersCache.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Marker> entry = iter.next();
                // if the item is not within the the queryBounds of the screen
                Marker marker = entry.getValue();
                if (!bounds.contains(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude))) {
                    marker.remove();
                    iter.remove();
                }
            }
        }
    }
}
