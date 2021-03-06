package com.example.medkit2006.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medkit2006.MainActivity;
import com.example.medkit2006.R;
import com.example.medkit2006.boundary.ChatMessageUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private final Context mContext;
    private final HashMap<Integer, ArrayList<String>> chats;

    public ChatListAdapter(Context mContext, HashMap<Integer, ArrayList<String>> chats) {
        this.mContext = mContext;
        this.chats = chats;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tile_chat_user, parent, false);
        return new ViewHolder(view);
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
                CardView cv = holder.itemView.findViewById(R.id.chatCardView);
                holder.username.setText(join.toString());
                cv.setOnClickListener(view -> {
                    Intent intent = new Intent(mContext, ChatMessageUI.class);
                    intent.putExtra("chatId", chat.getKey());
                    intent.putExtra("chatName", holder.username.getText().toString());
                    mContext.startActivity(intent);
                });
                cv.setOnLongClickListener(view -> {
                    new AlertDialog.Builder(mContext)
                            .setIcon(android.R.drawable.ic_delete)
                            .setTitle("Confirm delete?")
                            .setMessage("It will also delete for the other member(s)")
                            .setPositiveButton("Delete", (dialog,i)->
                                    MainActivity.chatMgr.removeChat(chat.getKey(), () -> {
                                        chats.remove(chat.getKey());
                                        notifyItemRemoved(position);
                                    }, e -> {
                                        e.printStackTrace();
                                        Toast.makeText(mContext, "Failed to remove", Toast.LENGTH_SHORT).show();
                                    }))
                            .setNegativeButton("No", (dialog, i) -> dialog.dismiss())
                            .show();
                    return true;
                });
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;

        public ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.chatName);
        }
    }
}
