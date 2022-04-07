package com.example.medkit2006.boundary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.medkit2006.R;
import com.example.medkit2006.UserAdapter;
import com.example.medkit2006.entity.User;

import java.util.ArrayList;
import java.util.List;

public class ChatUsersUI extends AppCompatActivity {

    private RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private List<User> mUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_users_ui);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUsers = new ArrayList<>();

        readUsers();
    }

    private void readUsers() {
        //TODO: Read users from database/Forum instead of hardcoding Users
        mUsers.add(new User("Tom"));
        mUsers.add(new User("Elira"));
        userAdapter = new UserAdapter(this, mUsers);
        recyclerView.setAdapter(userAdapter);
    }
}