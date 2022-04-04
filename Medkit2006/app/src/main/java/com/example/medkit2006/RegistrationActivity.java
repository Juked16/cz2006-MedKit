package com.example.medkit2006;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medkit2006.control.AccountMgr;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        EditText username = findViewById(R.id.regUsername);
        EditText email = findViewById(R.id.regEmailAddress);
        EditText pwdField = findViewById(R.id.regPassword);
        EditText cPwdField = findViewById(R.id.regConfirmPassword);
        TextView error = findViewById(R.id.regError);
        findViewById(R.id.btnRegister).setOnClickListener(view -> {
            AccountMgr mgr = MainActivity.accountMgr;
            if (username.length() >= 3) {
                if (mgr.validEmail(email.getText().toString())) {
                    if (pwdField.getText().toString().length() < 8) {
                        error.setText("Password length must be at least 8 characters");
                    } else if (!mgr.validateConfirmPassword(pwdField.getText().toString(), cPwdField.getText().toString())) {
                        error.setText("Passwords do not match");
                        pwdField.requestFocus();
                        pwdField.setText("");
                        cPwdField.setText("");
                    } else
                        mgr.usernameExist(username.getText().toString(), userExist -> {
                            if (userExist) {
                                error.setText("Username already in-use");
                                username.requestFocus();
                            } else
                                mgr.emailExist(email.getText().toString(), emailExist -> {
                                    if (emailExist) {
                                        error.setText("Email already in-use");
                                        email.requestFocus();
                                    } else {
                                        error.setText("Registering...");
                                        mgr.createAccount(username.getText().toString(), email.getText().toString(), pwdField.getText().toString(), () -> {
                                            mgr.getUserDetails(username.getText().toString(), user -> {
                                                mgr.setLoggedInUser(user);
                                                Intent intent = new Intent(this, LoginActivity.class);
                                                startActivity(intent);
                                            }, e -> error.setText(e.getMessage()));
                                        }, e -> error.setText(e.getMessage()));
                                    }
                                }, e -> error.setText(e.getMessage()));
                        }, e -> error.setText(e.getMessage()));
                } else {
                    error.setText("Invalid email");
                    email.requestFocus();
                }
            } else {
                error.setText("Username must be at least 3 characters" + username.length());
                username.requestFocus();
            }
        });
    }
}
