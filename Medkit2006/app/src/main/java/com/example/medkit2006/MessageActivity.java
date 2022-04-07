package com.example.medkit2006;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.example.medkit2006.entity.User;

public class MessageActivity extends AppCompatActivity {

    TextView username;
    User tmp_user;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CHAT");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/

        username = findViewById(R.id.username);

        intent = getIntent();
        String userName = intent.getStringExtra("username");
        tmp_user = MainActivity.accountMgr.getLoggedInUser();
        username.setText(userName);

    }
}