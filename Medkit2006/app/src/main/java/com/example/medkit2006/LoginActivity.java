package com.example.medkit2006;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medkit2006.control.AccountMgr;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(MainActivity.accountMgr.isLoggedIn()){
            finish();
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
            return;
        }
        setContentView(R.layout.login);
        EditText usernameField = findViewById(R.id.loginUsername);
        EditText pwdField = findViewById(R.id.loginPassword);
        TextView status = findViewById(R.id.loginStatus);
        //TODO: remove before submitting
        usernameField.setText("test");
        pwdField.setText("testtest");
        findViewById(R.id.loginLoginBtn).setOnClickListener(button -> {
            if (usernameField.getText().length() == 0)
                status.setText("Please input username");
            else if (pwdField.getText().length() == 0)
                status.setText("Please input password");
            else {
                status.setText("Logging in...");
                AccountMgr mgr = MainActivity.accountMgr;
                mgr.validateAccount(usernameField.getText().toString(), pwdField.getText().toString(), success -> {
                    if (success) {
                        mgr.getUserDetails(usernameField.getText().toString(),user -> {
                            mgr.setLoggedInUser(user);
                            finish();
                            Intent intent = new Intent(this, AccountActivity.class);
                            startActivity(intent);
                        },e -> status.setText(e.getMessage()));
                    } else
                        status.setText("Invalid username or password");
                }, e -> status.setText(e.getMessage()));
            }
        });
    }

    public void toRegister(View view) {
        finish();
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }
}
