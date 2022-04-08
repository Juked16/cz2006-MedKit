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
import com.example.medkit2006.entity.MedicalFacility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchUI extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    public static final String EXTRA_MESSAGE = "@string/MF_name";

    RecyclerView recyclerView;
    MedicalFacilityAdapter adapter;
    private final String[] filters = {"Rating>3.5", "Distance<10km"};
    private ArrayList<MedicalFacility> medicalFacilityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //1. Basic Settings
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Spinner spin = findViewById(R.id.filter_spinner);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, filters);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(ad);
        medicalFacilityList= new ArrayList<MedicalFacility>();
        recyclerView = findViewById(R.id.search_result_rv);

        facilityMgr.getAllFacilityList(medicalFacilityList -> {
            Log.d("Received Medical Facility List", String.valueOf(medicalFacilityList.size()));
            runOnUiThread(new Runnable() {
                @Override
                public void run() {// Stuff that updates the UI
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter = new MedicalFacilityAdapter(getApplicationContext(), medicalFacilityList);
                    recyclerView.setAdapter(adapter);
                }
            });
            try{
                Toast.makeText(SearchUI.this, "success", Toast.LENGTH_SHORT).show();
            } catch(Exception e){
                Looper.prepare();
                Toast.makeText(SearchUI.this, "Toast", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }, e -> {
            Log.d("Received Medical Facility List Unsuccessful", e.toString().trim());
            try {
                Toast.makeText(SearchUI.this, e.toString().trim(), Toast.LENGTH_SHORT).show();
            } catch(Exception e2){
                Looper.prepare();
                Toast.makeText(SearchUI.this, "Toast", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        });
    }

    public void searchHandler(View v) {
        //Get user input text
        EditText edx = findViewById(R.id.search_src1);
        String user_str = edx.getText().toString();
        //Get filters
        Spinner spn = findViewById(R.id.filter_spinner);
        int position = spn.getSelectedItemPosition();

        //Get user selection of order by
        RadioGroup rgr = findViewById(R.id.sort_radioGroup);
        int mode = rgr.getCheckedRadioButtonId();
        //Gather all info and formulate a query.
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getApplicationContext(), "@string/search_ord_hint"+filters[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Auto-generated method stub
    }
    public void toSearchResult(View view){
        Intent intent = new Intent(SearchUI.this, SearchResultUI.class);
        startActivity(intent);
    }
}