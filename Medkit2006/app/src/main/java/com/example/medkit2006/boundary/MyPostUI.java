package com.example.medkit2006.boundary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medkit2006.adapter.FeedAdapter;
import com.example.medkit2006.MainActivity;
import com.example.medkit2006.R;
import com.example.medkit2006.entity.Post;

public class MyPostUI extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);

        setTitle("Your Posts");
        final ListView listView = findViewById(R.id.list);
        MainActivity.forumMgr.getMyPostAbstract(getUser(), postList->{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    FeedAdapter itemsAdapter = new FeedAdapter(MyPostUI.this, postList);
                    listView.setAdapter(itemsAdapter);
                }
            });
        }, error->{
            Log.d("MyPost display error", error.getMessage());
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
            {
                Intent i = new Intent(MyPostUI.this, PostDetailUI.class);
                Post selectedFromList = (Post)(listView.getItemAtPosition(position));
                i.putExtra(ForumUI.EXTRA , selectedFromList.getID());
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
