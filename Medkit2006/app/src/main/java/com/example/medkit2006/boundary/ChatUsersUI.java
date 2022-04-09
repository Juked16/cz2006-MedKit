package com.example.medkit2006.boundary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.medkit2006.MainActivity;
import com.example.medkit2006.R;
import com.example.medkit2006.UserAdapter;
import com.example.medkit2006.control.ChatMgr;
import com.example.medkit2006.entity.User;

import java.util.ArrayList;
import java.util.List;

public class ChatUsersUI extends AppCompatActivity {

    private RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private List<User> mUsers;
    User tmp_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_users_ui);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUsers = new ArrayList<>();
        tmp_user = MainActivity.accountMgr.getLoggedInUser();
        ChatMgr mgr = MainActivity.chatMgr;
        TextView error = findViewById(R.id.sendError);
        mgr.getReceiver(tmp_user.getUsername(), user -> {
            if (!mUsers.contains(user)) {
                mUsers.add(user);
            }
        }, e -> error.setText(e.getMessage()));
        mgr.getSender(tmp_user.getUsername(), user -> {
            if (!mUsers.contains(user)) {
                mUsers.add(user);
            }
        }, e -> error.setText(e.getMessage()));

        readUsers();
    }

    private void readUsers() {
        mUsers.add(new User("Tom"));
        mUsers.add(new User("Elira"));
        //TODO: Read users from database/Forum instead
        userAdapter = new UserAdapter(this, mUsers);
        recyclerView.setAdapter(userAdapter);
    }
}