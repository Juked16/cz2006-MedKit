package com.example.medkit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //GetMedicalFacilityList
        //for() items in the list, create a btn

        //Demo
        LinearLayout ll = (LinearLayout)findViewById(R.id.main_linear_layout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        /*Button text = new Button(this);
        text.setText("text here");
        ll.addView(text);
        text.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                int id = arg0.getId(); // you get ID of your dynamic button
                Toast.makeText(getApplicationContext(), "Dynamic textview!", Toast.LENGTH_SHORT).show();
            }
        });*/


        Button btns[] = new Button[50];
        for(int i = 0; i < 30; i++) {
            btns[i] = new Button(this);
            btns[i].setText("Medical Facility");
            ll.addView(btns[i], lp);
            btns[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button btn = (Button) v;
                    Log.d("success:", "button has just been found");
                    Intent intent = new Intent(btn.getContext(), FacilDetailActivity.class);
                    String message = btn.getText().toString();
                    intent.putExtra(EXTRA_MESSAGE, message);
                    startActivity(intent);
                }
            });
        }
    }

    /**
     * triggered when the main search button is pressed, jump to activity_search
     */

    public void sendSearchMsg(View v){
        //TODO- 1. Find icon for arrow; 2.
        Intent intent = new Intent(this, SearchActivity.class);
        EditText editText = (EditText) findViewById(R.id.main_search_txt);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    /**
     * triggered when the main_medfacil button is pressed, jump to activity_displayfacil
     */
    /*View.OnClickListener handleOnClick(final Button button) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                Button btn = (Button) findViewById(R.id.editTextTextPersonName);
                Intent intent = new Intent(v.getContext(), FacilDetailActivity.class);
                Button medfacil = (Button) findViewById(v.getId());
                String message = medfacil.getText().toString();
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        };
    }*/
}