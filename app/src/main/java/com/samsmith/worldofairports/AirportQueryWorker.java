package com.samsmith.worldofairports;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Processes result batches and returns a list of Airport objects.
 */

public class AirportQueryWorker {

    private List<Airport> airports;
    private int totalRows;
    private String nextBookmark;

    public AirportQueryWorker(String queryResults) throws JSONException{
        airports = new ArrayList<>();
        processResults(queryResults);
    }

    /** Returns a list of Airport objects. */
    public List<Airport> get() {
        return airports;
    }

    /** Returns the total number of results for the entire query. */
    public int getTotalRows() {
        return totalRows;
    }

    /** Returns the bookmark required to fetch the next results batch. */
    public String getNextBookmark() {
        return nextBookmark;
    }

    private void processResults(String queryResults) throws JSONException {
        JSONObject jResults;
        JSONArray jAirports;
        try {
            jResults = new JSONObject(queryResults);
            jAirports = jResults.getJSONArray("rows");
            totalRows = Integer.parseInt(jResults.getString("total_rows"));
            nextBookmark = jResults.getString("bookmark");
        } catch (JSONException e) {
            Log.d("processResults", "failed to parse query results");
            throw e;
        }
        for (int i = 0; i < jAirports.length(); i++) {
            try {
                JSONObject jAirport = jAirports.getJSONObject(i);
                String id = jAirport.getString("id");
                String name = jAirport.getJSONObject("fields").getString("name");
                Double lat = Double.parseDouble(jAirport.getJSONObject("fields").getString("lat"));
                Double lon = Double.parseDouble(jAirport.getJSONObject("fields").getString("lon"));
                airports.add(new Airport(id, name, lat, lon));
            } catch (NumberFormatException e) {
                Log.d("processResults", "failed to parse airport position");
                // skip result
            } catch (JSONException e) {
                Log.d("processResults", "failed to parse airport result");
                // skip result
            }
        }
    }
}
