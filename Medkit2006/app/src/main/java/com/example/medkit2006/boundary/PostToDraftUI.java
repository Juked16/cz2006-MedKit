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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PostToDraftUI extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private int post_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_to_draft);

        Intent i = getIntent();
        post_id = i.getIntExtra(ForumUI.EXTRA, -1);
        EditText title = findViewById(R.id.title);
        EditText content = findViewById(R.id.post);
        EditText tags = findViewById(R.id.tags);

        Spinner mSpinner = findViewById(R.id.post_facility_selection_spinner);
        mSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, MainActivity.facilityMgr.all_facility_names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        if(post_id == -1)
            Toast.makeText(this, "Can't find post!", Toast.LENGTH_SHORT).show();
        else{
        MainActivity.forumMgr.getPostDetail(post_id, post-> runOnUiThread(() -> {
            title.setText(post.getTitle());
            content.setText(post.getContent());
            tags.setText(post.getTags());
            int spinnerPosition = adapter.getPosition(post.getFacility());
            mSpinner.setSelection(spinnerPosition);
        }), error-> Log.d("PostToDraftUI",error.getMessage()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.draft_post_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                if(updateData(1) == 0)
                    finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_draft:
                // Do nothing for now
                updateData(0);
                return true;
            case R.id.action_discard:
                MainActivity.forumMgr.deletePost(post_id);
                finish();
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public int updateData(int status)
    {
        EditText title = findViewById(R.id.title);
        EditText content = findViewById(R.id.post);
        EditText tags = findViewById(R.id.tags);
        RatingBar ratingbar = findViewById(R.id.ratingBar);
        Spinner med_spinner = findViewById(R.id.post_facility_selection_spinner);
        if(title.getText().length() <= 3){
            Toast.makeText(PostToDraftUI.this, "Title must have at least 3 characters!", Toast.LENGTH_LONG).show();
            return -1;
        }
        else if(content.getText().toString().length() < 10 ) {
            Toast.makeText(PostToDraftUI.this, "Content must have at least 10 characters!", Toast.LENGTH_LONG).show();
            return -1;
        }
        else {
            MainActivity.forumMgr.updatePost(post_id, //post_id
                    title.getText().toString().trim(),//title
                    content.getText().toString().trim(),//content
                    getNewDate(), //newDate
                    getUsername(),//username
                    MainActivity.facilityMgr.all_facility_names[med_spinner.getSelectedItemPosition()],//medical_mecility
                    tags.getText().toString().toLowerCase().trim(),//tags
                    status,//status
                    ratingbar.getNumStars(),//ratings
                    () -> runOnUiThread(() -> {// Stuff that updates the UI
                        if (status == 1)
                            Toast.makeText(getApplicationContext(), "Posted!", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getApplicationContext(), "Saved to draft!", Toast.LENGTH_SHORT).show();
                    }),//callback
                    e -> {
                        Log.d("Add Post Fail", e.getMessage());
                        runOnUiThread(() -> {// Stuff that updates the UI
                            Toast.makeText(getApplicationContext(), e.toString().trim(), Toast.LENGTH_SHORT).show();
                        });
                    }
            );
            return 0;
        }
    }

    public String getNewDate()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        return sdf.format(new Date());
    }

    public String getUsername(){
        Intent i = getIntent();
        return i.getStringExtra(ForumUI.USER_EXTRA);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
