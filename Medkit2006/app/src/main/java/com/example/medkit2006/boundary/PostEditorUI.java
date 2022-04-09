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
        MainActivity.facilityMgr.getAllFacilityName(names->{
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
        });
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
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_draft:
                // Do nothing for now
                insertData(0);
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
        EditText post = findViewById(R.id.post);
        EditText tags = findViewById(R.id.tags);
        RatingBar ratingbar = findViewById(R.id.ratingBar);
        Spinner med_spinner = findViewById(R.id.post_facility_selection_spinner);
        ;

        MainActivity.forumMgr.addPost(titleView.getText().toString().trim(),//title
                post.getText().toString().trim(),//content
                getUser(), //username
                medical_facility_names[med_spinner.getSelectedItemPosition()],//medical_mecility
                tags.getText().toString().toLowerCase().trim(),//tags
                status,//status
                ratingbar.getNumStars(),//ratings
                ()->{},//callback
                e->{
                    Log.d("Add Post Fail",e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {// Stuff that updates the UI
                            Toast.makeText(getApplicationContext(), e.toString().trim(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
        );

        /*ForumDbHelper helper = new ForumDbHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ForumContract.ForumEntry.COLUMN_STATUS, status);
        values.put(ForumContract.ForumEntry.COLUMN_TITLE, title);
        values.put(ForumContract.ForumEntry.COLUMN_TAGS, postTag);
        values.put(ForumContract.ForumEntry.COLUMN_REPORT, 0);
        values.put(ForumContract.ForumEntry.COLUMN_COMMENTS, "");
        values.put(ForumContract.ForumEntry.COLUMN_DATE, getDate());
        values.put(ForumContract.ForumEntry.COLUMN_LIKES, 0);
        values.put(ForumContract.ForumEntry.COLUMN_USER, getUser());
        values.put(ForumContract.ForumEntry.COLUMN_POST, postContent);

        long insert_id = db.insert(ForumContract.ForumEntry.TABLE_NAME, null, values);
        db.close();

        if(insert_id != -1)
        {
            Toast.makeText(PostEditorUI.this, "Posted!!" , Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(PostEditorUI.this, "Error posting."  , Toast.LENGTH_LONG).show();
        }*/
    }

/*    public String getDate()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(new Date());
        return strDate;
    }*/

    public String getUser()
    {
        Intent i = getIntent();
        Bundle bd = i.getExtras();
        String name = null;
        if(bd != null) {
            name = (String) bd.get("username");
        }
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
