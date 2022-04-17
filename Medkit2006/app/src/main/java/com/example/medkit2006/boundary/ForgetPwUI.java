package com.example.medkit2006.boundary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medkit2006.MainActivity;
import com.example.medkit2006.R;
import com.example.medkit2006.control.AccountMgr;

public class ForgetPwUI extends AppCompatActivity {

    private int stage = 0;
    private String email;
    private String password;
    private boolean codeValidated = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpw);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        AccountMgr mgr = MainActivity.accountMgr;
        EditText text = findViewById(R.id.forgotPwTextField);
        TextView status = findViewById(R.id.forgotPwStatus);
        Button button = findViewById(R.id.forgotPwBtn);
        button.setOnClickListener(btn -> {
            status.setText("");
            switch (stage) {
                case 0:
                    email = text.getText().toString();
                    if (mgr.validEmail(email)) {
                        text.setText("");
                        text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                        text.setHint("Verification code");
                        mgr.emailExist(email, emailExist -> {
                            if (emailExist) {
                                status.setText("Sending email to " + email);
                                mgr.sendVerificationCode(email,
                                        () -> runOnUiThread(() -> status.setText("Verification code sent to " + email)),
                                        error -> runOnUiThread(() -> status.setText("An error occurred while sending email"))
                                );
                            } else
                                status.setText("Email does not exist in our records");
                        }, e -> status.setText(e.getMessage()));
                        stage++;
                    } else
                        status.setText("Invalid email");
                    break;
                case 1:
                    if (codeValidated || mgr.validateVerificationCode(text.getText().toString())) {
                        codeValidated = true;
                        text.setText("");
                        text.setHint("New Password");
                        text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        stage++;
                    } else
                        status.setText("Invalid code");
                    break;
                case 2:
                    password = text.getText().toString();
                    if (password.length() < 8) {
                        status.setText("Password length must be at least 8 characters");
                        break;
                    }
                    text.setText("");
                    text.setHint("Confirm Password");
                    stage++;
                    break;
                case 3:
                    if (mgr.validateConfirmPassword(password, text.getText().toString())) {
                        status.setText("Please wait...");
                        btn.setEnabled(false);
                        mgr.updatePassword(email, password, () -> {
                            email = null;
                            password = null;
                            stage = 0;
                            codeValidated = false;
                            finish();
                            startActivity(new Intent(this, LoginUI.class));
                        }, e -> {
                            btn.setEnabled(true);
                            stage = 1;
                            btn.callOnClick();
                            status.setText(e.getMessage());
                        });
                    } else {
                        stage = 1;
                        btn.callOnClick();
                        status.setText("Passwords do not match");
                    }
                    break;
            }
        });
        if (getIntent().getBooleanExtra("ChangePassword", false)) {//called from AccountSettingsUI
            text.setText(mgr.getLoggedInUser().getEmail());
            button.callOnClick();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        email = null;
        password = null;
        stage = 0;
        codeValidated = false;
    }
}
