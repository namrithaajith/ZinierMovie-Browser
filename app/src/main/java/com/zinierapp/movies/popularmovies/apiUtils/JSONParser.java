package com.zinierapp.movies.popularmovies.apiUtils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zinierapp.movies.popularmovies.models.Movies;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class JSONParser {

    public static List<Movies> getMovieList(String jsonString) throws JSONException {
        JSONObject responseJson;
        Gson gson =  new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

        responseJson = new JSONObject(jsonString);

        return gson.fromJson(responseJson.getJSONArray("results").toString(), new TypeToken<List<Movies>>(){}.getType());

    }
}
