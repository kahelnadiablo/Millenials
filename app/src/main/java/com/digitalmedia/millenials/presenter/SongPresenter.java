package com.digitalmedia.millenials.presenter;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.digitalmedia.millenials.MainActivity;
import com.digitalmedia.millenials.model.Song;
import com.digitalmedia.millenials.utils.VolleyQueue;
import com.google.gson.Gson;

import org.json.JSONObject;

import static com.android.volley.Request.Method.GET;

/**
 * Created by Mark on 8/18/2015.
 */
public class SongPresenter {

    MainActivity view;
    Context context;

    public SongPresenter(Context context, MainActivity view) {
        this.context = context;
        this.view = view;
    }

    public void getSong(){
        StringRequest jsonObjectRequest = new StringRequest(GET, "http://10.0.2.106/services/smells_like.json",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Test", response);
                        Song song;
                        Gson gson = new Gson();
                        song = gson.fromJson(response.toString(), Song.class);
                        view.DisplaySongInformation(song);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                    Log.e("Response", "Error");

            }
        });

        // Access the RequestQueue through your singleton class.
        VolleyQueue.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }
}
