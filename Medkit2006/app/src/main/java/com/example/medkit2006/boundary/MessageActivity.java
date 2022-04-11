package com.example.medkit2006.boundary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.example.medkit2006.entity.Message;
import com.example.medkit2006.entity.User;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    User tmp_user;
    ImageButton btn_send;
    EditText text_send;
    MessageAdapter messageAdapter;
    List<Message> mChat = new ArrayList<>();
    RecyclerView recyclerView;
    Intent intent;
    Integer chatId;
    TextView error;
    Handler handler = new Handler(Looper.getMainLooper());
    boolean loopPaused = false;

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
        error = findViewById(R.id.sendError);

        intent = getIntent();
        setTitle(intent.getStringExtra("chatName"));
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
        loopPaused = false;
        loop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        loopPaused = true;
    }

    /**
     * Polling for new msg every 5 sec, find a better way
     */
    private void loop() {
        if (loopPaused) return;
        MainActivity.chatMgr.getMessages(chatId, messages -> {
            if (!mChat.equals(messages)) {
                mChat = messages;
                messageAdapter = new MessageAdapter(this, mChat);
                recyclerView.setAdapter(messageAdapter);
            }
        }, e -> error.setText(e.getMessage()));
        handler.postDelayed(this::loop, 5000);
    }

    private void sendMessage(String sender, String message) {
        MainActivity.chatMgr.sendMessage(chatId, sender, message, () -> {
            mChat.add(new Message(sender, message));
            messageAdapter.notifyItemInserted(mChat.size() - 1);
        }, e -> error.setText(e.getMessage()));
        //TODO: notifications?
    }
}