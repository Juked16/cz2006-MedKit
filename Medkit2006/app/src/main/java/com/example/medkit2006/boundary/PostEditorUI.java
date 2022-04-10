package com.example.medkit2006.boundary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.example.medkit2006.MainActivity;
import com.example.medkit2006.R;

public class PostEditorUI extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private String[] medical_facility_names = new String[150];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_editor);

        Spinner spn = findViewById(R.id.post_facility_selection_spinner);
        spn.setOnItemSelectedListener(this);
        ArrayAdapter ad = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, MainActivity.facilityMgr.all_facility_names);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(ad);
        /*MainActivity.facilityMgr.getAllFacilityName(names->{
            this.medical_facility_names = names;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ArrayAdapter ad = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, names);
                    ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spn.setAdapter(ad);
                }
            });
        },e->{
            Log.d("Post Spinner Set Failed",e.toString().trim());
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                insertData(1);
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_draft:
                // Do nothing for now
                insertData(0);
                finish();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void insertData(int status)
    {
        EditText titleView = findViewById(R.id.title);
        EditText content = findViewById(R.id.post);
        EditText tags = findViewById(R.id.tags);
        RatingBar ratingbar = findViewById(R.id.ratingBar);
        Spinner med_spinner = findViewById(R.id.post_facility_selection_spinner);

        MainActivity.forumMgr.addPost(titleView.getText().toString().trim(),//title
            content.getText().toString().trim(),//content
            getUser(), //username
            MainActivity.facilityMgr.all_facility_names[med_spinner.getSelectedItemPosition()],//medical_mecility
            tags.getText().toString().toLowerCase().trim(),//tags
            status,//status
            ratingbar.getNumStars(),//ratings
            ()->{
                runOnUiThread(new Runnable() {
                @Override
                public void run() {// Stuff that updates the UI
                    Toast.makeText(getApplicationContext(), "Posted!", Toast.LENGTH_SHORT).show();
                }}); },//callback
            e-> {
                Log.d("Add Post Fail", e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {// Stuff that updates the UI
                        Toast.makeText(getApplicationContext(), e.toString().trim(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        );
    }

    public String getUser()
    {
        Intent i = getIntent();
        String name = i.getStringExtra(ForumUI.USEREXTRA);
        return name;
    }
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        // Auto-generated method stub
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Auto-generated method stub
    }
}
