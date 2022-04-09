package com.example.medkit2006.boundary;

import static com.example.medkit2006.MainActivity.facilityMgr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.medkit2006.MedicalFacilityAdapter;
import com.example.medkit2006.R;
import com.example.medkit2006.control.MedicalFacilityMgr;
import com.example.medkit2006.data.DB;
import com.example.medkit2006.data.ForumContract;
import com.example.medkit2006.entity.MedicalFacility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchUI extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    public static final String EXTRA_MESSAGE = "@string/MF_name";
    public static final String QUERY = "@string/MF_search";

    RecyclerView recyclerView;
    MedicalFacilityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Setting spinners
        Spinner type_spin = findViewById(R.id.type_filter_spinner);
        Spinner rating_spin = findViewById(R.id.rating_filter_spinner);

        type_spin.setOnItemSelectedListener(this);
        rating_spin.setOnItemSelectedListener(this);

        ArrayAdapter type_ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, facilityMgr.filters_type);
        type_ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_spin.setAdapter(type_ad);
        ArrayAdapter rating_ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, facilityMgr.filters_rating);
        rating_ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rating_spin.setAdapter(rating_ad);

        recyclerView = findViewById(R.id.search_result_rv);
        displayAllFacility();
    }

    public void displayAllFacility(){
        facilityMgr.getAllFacilityAbstract(medicalFacilityList -> {
            Log.d("Received Medical Facility List", String.valueOf(medicalFacilityList.size()));
            runOnUiThread(new Runnable() {
                @Override
                public void run() {// Stuff that updates the UI
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter = new MedicalFacilityAdapter(getApplicationContext(), medicalFacilityList);
                    recyclerView.setAdapter(adapter);
                }
            });
        }, e -> {
            Log.d("Received Medical Facility List Unsuccessful", e.toString().trim());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {// Stuff that updates the UI
                    Toast.makeText(getApplicationContext(), e.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        // Auto-generated method stub
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Auto-generated method stub
    }
    public void toSearchResult(View view){
        //Get user input text
        EditText edx = findViewById(R.id.search_src1);
        String name = edx.getText().toString();
        //Get filters
        int filter_pos[] = new int[2];
        Spinner spn = findViewById(R.id.type_filter_spinner);
        filter_pos[0] = spn.getSelectedItemPosition();
        spn = findViewById(R.id.rating_filter_spinner);
        filter_pos[1] = spn.getSelectedItemPosition();
        //Get user selection of order by
        RadioGroup rgr = findViewById(R.id.sort_radioGroup);
        String order ;
        try {
            RadioButton rbtn = findViewById(rgr.getCheckedRadioButtonId());
            order = rbtn.getText().toString();
        }catch (Exception e){
            order = "";
        }
        facilityMgr.getFacilityList(name, filter_pos, order, medicalFacilityList -> {
            Log.d("Received Medical Facility List", String.valueOf(medicalFacilityList.size()));
            runOnUiThread(new Runnable() {
                @Override
                public void run() {// Stuff that updates the UI
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter = new MedicalFacilityAdapter(getApplicationContext(), medicalFacilityList);
                    recyclerView.setAdapter(adapter);
                }
            });
        }, e -> {
            Log.d("Received Medical Facility List Unsuccessful", e.toString().trim());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {// Stuff that updates the UI
                    Toast.makeText(getApplicationContext(), e.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        //Gather all info and formulate a query.
        //Intent intent = new Intent(SearchUI.this, SearchResultUI.class);
        //startActivity(intent);
    }
}