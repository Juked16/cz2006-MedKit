package com.example.medkit2006;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medkit2006.entity.User;

public class AccountActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);
        User loggedIn = MainActivity.accountMgr.getLoggedInUser();
        if(loggedIn != null){//TODO: WHY NOT UPDATING??
            ((TextView) findViewById(R.id.accName)).setText(loggedIn.getUsername());
            ((TextView) findViewById(R.id.accEmail)).setText(loggedIn.getEmail());
            ((TextView) findViewById(R.id.accSettings)).setText("?");
            ((Button)findViewById(R.id.btnAccLogin)).setText("Logout");
        }
    }

    public void toLogin(View view){
        if(((Button)view).getText().toString().equals("Logout"))
            MainActivity.accountMgr.setLoggedInUser(null);
        Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
