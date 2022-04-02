package com.example.medkit2006;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.medkit2006.control.MedicalFacilityMgr;
import com.example.medkit2006.entity.MedicalFacility;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    public static final String EXTRA_MESSAGE = "@string/MF_name";
    public static final int NUMBER_OF_DISPLAY = 30;
    String[] filters = {"Rating>3.5", "Distance<10km"};
    private MedicalFacilityMgr mf_mgr = new MedicalFacilityMgr();
    private Button[] display_btns = new Button[30];

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

        //3. Settings of RecyclerView
        LinearLayout ll = findViewById(R.id.search_result_ll);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //Use Button to display medical facilities, on click to go to facility detail page
        mf_mgr.getFacilityList();
        for(int i = 0; i < NUMBER_OF_DISPLAY; i++) {
            display_btns[i] = new Button(this);
            display_btns[i].setText("@string/MF_name");
            ll.addView(display_btns[i], lp);
            //using lambda to initialize onclick operations
            display_btns[i].setOnClickListener(v -> {
                Button btn = (Button) v;
                Log.d("success:", "button has just been found");
                Intent intent = new Intent(btn.getContext(), FacilDetailActivity.class);
                String message = btn.getText().toString();
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
            });
        }
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

        MedicalFacility[] results = mf_mgr.getFacilityList(user_str, filters[position], mode);
        if(results == null){//generate warning use toaster
            Toast.makeText(getApplicationContext(), "@string/no_result_warning", Toast.LENGTH_LONG).show();
        }
        else{//change result list display
            for(int i = 0; i < 30; i++) {
                display_btns[i] = new Button(this);
                display_btns[i].setText("@string/MF_name");
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getApplicationContext(), "@string/search_ord_hint"+filters[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Auto-generated method stub
    }
}