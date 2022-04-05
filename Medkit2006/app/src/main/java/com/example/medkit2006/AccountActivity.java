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
    }

    @Override
    protected void onResume() {
        super.onResume();
        User loggedIn = MainActivity.accountMgr.getLoggedInUser();
        if (loggedIn != null) {
            ((TextView) findViewById(R.id.accountName)).setText(loggedIn.getUsername());
            ((TextView) findViewById(R.id.accountEmail)).setText(loggedIn.getEmail());
            findViewById(R.id.AccountToSettingsBtn).setVisibility(View.VISIBLE);
            findViewById(R.id.AccountToSettingsBtn).setOnClickListener(btn -> {
                Intent intent = new Intent(AccountActivity.this, AccountSettingsActivity.class);
                startActivity(intent);
            });
            ((Button) findViewById(R.id.accountLoginBtn)).setText("Logout");
        }
    }

    public void toLogin(View view) {
        if (((Button) view).getText().toString().equals("Logout")) {
            MainActivity.accountMgr.setLoggedInUser(null);
        }
        finish();
        Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
