package com.example.medkit2006.boundary;

import android.content.Intent;
import android.os.Bundle;
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
    String[] filters = {"Rating>3.5", "Distance<10km"};
    MedicalFacilityAdapter adapter;
    ArrayList<MedicalFacility> medicalFacilityList;

    private String stContact;
    private final String URL = "http://159.138.106.155/mf.php";

    //private CardView[] display_cards = new CardView[30];
    //private Button[] display_btns = new Button[30];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //1. Basic Settings
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //2. Settings of Spinner
        // Take the instance of Spinner and apply OnItemSelectedListener on it which tells which item of spinner is clicked
        Spinner spin = findViewById(R.id.filter_spinner);
        spin.setOnItemSelectedListener(this);
        // Create the instance of ArrayAdapter having the list of filters
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, filters);
        // set simple layout resource file for each item of spinner
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Set the ArrayAdapter (ad) data on the Spinner which binds data to spinner
        spin.setAdapter(ad);

        //3. Settings of LayOut
        recyclerView = findViewById(R.id.search_result_rv);
        medicalFacilityList = new ArrayList<>();
        extractMedicalFacility();
    }

    public void extractMedicalFacility(){
        //instantiate request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for(int i =0; i <response.length(); i++){
                    try {
                        JSONObject mfDetail = response.getJSONObject(i);

                        MedicalFacility medicalFacility = new MedicalFacility();
                        medicalFacility.setName(mfDetail.getString("name").toString());
                        medicalFacility.setContact(mfDetail.getString("contact"));
                        medicalFacility.setType(mfDetail.getString("type"));
                        medicalFacility.setAddress(mfDetail.getString("address"));
                        medicalFacilityList.add(medicalFacility);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter = new MedicalFacilityAdapter(getApplicationContext(),medicalFacilityList);
                recyclerView.setAdapter(adapter);

                Toast.makeText(SearchUI.this,"success", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchUI.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> data=new HashMap<>();
                data.put("contact",stContact);
                return data;
            }
        };
        requestQueue.add(jsonArrayRequest);
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
        //TODO: get the search result and pass to search result page? or pass the search parameters?
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