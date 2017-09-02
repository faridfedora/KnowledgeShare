package com.example.faridfedora.knowledgeshare;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by faridfedora on 5/26/17.
 */
public class Server {
    private static Server ourInstance = new Server();
    public static RequestQueue queue;
    //// TODO: 6/8/17  move all web services in here ASAP...!
    public static Server getInstance(Context context) {
        queue= Volley.newRequestQueue(context);
        return ourInstance;
    }

    private Server() {

    }






    public void convertPostsJsonToJavaModelAndViewInRecycler(Response.Listener listener, final String tagName){



        StringRequest stringRequest=new StringRequest(Request.Method.POST,Static.URL_GET_POSTS_PHP,listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String,String>();
                map.put("tag",tagName);
                return map;
            }
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String,String> map = new HashMap<String, String>();
//                map.put("Content-Type","application/x-www-form-urlencoded");
//                return map;
//            }
        };



        queue.add(stringRequest);
    }














}
