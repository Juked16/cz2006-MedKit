package com.example.medkit2006.boundary;

import static com.example.medkit2006.MainActivity.facilityMgr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medkit2006.MainActivity;
import com.example.medkit2006.R;
import com.example.medkit2006.adapter.MedicalFacilityAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchUI extends AppCompatActivity{
    public static final String EXTRA_MESSAGE = "@string/MF_name";

    RecyclerView recyclerView;
    MedicalFacilityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        BottomNavigationView btmNav = findViewById(R.id.navigation);
        btmNav.getMenu().clear();
        btmNav.inflateMenu(R.menu.bottom_navigation);
        btmNav.setSelectedItemId(R.id.nav_search);
        btmNav.setOnItemSelectedListener(item -> {
            Intent i;
            switch (item.getItemId()) {
                case R.id.nav_forum:
                    i = new Intent(getApplicationContext(), ForumUI.class);
                    startActivity(i);
                    break;
                case R.id.nav_account:
                    i = new Intent(getApplicationContext(), MainActivity.accountMgr.isLoggedIn()? AccountUI.class: LoginUI.class);
                    startActivity(i);
                    break;
            }
            return false;
        });

        //Setting spinners
        Spinner type_spin = findViewById(R.id.type_filter_spinner);
        Spinner rating_spin = findViewById(R.id.rating_filter_spinner);

        AdapterView.OnItemSelectedListener update = new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toSearchResult(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                toSearchResult(null);
            }
        };

        type_spin.setOnItemSelectedListener(update);
        rating_spin.setOnItemSelectedListener(update);

        RadioGroup rgr = findViewById(R.id.sort_radioGroup);

        rgr.setOnCheckedChangeListener((radioGroup, i) -> toSearchResult(null));

        ArrayAdapter<String> type_ad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, facilityMgr.filters_type);
        type_ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_spin.setAdapter(type_ad);
        ArrayAdapter<String> rating_ad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, facilityMgr.filters_rating);
        rating_ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rating_spin.setAdapter(rating_ad);

        recyclerView = findViewById(R.id.search_result_rv);
        displayAllFacility();
    }

    public void displayAllFacility(){
        facilityMgr.getAllFacilityAbstract(medicalFacilityList -> {
            Log.d("Received Medical Facility List", String.valueOf(medicalFacilityList.size()));
            runOnUiThread(() -> {// Stuff that updates the UI
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter = new MedicalFacilityAdapter(getApplicationContext(), medicalFacilityList);
                recyclerView.setAdapter(adapter);
                facilityMgr.all_facility_names = new String[medicalFacilityList.size()];
                for(int i =0; i<medicalFacilityList.size();i++){
                    facilityMgr.all_facility_names[i]=medicalFacilityList.get(i).getName();
                }
                Log.d("Medical Facility Name Size ",String.valueOf(facilityMgr.all_facility_names.length));
            });
        }, e -> {
            Log.d("Received Medical Facility List Unsuccessful", e.toString().trim());
            runOnUiThread(() -> {// Stuff that updates the UI
                Toast.makeText(getApplicationContext(), e.toString().trim(), Toast.LENGTH_SHORT).show();
            });
        });
    }

    public void toSearchResult(View view){
        //Get user input text
        EditText edx = findViewById(R.id.search_src1);
        String name = edx.getText().toString();
        //Get filters
        int[] filter_pos = new int[2];
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
        //prevent spamming leading to crash
        findViewById(R.id.toMfList2).setEnabled(false);
        findViewById(R.id.type_filter_spinner).setEnabled(false);
        spn.setEnabled(false);
        rgr.setEnabled(false);
        MainActivity.facilityMgr.getFacilityAbstract(name, filter_pos, order, medicalFacilityList -> {
            Log.d("Received Medical Facility List", String.valueOf(medicalFacilityList.size()));
            runOnUiThread(() -> {// Stuff that updates the UI
                //prevent spamming leading to crash
                findViewById(R.id.toMfList2).setEnabled(true);
                findViewById(R.id.type_filter_spinner).setEnabled(true);
                findViewById(R.id.rating_filter_spinner).setEnabled(true);
                rgr.setEnabled(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter = new MedicalFacilityAdapter(getApplicationContext(), medicalFacilityList);
                recyclerView.setAdapter(adapter);
            });
        }, e -> {
            Log.d("Received Medical Facility List Unsuccessful", e.toString().trim());
            runOnUiThread(() -> {// Stuff that updates the UI
                //prevent spamming leading to crash
                findViewById(R.id.toMfList2).setEnabled(true);
                findViewById(R.id.type_filter_spinner).setEnabled(true);
                findViewById(R.id.rating_filter_spinner).setEnabled(true);
                rgr.setEnabled(true);
                Toast.makeText(getApplicationContext(),"No Search Result!", Toast.LENGTH_SHORT).show();
            });
        });
    }
}