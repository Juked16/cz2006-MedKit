package com.example.medkit2006;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.medkit2006.entity.MedicalFacility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchResultActivity extends AppCompatActivity{
    RecyclerView recyclerView;
    MedicalFacilityAdapter adapter;
    ArrayList<MedicalFacility> medicalFacilityList;

    private CardView cardView;
    private TextView txtMFName,txtMFContact,txtMFType,txtMFAddress;
    private TextView test;
    private String stContact;
    private String URL = "http://159.138.106.155/mf.php";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search);

        recyclerView = findViewById(R.id.mfListRecyclerV);
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

                Toast.makeText(SearchResultActivity.this,"success", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchResultActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
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

    public void toMFDetails(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

}
