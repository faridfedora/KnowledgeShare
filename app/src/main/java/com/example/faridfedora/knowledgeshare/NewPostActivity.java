package com.example.faridfedora.knowledgeshare;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewPostActivity extends AppCompatActivity {


    SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        ImageButton submitImageButton= (ImageButton) findViewById(R.id.submitBtn);
        final EditText subjectET= (EditText) findViewById(R.id.subjectET);
        final EditText textET= (EditText) findViewById(R.id.textET);
        final EditText tagET= (EditText) findViewById(R.id.tagET);

        settings=getSharedPreferences("sh",MODE_PRIVATE);
        final String userName=settings.getString("userName","err");

        final RequestQueue queue= Volley.newRequestQueue(this);

        submitImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tags = tagET.getText().toString();
//                String[] tagsList=tags.split("#");
//                ArrayList<String> tagArrayList=new ArrayList<String>();
//                String tagsSerialized;
//                for(int i=0;i<tagsList.length;i++){
//                    tagsSerialized=
//                }

                insertNewPost(queue,userName,subjectET.getText().toString(),textET.getText().toString(), tags);

            }
        });
    }





    private void insertNewPost(RequestQueue requestQueue, final String userName, final String subject, final String text, final String tag) {
//        JSONObject paramJsonObject=new JSONObject();
//        try {
//            paramJsonObject.put("userName",emailEditText.getText().toString());
//            paramJsonObject.put("password",passwordEditText.getText().toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        Log.d("json",paramJsonObject.toString());
        StringRequest jsonObjectRequest=new StringRequest(Request.Method.POST, Static.URL_INSERT_POST_PHP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject responseJsonObject=new JSONObject(response);
                    if(responseJsonObject.getInt("result")==1){
                        Toast.makeText(NewPostActivity.this, responseJsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(NewPostActivity.this, responseJsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        Log.e("insertPost",responseJsonObject.getString("error"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("login", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String,String>();
                map.put("userName",userName);
                map.put("subject",subject);
                map.put("text",text);
                map.put("tag",tag);
                return map;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }










}
