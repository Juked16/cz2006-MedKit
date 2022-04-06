package com.example.medkit2006.boundary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medkit2006.MainActivity;
import com.example.medkit2006.R;
import com.example.medkit2006.entity.User;

import java.security.SecureRandom;

public class VerificationUI extends AppCompatActivity {

    private int resendTime;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification);
    }

    @Override
    protected void onResume() {
        super.onResume();
        User user = MainActivity.accountMgr.getLoggedInUser();
        if (user == null) {
            finish();
            startActivity(new Intent(this, LoginUI.class));
            return;
        }
        EditText code = findViewById(R.id.verificationCode);
        Button send = findViewById(R.id.verificationSendButton);
        Button verify = findViewById(R.id.verificationVerifyButton);
        TextView status = findViewById(R.id.verificationStatus);
        send.setOnClickListener(btn -> {
            code.setEnabled(true);
            verify.setEnabled(true);
            SecureRandom random = new SecureRandom();
            char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
            char[] codeBuf = new char[5];
            for (int idx = 0; idx < codeBuf.length; ++idx)
                codeBuf[idx] = chars[random.nextInt(chars.length)];
            String codeStr = new String(codeBuf);
            code.setText(codeStr); //TODO: remove after implement sending
            MainActivity.accountMgr.sendVerificationCode(user.getEmail(), codeStr);
            status.setText("Verification code sent to " + user.getEmail());
            send.setEnabled(false);
            resendTime = 11;
            scheduleTimer(send);
        });
        verify.setOnClickListener(btn -> {
            if (MainActivity.accountMgr.validateVerificationCode(code.getText().toString())) {
                user.setVerified(true);
                finish();
                startActivity(new Intent(this, AccountUI.class));
            } else
                status.setText("Invalid verification code");
        });
    }

    private void scheduleTimer(Button send){
        Runnable timer = () -> {
            if (--resendTime <= 0) {
                send.setText("Resend");
                send.setEnabled(true);
            }else {
                send.setText("Resend in " + resendTime + "s");
                scheduleTimer(send);
            }
        };
        handler.postDelayed(timer,1000);
    }
}
