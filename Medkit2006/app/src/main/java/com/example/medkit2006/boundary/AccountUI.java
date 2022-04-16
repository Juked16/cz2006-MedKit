package com.example.medkit2006.boundary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medkit2006.MainActivity;
import com.example.medkit2006.R;
import com.example.medkit2006.entity.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AccountUI extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        if (!MainActivity.accountMgr.isLoggedIn()) {
            Toast.makeText(this, "Please Login First!", Toast.LENGTH_LONG).show();
        }
        BottomNavigationView btmNav = findViewById(R.id.navigation);
        btmNav.getMenu().clear();
        btmNav.inflateMenu(R.menu.bottom_navigation);
        btmNav.setSelectedItemId(R.id.nav_account);
        btmNav.setOnItemSelectedListener(item -> {
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
            }
            return false;
        });
    }


    @SuppressLint("SetTextI18n")
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

            findViewById(R.id.accountChangePwBtn).setVisibility(View.VISIBLE);
            findViewById(R.id.accountChangePwBtn).setOnClickListener(btn -> {
                Intent intent = new Intent(this, ForgetPwUI.class);
                intent.putExtra("ChangePassword", true);
                startActivity(intent);
            });

            ((Button) findViewById(R.id.accountLoginBtn)).setText("Logout");
            findViewById(R.id.accountLoginBtn).setOnClickListener(btn -> {
                MainActivity.accountMgr.setLoggedInUser(null);
                finish();
                Intent intent = new Intent(this, LoginUI.class);
                startActivity(intent);
            });
        } else
            Toast.makeText(this, "Please Login First!", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_go_chat) {
            startActivity(new Intent(getApplicationContext(), MainActivity.accountMgr.isLoggedIn() ? ChatUsersUI.class : LoginUI.class));
            return true;
        } else if (item.getItemId() == R.id.action_go_bookmark) {
            startActivity(new Intent(getApplicationContext(), MainActivity.accountMgr.isLoggedIn() ? BookmarkUI.class : LoginUI.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
