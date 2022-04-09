package com.example.medkit2006;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.medkit2006.control.AccountMgr;
import com.example.medkit2006.control.ChatMgr;
import com.example.medkit2006.entity.MedicalFacility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MedicalFacilityAdapter adapter;
    ArrayList<MedicalFacility> medicalFacilityList;

    private TextView txtMFName,txtMFContact,txtMFType,txtMFAddress;
    private TextView test;
    private String stContact;
    private String URL = "http://159.138.106.155/mf.php";
    public static AccountMgr accountMgr = new AccountMgr();
    public static ChatMgr chatMgr = new ChatMgr();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stContact="";
        test = findViewById(R.id.testResult);
        txtMFName = (TextView) findViewById(R.id.txtMedicalFacilityName);
        txtMFType = (TextView)findViewById(R.id.txtMedicalFacilityType);
        txtMFAddress = (TextView)findViewById(R.id.txtMedicalFacilityAddress);
        txtMFContact = (TextView)findViewById(R.id.txtMedicalFacilityContact);

        //Button btnTest = findViewById(R.id.testButton);
        /**
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMF);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadMF();

        adapter = new MedicalFacilityAdapter(this, medicalFacilityList);
        recyclerView.setAdapter(adapter);
        */

    }
    public void loadMF(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray mfList = new JSONArray(response);
                            for(int i =0; i<mfList.length(); i++){
                                JSONObject mfObject = mfList.getJSONObject(i);
                                String name = mfObject.getString("name");
                                String type = mfObject.getString("type");
                                String address = mfObject.getString("address");
                                String contact = mfObject.getString("contact");
                                //double rating = mfObject.getDouble("rating");
                               //Constructor
                                MedicalFacility medicalFacility = new MedicalFacility(name);
                                medicalFacility.setType(type);
                                medicalFacility.setAddress(address);
                                medicalFacility.setContact(contact);

                                medicalFacilityList.add(medicalFacility);
                            }
                            adapter = new MedicalFacilityAdapter(MainActivity.this, medicalFacilityList);
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                }
                });

        Volley.newRequestQueue(this).add(stringRequest);

    }


    public void onClickTest (View view){
        stContact= "64722000";
        test.setText("getting test result");
        Toast.makeText(MainActivity.this, "test toast", Toast.LENGTH_SHORT).show();
        if(!stContact.equals("")){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    txtMFAddress.setText(response.toString());
                    txtMFName.setText("success");
                    try {
                        JSONArray mfList = new JSONArray(response);
                        JSONObject mfDetail = mfList.getJSONObject(0);

                        txtMFName.setText(mfDetail.getString("name"));
                        txtMFType.setText(mfDetail.getString("type"));
                        txtMFAddress.setText(mfDetail.getString("address"));
                        txtMFContact.setText( mfDetail.getString("contact"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                        Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                    }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                    txtMFName.setText("failed");
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
            //instantiate request queue
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);

        }
        else{
            txtMFName.setText("failedd");
            Toast.makeText(this, "string cannot be empty",Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "end of class",Toast.LENGTH_SHORT).show();

    }

}