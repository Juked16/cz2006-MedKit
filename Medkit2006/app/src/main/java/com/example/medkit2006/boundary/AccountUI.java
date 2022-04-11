package com.example.medkit2006.boundary;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.example.medkit2006.MainActivity;
import com.example.medkit2006.R;
import com.example.medkit2006.entity.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AccountUI extends AppCompatActivity {

    private View.OnClickListener homeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            NavUtils.navigateUpFromSameTask(AccountUI.this);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);
        BottomNavigationView btmNav = new BottomNavigationView(this);
        btmNav = findViewById(R.id.navigation);
        btmNav.inflateMenu(R.menu.bottom_navigation);
        btmNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent i;
                switch (item.getItemId()) {
                    case R.id.nav_search:
                        i = new Intent(getApplicationContext(), SearchUI.class);
                        startActivity(i);
                        break;
                    case R.id.nav_forum:
                        i = new Intent(getApplicationContext(), ForumUI.class);
                        startActivity(i);
                        break;
                    case R.id.nav_account:
                        i = new Intent(getApplicationContext(), AccountUI.class);
                        startActivity(i);
                        break;
                }
                return false;
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        User loggedIn = MainActivity.accountMgr.getLoggedInUser();
        if (loggedIn != null) {
            ((TextView) findViewById(R.id.accountName)).setText(loggedIn.getUsername());
            ((TextView) findViewById(R.id.accountEmail)).setText(loggedIn.getEmail());

            findViewById(R.id.accountToSettingsBtn).setVisibility(View.VISIBLE);
            findViewById(R.id.accountToSettingsBtn).setOnClickListener(btn -> {
                Intent intent = new Intent(this, AccountSettingsUI.class);
                startActivity(intent);
            });
            ((Button) findViewById(R.id.accountLoginBtn)).setText("Logout");
            findViewById(R.id.accountToMainBtn).setVisibility(View.VISIBLE);
            findViewById(R.id.accountToMainBtn).setOnClickListener(btn -> {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            });
        }
    }

    public void toLogin(View view) {
        if (((Button) view).getText().toString().equals("Logout")) {
            MainActivity.accountMgr.setLoggedInUser(null);
        }
        finish();
        Intent intent = new Intent(AccountUI.this, LoginUI.class);
        startActivity(intent);
    }
}
