package com.example.medkit2006;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AccountActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);
    }


    public void toLogin(View view){
        Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
        startActivity(intent);
    }


}
