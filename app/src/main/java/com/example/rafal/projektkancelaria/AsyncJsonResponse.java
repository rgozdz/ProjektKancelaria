package com.example.rafal.projektkancelaria;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by rafal on 10.06.2016.
 */
public interface AsyncJsonResponse {


    void processFinishJSON(JSONArray output) throws JSONException;
}
