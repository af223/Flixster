package com.example.flixster.models;

import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

@Parcel // marks this class as Parcelable
public class Movie {

    public static String TRAILER = "https://api.themoviedb.org/3/movie/%s/videos?api_key=c2dfc26b69ab046dd5f7e360e6d12265&language=en-US";
    public static final String TAG = "MovieTrailer";

    String backdropPath;
    String posterPath;
    String title;
    String overview;
    Double voteAverage;
    Integer id;
    String key;

    // no-arg constructor for Parceler
    public Movie() {}

    // normal constructor for Movie object
    public Movie(JSONObject jsonObject) throws JSONException {
        backdropPath = jsonObject.getString("backdrop_path");
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        voteAverage = jsonObject.getDouble("vote_average");
        id = jsonObject.getInt("id");
        key = getKey(this.id);
    }

    // parse the returned JSON into Movie
    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < movieJsonArray.length(); i++) {
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    public String getPosterPath() {
        // hardcoding for example
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getId() {
        return ""+id;
    }

    // fast retrieval of key without needing to call API
    public String getKey() {
        return key;
    }

    // get key to use for youtube request
    // only called once when creating a Movie object to improve speed so API calls are made only once per movie
    public String getKey(int movie_id) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(TRAILER, ""+movie_id), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + results.toString());
                    // getting the key. Possible that some movies don't have an associated YouTube trailer
                    // so safety check here
                    if (results.length() > 0) {
                        key = results.getJSONObject(0).getString("key");
                        Log.i(TAG, "Key: " + key);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });

        // possible that some movies don't have associated key for trailer, in which case key is empty
        if (key == null) return "";
        else return key;
    }
}
