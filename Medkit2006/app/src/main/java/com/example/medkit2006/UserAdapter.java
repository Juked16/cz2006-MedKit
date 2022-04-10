package com.example.medkit2006;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medkit2006.boundary.MessageActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final Context mContext;
    private final HashMap<Integer, ArrayList<String>> chats;

    public UserAdapter(Context mContext, HashMap<Integer, ArrayList<String>> chats) {
        this.mContext = mContext;
        this.chats = chats;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_container, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int pos = 0;
        for(Map.Entry<Integer,ArrayList<String>> chat : chats.entrySet()){
            if(pos++ == position){
                StringJoiner join = new StringJoiner(",");
                for(String username : chat.getValue()) {
                    if(!username.equals(MainActivity.accountMgr.getLoggedInUser().getUsername()))
                        join.add(username);
                }
                holder.username.setText(join.toString());
                holder.itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(mContext, MessageActivity.class);
                    intent.putExtra("chatId", chat.getKey());
                    intent.putExtra("chatName", holder.username.getText().toString());
                    mContext.startActivity(intent);
                });
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;

        public ViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.chatName);
        }
    }
}
