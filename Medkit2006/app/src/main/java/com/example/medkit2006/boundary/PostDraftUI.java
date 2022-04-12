package com.example.medkit2006.boundary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medkit2006.adapter.FeedAdapter;
import com.example.medkit2006.MainActivity;
import com.example.medkit2006.R;
import com.example.medkit2006.entity.Post;

public class PostDraftUI extends AppCompatActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_drafts);
        setTitle("Your Drafts");
        listView = findViewById(R.id.list);
        MainActivity.forumMgr.getMyDraftAbstract(getUser(), postList-> runOnUiThread(() -> {
            FeedAdapter itemsAdapter = new FeedAdapter(PostDraftUI.this,postList);
            listView.setAdapter(itemsAdapter);
        }), error-> Log.d("Post Draft OnCreate error", error.getMessage()));

        listView.setOnItemClickListener((adapter, v, position, arg3) -> {
            Intent i = new Intent(PostDraftUI.this, PostToDraftUI.class);
            Post selectedFromList = (Post)(listView.getItemAtPosition(position));
            i.putExtra(ForumUI.EXTRA, selectedFromList.getID());
            i.putExtra(ForumUI.USER_EXTRA, getUser());
            startActivity(i);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.forumMgr.getMyDraftAbstract(getUser(), postList-> runOnUiThread(() -> {
            FeedAdapter itemsAdapter = new FeedAdapter(PostDraftUI.this,postList);
            listView.setAdapter(itemsAdapter);
        }), error-> Log.d("Post Draft OnResume error", error.getMessage()));

    }

    public String getUser()
    {
        Intent i = getIntent();
        return i.getStringExtra(ForumUI.USER_EXTRA);
    }
}
