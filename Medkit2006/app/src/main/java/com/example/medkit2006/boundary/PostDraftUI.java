package com.example.medkit2006.boundary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medkit2006.FeedAdapter;
import com.example.medkit2006.R;
import com.example.medkit2006.entity.FeedWord;
import com.example.student.forum.data.ForumContract;
import com.example.student.forum.data.ForumDbHelper;

import java.util.ArrayList;

public class PostDraftUI extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_drafts);
        setTitle("Your Drafts");
        ArrayList<FeedWord> words = new ArrayList<FeedWord>();
        ForumDbHelper helper = new ForumDbHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        String projection[] = {
                ForumContract.ForumEntry.COLUMN_TITLE,
                ForumContract.ForumEntry.COLUMN_USER,
                ForumContract.ForumEntry.COLUMN_DATE,
                ForumContract.ForumEntry.COLUMN_STATUS
        };
        String selection = ForumContract.ForumEntry.COLUMN_STATUS + "=? AND " + ForumContract.ForumEntry.COLUMN_USER + "=?";
        String[] selectionArgs = {"0",getUser()};
        Cursor cursor = db.query(ForumContract.ForumEntry.TABLE_NAME, projection,selection,selectionArgs,null,null,null);
        try {
            int userColumnIndex = cursor.getColumnIndex(ForumContract.ForumEntry.COLUMN_USER);
            int dateColumnIndex = cursor.getColumnIndex(ForumContract.ForumEntry.COLUMN_DATE);
            int titleColumnIndex = cursor.getColumnIndex(ForumContract.ForumEntry.COLUMN_TITLE);
            while(cursor.moveToNext())
            {
                String userID = cursor.getString(userColumnIndex);
                String date = cursor.getString(dateColumnIndex);
                String title = cursor.getString(titleColumnIndex);
                words.add(new FeedWord(title,userID,date));
            }
        } finally {
            cursor.close();
        }
        FeedAdapter itemsAdapter = new FeedAdapter(this,words);
        final ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
            {
                Intent i = new Intent(PostDraftUI.this, PostToDraftUI.class);
                FeedWord selectedFromList = (FeedWord)(listView.getItemAtPosition(position));
                i.putExtra("date" , selectedFromList.getDate());
                i.putExtra("username", getUser());
                startActivity(i);
            }
        });
    }

    public String getUser()
    {
        Intent i = getIntent();
        Bundle bd = i.getExtras();
        String username = "";
        if(bd != null)
        {
            String name = (String) bd.get("username");
            username = name;
        }
        return username;
    }
}
