package com.samsmith.worldofairports;

import android.util.Log;

import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AirportQuery {

    private HashMap<String, Marker> airportsCache;
    private List<Airport> airports;

    private static final String BASE_URL = "https://mikerhodes.cloudant.com/airportdb/_design/view1/_search/geo";
    private static final int MAX_MARKER_COUNT = 150;

    public AirportQuery(HashMap<String, Marker> markerCache) {
        this.airportsCache = markerCache;
        airports = new ArrayList<>();
    }

    public List<Airport> getAirports(LatLngBounds queryBounds) {
        String nextBookmark = null;
        int totalRows = MAX_MARKER_COUNT;
        while ((airportsCache.size() + airports.size()) < Math.min(MAX_MARKER_COUNT, totalRows)) {
            String response = submitQuery(getUrl(queryBounds, nextBookmark));
            try {
                AirportBatch batch = new AirportBatch(response);
                totalRows = batch.getTotalRows();
                nextBookmark = batch.getNextBookmark();
                addAirports(batch.get());
            } catch (Exception e) {
                Log.e("getMarkers", "failed to parse query batch");
                break;
            }
        }
        return airports;
    }

    private void addAirports(List<Airport> newAirports) {
        for (int i=0; i<newAirports.size(); i++) {
            Airport airport = newAirports.get(i);
            if (!airportsCache.containsKey(airport.getId())) {
                airports.add(airport);
            }
        }
    }

    public static String getUrl(LatLngBounds bounds, String nextBookmark) {
        // NE LatLong
        double latNE = bounds.northeast.latitude;
        double lngNE = bounds.northeast.longitude;
        // SW LatLong
        double latSW = bounds.southwest.latitude;
        double lngSW = bounds.southwest.longitude;
        // build url
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_URL);
        sb.append("?q=lon:[");
        sb.append(lngSW);
        sb.append("%20TO%20");
        sb.append(lngNE);
        sb.append("]%20AND%20lat:[");
        sb.append(latSW);
        sb.append("%20TO%20");
        sb.append(latNE);
        sb.append("]");
        if (nextBookmark != null) {
            sb.append("&bookmark=").append(nextBookmark);
        }
        return sb.toString();
    }

    private static String submitQuery(String url) {
        Log.d("submitQuery", "fetching url: " + url);
        StringBuilder stringBuilder = new StringBuilder();
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                inputStream.close();
            } else {
                Log.d("submitQuery", "failed to get query results - status code " + statusCode);
            }
        } catch (Exception e) {
            Log.d("submitQuery", e.getLocalizedMessage());
        }
        return stringBuilder.toString();
    }
}
