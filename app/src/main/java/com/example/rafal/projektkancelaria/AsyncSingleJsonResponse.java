package com.example.rafal.projektkancelaria;

import org.json.JSONException;
import org.json.JSONTokener;

/**
 * Created by rafal on 14.06.2016.
 */
public interface AsyncSingleJsonResponse {


    void processJSONFinish(JSONTokener output) throws JSONException;

}
