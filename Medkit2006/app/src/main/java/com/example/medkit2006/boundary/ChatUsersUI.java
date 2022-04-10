package com.example.medkit2006.boundary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medkit2006.MainActivity;
import com.example.medkit2006.R;
import com.example.medkit2006.UserAdapter;
import com.example.medkit2006.control.ChatMgr;
import com.example.medkit2006.entity.User;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatUsersUI extends AppCompatActivity {

    private RecyclerView recyclerView;

    private UserAdapter userAdapter;
    User tmp_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_users_ui);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        tmp_user = MainActivity.accountMgr.getLoggedInUser();
        if (tmp_user == null) {
            finish();
            startActivity(new Intent(this, LoginUI.class));
            return;
        }
        updateChats();
        findViewById(R.id.chatTestBtn).setVisibility(View.VISIBLE);
        findViewById(R.id.chatTestBtn).setOnClickListener(btn -> MainActivity.chatMgr.startPrivateMessage(tmp_user.getUsername(), "test2", this::updateChats, Throwable::printStackTrace));//TODO: remove
    }

    /**
     * Called whenever chat list needs to be updated (member add,remove, new chat etc)
     */
    public void updateChats() {
        ChatMgr mgr = MainActivity.chatMgr;
        mgr.getChats(tmp_user.getUsername(), chatIds -> {
            HashMap<Integer, ArrayList<String>> map = new HashMap<>();
            for (Integer id : chatIds)
                mgr.getMembers(id, usernames -> {
                    map.put(id,usernames);
                    userAdapter = new UserAdapter(this, map);
                    recyclerView.setAdapter(userAdapter);
                }, Throwable::printStackTrace);
        }, Throwable::printStackTrace);
    }
}