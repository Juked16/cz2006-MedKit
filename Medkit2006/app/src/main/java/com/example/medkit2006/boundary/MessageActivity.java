package com.example.medkit2006.boundary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medkit2006.MainActivity;
import com.example.medkit2006.MessageAdapter;
import com.example.medkit2006.R;
import com.example.medkit2006.control.ChatMgr;
import com.example.medkit2006.entity.Message;
import com.example.medkit2006.entity.User;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    TextView chatName;
    User tmp_user;
    ImageButton btn_send;
    EditText text_send;
    MessageAdapter messageAdapter;
    List<Message> mChat = new ArrayList<>();
    RecyclerView recyclerView;
    Intent intent;
    Integer chatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_ui);

        recyclerView = findViewById(R.id.chatRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);

        intent = getIntent();
        String chatName = intent.getStringExtra("chatName");
        setTitle(chatName);
        chatId = intent.getIntExtra("chatId", -1);
        tmp_user = MainActivity.accountMgr.getLoggedInUser();

        btn_send.setOnClickListener(view -> {
            String message = text_send.getText().toString();
            if (!message.equals("")) {
                sendMessage(tmp_user.getUsername(), message);
            } else {
                Toast.makeText(MessageActivity.this, "You can't send empty message",
                        Toast.LENGTH_SHORT).show();
            }
            text_send.setText("");
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView error = findViewById(R.id.sendError);
        MainActivity.chatMgr.getMessages(chatId, messages -> {
            mChat = messages;
            messageAdapter = new MessageAdapter(this, mChat);
            recyclerView.setAdapter(messageAdapter);
        }, e -> error.setText(e.getMessage()));
    }

    private void sendMessage(String sender, String message) {
        ChatMgr mgr = MainActivity.chatMgr;
        TextView error = findViewById(R.id.sendError);
        mgr.sendMessage(chatId, sender, message, () -> mChat.add(new Message(sender,message)),e -> error.setText(e.getMessage()));
        //TODO: notifications?
        /*i = new Intent(MessageActivity.this, MainActivity.accountMgr.isLoggedIn() ? MessageActivity.class : LoginUI.class);
        i.putExtra("username", getUser());
        startActivity(i);*/
    }
}