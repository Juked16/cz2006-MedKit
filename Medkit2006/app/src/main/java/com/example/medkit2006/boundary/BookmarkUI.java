package com.example.medkit2006.boundary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medkit2006.adapter.BookmarkAdapter;
import com.example.medkit2006.MainActivity;
import com.example.medkit2006.R;
import com.example.medkit2006.entity.User;

public class BookmarkUI extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activility_bookmark);
    }

    @Override
    protected void onResume() {
        super.onResume();
        User user = MainActivity.accountMgr.getLoggedInUser();
        if (user == null) {
            finish();
            startActivity(new Intent(this, LoginUI.class));
            return;
        }
        ListView list = findViewById(R.id.bookmarkList);
        list.setOnItemClickListener((adapterView, view, pos, l) -> {

        });
        MainActivity.bookmarkMgr.getAll(bookmarks -> {
            list.setAdapter(new BookmarkAdapter(this, bookmarks));
            findViewById(R.id.bookmarkLoading).setVisibility(View.INVISIBLE);
        }, e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG));
    }
}
