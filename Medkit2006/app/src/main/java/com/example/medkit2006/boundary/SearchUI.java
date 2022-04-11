package com.example.medkit2006.boundary;

import static com.example.medkit2006.MainActivity.facilityMgr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medkit2006.MainActivity;
import com.example.medkit2006.adapter.MedicalFacilityAdapter;
import com.example.medkit2006.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class SearchUI extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    public static final String EXTRA_MESSAGE = "@string/MF_name";
    public static final String QUERY = "@string/MF_search";

    RecyclerView recyclerView;
    MedicalFacilityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        BottomNavigationView btmNav = findViewById(R.id.navigation);
        btmNav.getMenu().clear();
        btmNav.inflateMenu(R.menu.bottom_navigation);
        btmNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent i;
                switch (item.getItemId()) {
                    case R.id.nav_search:
                        i = new Intent(getApplicationContext(), SearchUI.class);
                        startActivity(i);
                        break;
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
            }
        });


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
        MainActivity.facilityMgr.getFacilityAbstract(name, filter_pos, order, medicalFacilityList -> {
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
                    Toast.makeText(getApplicationContext(),"No Search Result!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}