package com.example.faridfedora.knowledgeshare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText;
    EditText passwordEditText;
    Button loginButton;
    Button registerFormButton;
//    SharedPreferences setting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEditText= (EditText) findViewById(R.id.email);
        passwordEditText= (EditText) findViewById(R.id.password);
        loginButton= (Button) findViewById(R.id.sign_in_button);
        registerFormButton= (Button) findViewById(R.id.register_form_button);
//        setting=getSharedPreferences("sh",MODE_PRIVATE);

        final RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWebService(requestQueue);

            }
        });
        registerFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });



    }

    private void loginWebService(RequestQueue requestQueue) {
//        JSONObject paramJsonObject=new JSONObject();
//        try {
//            paramJsonObject.put("userName",emailEditText.getText().toString());
//            paramJsonObject.put("password",passwordEditText.getText().toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        Log.d("json",paramJsonObject.toString());
        StringRequest jsonObjectRequest=new StringRequest(Request.Method.POST, "http://faridheydari.ir/webService/login.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject responseJsonObject=new JSONObject(response);
                    if(responseJsonObject.getInt("result")==1){
//                        SharedPreferences.Editor editor=MainApplication.setting.edit();
                        MainApplication.editor.putString("userName",emailEditText.getText().toString());
                        MainApplication.editor.commit();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }
                    Toast.makeText(LoginActivity.this, responseJsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                map.put("userName",emailEditText.getText().toString());
                map.put("password",passwordEditText.getText().toString());
                return map;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}
