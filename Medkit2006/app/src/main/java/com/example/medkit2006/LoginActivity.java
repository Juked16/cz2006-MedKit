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
        setContentView(R.layout.login);
        EditText usernameField = findViewById(R.id.loginUsername);
        EditText pwdField = findViewById(R.id.loginPassword);
        TextView status = findViewById(R.id.loginStatus);
        status.setText(DB.instance.lastMsg);//TODO: remove
        findViewById(R.id.btnLogin).setOnClickListener(button -> {
            if(usernameField.getText().length() == 0)
                status.setText("Please input username");
            else if(pwdField.getText().length() == 0)
                status.setText("Please input password");
            else {
                status.setText("Logging in...");
                AccountMgr mgr = MainActivity.accountMgr;
                mgr.validateAccount(usernameField.getText().toString(), pwdField.getText().toString(), success -> {
                    if (success) {
                        Intent intent = new Intent(this, AccountActivity.class);
                        startActivity(intent);
                    }else
                        status.setText("Invalid username or password");
                });
            }
        });
    }

    public void toRegister(View view) {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }
}
