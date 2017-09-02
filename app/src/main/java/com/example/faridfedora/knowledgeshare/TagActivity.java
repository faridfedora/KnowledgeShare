package com.example.faridfedora.knowledgeshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class TagActivity extends AppCompatActivity {
        EditText tagEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        tagEditText= (EditText) findViewById(R.id.tag_editText);
        tagEditText.setText(MainApplication.setting.getString("tag","new"));


    }

    @Override
    protected void onPause() {
        super.onPause();
        MainApplication.editor.putString("tag",tagEditText.getText().toString());
        MainApplication.editor.commit();
        Toast.makeText(this, "tag: "+MainApplication.setting.getString("tag","empty"), Toast.LENGTH_SHORT).show();
    }
}
