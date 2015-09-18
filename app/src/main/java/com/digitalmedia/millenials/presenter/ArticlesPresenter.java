package com.digitalmedia.millenials.presenter;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.digitalmedia.millenials.utils.VolleyQueue;

import org.json.JSONObject;

import static com.android.volley.Request.Method.GET;

/**
 * Created by Mark on 8/18/2015.
 */
public class ArticlesPresenter {

    Context context;

    public ArticlesPresenter(Context context) {
        this.context = context;
    }

    public void getArticles(){
        StringRequest jsonObjectRequest = new StringRequest(GET, "http://10.0.2.106/services/test.json",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Test", response);
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
