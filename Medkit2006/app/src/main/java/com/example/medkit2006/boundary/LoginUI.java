package com.example.medkit2006.boundary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medkit2006.MainActivity;
import com.example.medkit2006.R;
import com.example.medkit2006.control.AccountMgr;

public class LoginUI extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MainActivity.accountMgr.isLoggedIn()) {
            finish();
            startActivity(new Intent(this, AccountUI.class));
            return;
        }
        EditText usernameField = findViewById(R.id.loginUsername);
        EditText pwdField = findViewById(R.id.loginPassword);
        TextView status = findViewById(R.id.loginStatus);
        //TODO: remove before submitting
        usernameField.setText("test");
        pwdField.setText("testtest");
        findViewById(R.id.loginForgotPwBtn).setOnClickListener(btn -> startActivity(new Intent(this, ForgetPwUI.class)));
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
                        mgr.getUserDetails(usernameField.getText().toString(), user -> {
                            mgr.setLoggedInUser(user);
                            finish();
                            Intent intent = new Intent(this, AccountUI.class);
                            startActivity(intent);
                        }, e -> status.setText(e.getMessage()));
                    } else
                        status.setText("Invalid username or password");
                }, e -> status.setText(e.getMessage()));
            }
        });
    }

    public void toRegister(View view) {
        finish();
        Intent intent = new Intent(LoginUI.this, RegistrationUI.class);
        startActivity(intent);
    }
}
