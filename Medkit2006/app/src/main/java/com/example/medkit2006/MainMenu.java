package com.example.medkit2006;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainMenu extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);

        button = (Button)findViewById(R.id.btnSearch);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, MainActivity.class);
                startActivity(intent);
            }
        });


        //Intent intent = new Intent(MainActivity.this, Success.class);
        //startActivity(intent);
        //finish();
    }

    public void onClickSearch(View view){
        Intent intent = new Intent(MainMenu.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickForum(View view){
        Intent intent = new Intent(MainMenu.this, ForumActivity.class);
        startActivity(intent);
    }
    public void onClickChat(View view){
        Intent intent = new Intent(MainMenu.this, ChatActivity.class);
        startActivity(intent);
    }
    public void onClickBookmark(View view){
        Intent intent = new Intent(MainMenu.this, BookmarkActivity.class);
        startActivity(intent);
    }
    public void onClickAccount(View view){
        Intent intent = new Intent(MainMenu.this, AccountActivity.class);
        startActivity(intent);
    }

}
