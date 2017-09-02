package com.example.faridfedora.knowledgeshare;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private ArrayList<PostModel> postModelList=new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String tag;
    private TextView headerUserNameTextView;
    private TextView headerTitleTextView;
    Server server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        server=Server.getInstance(this);
        if(MainApplication.setting.getString("userName","err").equalsIgnoreCase("err")){
            //starts login page:
//            Toast.makeText(this, "userName : "+MainApplication.setting.getString("userName","err"), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,NewPostActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header=navigationView.getHeaderView(0);
        headerUserNameTextView = (TextView) header.findViewById(R.id.nav_header_textView);
        headerTitleTextView = (TextView) header.findViewById(R.id.header_title_textView);
//        headerUserNameTextView.setText(MainApplication.setting.getString("userName",""));
        headerUserNameTextView.setText(MainApplication.setting.getString("userName",""));




        recyclerView= (RecyclerView) findViewById(R.id.postRecyclerView);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipe_to_refreshLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                convertPostsJsonToJavaModelAndViewInRecycler(tag);
            }
        });

    }





    private void convertPostsJsonToJavaModelAndViewInRecycler(final String tagName){

        server.convertPostsJsonToJavaModelAndViewInRecycler(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Gson gson=new Gson();
                Type type = new TypeToken<List<PostModel>>() {}.getType();

                //converting json data to MovieModel:
                postModelList=gson.fromJson(response.toString(), type);
                recyclerView.setAdapter(new PostRecyclerView(postModelList, new RecyclerViewClickListener() {
                    @Override
                    public void recyclerViewItemClicked(View v, int id) {
                        Intent intent = new Intent(MainActivity.this, PostActivity.class);
                        intent.putExtra("id",id);
                        startActivity(intent);
                    }
                }));

                swipeRefreshLayout.setRefreshing(false);

            }
        },tagName);


    }

    private void getUsersTotalScore(){

        RequestQueue queue= Volley.newRequestQueue(this);

        StringRequest stringRequest=new StringRequest(Request.Method.POST,"http://faridheydari.ir/webService/getUsersTotalScore.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int score= Integer.parseInt(response.replace("\n",""));
                headerTitleTextView.setText("اپ تسهیم دانش" +" ("+score+"+)");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                headerTitleTextView.setText(headerTitleTextView.getText().toString() +" (?+)");

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String,String>();
                map.put("userName",MainApplication.setting.getString("userName",""));
                return map;
            }

        };



        queue.add(stringRequest);
    }


    @Override
    protected void onResume() {
        super.onResume();
        tag = MainApplication.setting.getString("tag","new").replace("#","");
        convertPostsJsonToJavaModelAndViewInRecycler(tag);
        getUsersTotalScore();
//        Toast.makeText(this, "tag: "+tag, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.feed) {
            // Handle the camera action
            startActivity(new Intent(getApplicationContext(),TagActivity.class));

        } else if (id == R.id.setting) {

        } else if (id == R.id.about) {

        } else if (id == R.id.exit) {
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
