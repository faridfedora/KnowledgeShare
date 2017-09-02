package com.example.faridfedora.knowledgeshare;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PostActivity extends AppCompatActivity {

    private TextView subjectTV;
    private TextView textTV;
    private TextView tagTV;
    private TextView scoreTextView;
    private Button plusOneButton;
    private Button minusOneButton;
    SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        subjectTV= (TextView) findViewById(R.id.subjectTV);
        textTV= (TextView) findViewById(R.id.textTV);
        tagTV= (TextView) findViewById(R.id.tagTV);
        scoreTextView= (TextView) findViewById(R.id.score_textView);
        plusOneButton= (Button) findViewById(R.id.plus_one_button);
        minusOneButton= (Button) findViewById(R.id.minus_one_button);
        settings=getSharedPreferences("sh",MODE_PRIVATE);
        final String userName=settings.getString("userName","err");


        final RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        getPostByID(requestQueue);


        plusOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                plusOnePost(userName, requestQueue);
            }


        });



    }

    private void plusOnePost(final String userName, RequestQueue requestQueue) {
        StringRequest jsonObjectRequest=new StringRequest(Request.Method.POST, Static.URL_PLUS_ONE_POST_PHP, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {


                try {
                    JSONObject response=new JSONObject(res);
                    if(response.getInt("result")==1){
                        int score= Integer.parseInt(scoreTextView.getText().toString())+1;
                        scoreTextView.setText("+"+score);
                    }
                    Toast.makeText(PostActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("plusErr",error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String, String>();
                map.put("userName",userName);
                map.put("id", String.valueOf(getIntent().getIntExtra("id",-1)));
                return map;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void getPostByID(RequestQueue requestQueue) {
        String url = "http://faridheydari.ir/webService/getPostByID.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TAG", "onResponse: "+response);
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    subjectTV.setText(jsonObject.getString("subject"));
                    textTV.setText(jsonObject.getString("text"));
                    tagTV.setText(jsonObject.getString("tag"));
                    scoreTextView.setText("+"+jsonObject.getString("score"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map= new HashMap<String,String>();
                map.put("id",String.valueOf(getIntent().getIntExtra("id",-1)));

                return map;
            }
        };

        requestQueue.add(stringRequest);
    }
}
