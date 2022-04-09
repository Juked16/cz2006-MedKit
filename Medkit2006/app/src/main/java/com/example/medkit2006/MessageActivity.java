package com.example.medkit2006;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medkit2006.control.ChatMgr;
import com.example.medkit2006.entity.User;

public class MessageActivity extends AppCompatActivity {

    TextView username;
    User tmp_user;
    ImageButton btn_send;
    EditText text_send;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);

        intent = getIntent();
        String userName = intent.getStringExtra("username");
        tmp_user = MainActivity.accountMgr.getLoggedInUser();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = text_send.getText().toString();
                if (!message.equals("")) {
                    sendMessage(tmp_user.getUsername(), userName, message);
                } else {
                    Toast.makeText(MessageActivity.this, "You can't send empty message",
                            Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });

        username.setText(userName);

    }

    private void sendMessage(String sender, String receiver, String message) {
        ChatMgr mgr = MainActivity.chatMgr;
        TextView error = findViewById(R.id.sendError);
        mgr.startPrivateMessage(sender, receiver, message, () -> {
            finish();
        }, e -> error.setText(e.getMessage()));
    }
}