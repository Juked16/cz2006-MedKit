package com.example.medkit2006.boundary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medkit2006.FeedAdapter;
import com.example.medkit2006.MainActivity;
import com.example.medkit2006.R;
import com.example.medkit2006.entity.Post;
import com.example.medkit2006.data.ForumContract;
import com.example.medkit2006.data.ForumDbHelper;

import java.util.ArrayList;

public class PostDraftUI extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_drafts);
        setTitle("Your Drafts");
        final ListView listView = (ListView) findViewById(R.id.list);
        MainActivity.forumMgr.getMyDraftAbstract(getUser(), postList->{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    FeedAdapter itemsAdapter = new FeedAdapter(PostDraftUI.this,postList);
                    listView.setAdapter(itemsAdapter);
                }
            });
        }, error->{
            Log.d("Post Draft OnCreate error", error.getMessage());
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
            {
                Intent i = new Intent(PostDraftUI.this, PostToDraftUI.class);
                Post selectedFromList = (Post)(listView.getItemAtPosition(position));
                i.putExtra(ForumUI.EXTRA, selectedFromList.getID());
                i.putExtra(ForumUI.USEREXTRA, getUser());
                startActivity(i);
            }
        });
    }

    public String getUser()
    {
        Intent i = getIntent();
        String username = i.getStringExtra(ForumUI.USEREXTRA);
        return username;
    }
}
