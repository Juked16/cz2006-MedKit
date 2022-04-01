package com.example.medkit2006;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.medkit2006.control.MedicalFacilityMgr;

public class SearchActivity extends AppCompatActivity {
//TODO - implement dropdown list
    private MedicalFacilityMgr facil_mgr = new MedicalFacilityMgr();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //TODO: logic to get search history and return as a list Medical Facil List

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        //String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        // TextView textView = findViewById(R.id.medfacil_name);
        //textView.setText(message);

    }
    public void searchHandler(View v) {
        EditText edx = (EditText)findViewById(R.id.search_src1);
        String user_str = edx.getText().toString();

        //TODO: MedicalFacilityMgr performs the search function.

        //Search function returns a list of medical facilities.
        //If there is no any result, alarm user with toaster
    }


}