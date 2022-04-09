package com.example.medkit2006.boundary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medkit2006.MainActivity;
import com.example.medkit2006.MessageAdapter;
import com.example.medkit2006.R;
import com.example.medkit2006.control.ChatMgr;
import com.example.medkit2006.entity.Message;
import com.example.medkit2006.entity.User;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    TextView username;
    User tmp_user;
    ImageButton btn_send;
    EditText text_send;
    MessageAdapter messageAdapter;
    List<Message> mChat;
    RecyclerView recyclerView;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_ui);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

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
        readMessages(tmp_user.getUsername(), userName);

    }

    private void sendMessage(String sender, String receiver, String message) {
        ChatMgr mgr = MainActivity.chatMgr;
        TextView error = findViewById(R.id.sendError);
        mgr.startPrivateMessage(sender, receiver, message, () -> {
            finish();
        }, e -> error.setText(e.getMessage()));
        /*i = new Intent(MessageActivity.this, MainActivity.accountMgr.isLoggedIn() ? MessageActivity.class : LoginUI.class);
        i.putExtra("username", getUser());
        startActivity(i);*/
    }

    private void readMessages(final String sender, final String receiver) {
        mChat = new ArrayList<>();
        ChatMgr mgr = MainActivity.chatMgr;
        TextView error = findViewById(R.id.sendError);
        mgr.getChatSender(sender, receiver, message -> {
            mChat.add(message);
        }, e -> error.setText(e.getMessage()));
        mgr.getChatSender(receiver, sender, message -> {
            mChat.add(message);
        }, e -> error.setText(e.getMessage()));
        messageAdapter = new MessageAdapter(MessageActivity.this, mChat);
        recyclerView.setAdapter(messageAdapter);
    }
}