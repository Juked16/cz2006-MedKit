package com.example.medkit2006.boundary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medkit2006.DB;
import com.example.medkit2006.MainActivity;
import com.example.medkit2006.R;
import com.example.medkit2006.entity.User;

import java.time.Instant;
import java.util.Objects;

public class PostDetailUI extends AppCompatActivity {
    private int post_id;
    private int likeFlag = 0;
    private int reportFlag = 0;
    private int likeNum = 0;
    private int reportNum = 0;
    private String post_user;
    public void setLikeFlag() { likeFlag = 1; }
    public int getLikeFlag() { return likeFlag; }
    public void setReportFlag() { reportFlag = 1; }
    public int getReportFlag() { return reportFlag; }
    private ArrayAdapter<String> commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        Intent i = getIntent();
        post_id = i.getIntExtra(ForumUI.EXTRA, -1);

        TextView title = findViewById(R.id.title);
        TextView user = findViewById(R.id.user);
        TextView date = findViewById(R.id.date);
        TextView tags = findViewById(R.id.tags);
        TextView content = findViewById(R.id.post);
        final Button like = findViewById(R.id.like);
        if(post_id == -1)
            Toast.makeText(this, "Can't find post!", Toast.LENGTH_LONG).show();
        else {
            MainActivity.forumMgr.getPostDetail(post_id, searchPost -> runOnUiThread(() -> {
                setTitle(searchPost.getTitle());
                user.setText("Post by " + searchPost.getUsername());
                post_user = searchPost.getUsername();
                title.setText(searchPost.getTitle());
                date.setText(searchPost.getDate());
                tags.setText("Tags: " + searchPost.getTags());
                content.setText(searchPost.getContent());
                likeNum = searchPost.getLikeNum();
                reportNum = searchPost.getReportNum();
                like.setText(likeNum + " Like" + (likeNum > 0 ? "s" : ""));
                commentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, searchPost.getComments());
                ListView listView = findViewById(R.id.commList);
                listView.setAdapter(commentAdapter);
                User u = MainActivity.accountMgr.getLoggedInUser();
                findViewById(R.id.message).setVisibility(u != null && Objects.equals(u.getUsername(), post_user) ? View.INVISIBLE : View.VISIBLE);
            }), error -> Log.d("Detail Getting Failed", error.getMessage()));
        }

        final Button report = findViewById(R.id.report);
        final ImageButton message = findViewById(R.id.message);
        Button comIt = findViewById(R.id.comIt);
        final EditText comment = findViewById(R.id.comment);

        like.setOnClickListener(view -> {
            if(!MainActivity.accountMgr.isLoggedIn()){
                Toast.makeText(PostDetailUI.this, "Please Login First!", Toast.LENGTH_LONG).show();
                Intent i1 = new Intent(PostDetailUI.this, LoginUI.class);
                startActivity(i1);
            }
            else if(getLikeFlag() != 0)
                Toast.makeText(PostDetailUI.this, "Already Liked!", Toast.LENGTH_LONG).show();
            else {
                ImageView like_img = findViewById(R.id.like_img);
                like_img.setImageResource(R.drawable.like_on);
                likeNum++;
                like.setText(likeNum + " Like" + (likeNum > 0 ? "s" : ""));
                setLikeFlag();
                //update the like into database
                DB.instance.execute(("update post set likes = ".toUpperCase() + likeNum + " WHERE _ID = "+post_id), ()->{}, error-> Log.d("Update Likes Fail", error.getMessage()));
            }
        });

        report.setOnClickListener(view -> {
            if(!MainActivity.accountMgr.isLoggedIn()){
                Toast.makeText(PostDetailUI.this, "Please Login First!", Toast.LENGTH_LONG).show();
                Intent i12 = new Intent(PostDetailUI.this, LoginUI.class);
                startActivity(i12);
            }
            else if(getReportFlag() != 0)
                Toast.makeText(PostDetailUI.this, "Already Reported!", Toast.LENGTH_LONG).show();
            else
            {
                if(reportNum >= 5) {
                    MainActivity.forumMgr.deletePost(post_id);
                    onBackPressed();
                } else {
                    DB.instance.execute(("update post set report = " + (reportNum + 1) + " where _ID = " + post_id).toUpperCase(),
                            () -> runOnUiThread(() -> Toast.makeText(PostDetailUI.this, "Reported!", Toast.LENGTH_LONG).show()), error -> Log.d("Update Reports Fail", error.getMessage()));
                    setReportFlag();
                }
            }
        });

        message.setOnClickListener(view -> {
            //check if the account is logged in
            User user1 = MainActivity.accountMgr.getLoggedInUser();
            if(user1 == null){
                Toast.makeText(PostDetailUI.this, "Please Login First!", Toast.LENGTH_LONG).show();
                Intent i13 = new Intent(PostDetailUI.this, LoginUI.class);
                startActivity(i13);
            }
            else {
                MainActivity.chatMgr.startPrivateMessage(user1.getUsername(), post_user, () -> {
                }, error -> Log.d("Message Unable to Start", error.getMessage()));
                MainActivity.chatMgr.getPrivateMessage(user1.getUsername(), post_user, chat_id->{
                    Intent intent = new Intent(PostDetailUI.this, ChatMessageUI.class);
                    intent.putExtra("chatId", chat_id);
                    intent.putExtra("chatName", post_user);
                    startActivity(intent);
                    }, error-> Log.d("Message Unable to Start", error.getMessage()));
            }
        });

        comIt.setOnClickListener(view -> {
            User user12 = MainActivity.accountMgr.getLoggedInUser();
            if(user12 == null){
                Toast.makeText(PostDetailUI.this, "Please Login First!", Toast.LENGTH_LONG).show();
                Intent i14 = new Intent(PostDetailUI.this, LoginUI.class);
                startActivity(i14);
            }
            else {
                String c = comment.getText().toString().trim();
                comment.setText("");
                MainActivity.forumMgr.addComment(post_id,user12.getUsername(),c,
                        () -> {
                            commentAdapter.add(user12.getUsername() + ": " + c + "\n" + Instant.now());
                            Toast.makeText(PostDetailUI.this, "Commented!", Toast.LENGTH_LONG).show();
                        },
                        error -> Log.d("Update Comments Fail", error.getMessage()));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        User user = MainActivity.accountMgr.getLoggedInUser();
        findViewById(R.id.message).setVisibility(user != null && Objects.equals(user.getUsername(), post_user) ? View.INVISIBLE : View.VISIBLE);
    }
}
