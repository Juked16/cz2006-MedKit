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

import java.util.Objects;

public class PostDetailUI extends AppCompatActivity {
    private int post_id;
    private int likeFlag = 0;
    private int reportFlag = 0;
    private int likeNum = 0;
    private int reportNum = 0;
    private String comments = null;
    private String post_user;
    public void setLikeFlag() { likeFlag = 1; }
    public int getLikeFlag() { return likeFlag; }
    public void setReportFlag() { reportFlag = 1; }
    public int getReportFlag() { return reportFlag; }

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
                if (searchPost.getReportNum() >= 5)
                    setTitle("Reported Post");
                else {
                    setTitle(searchPost.getTitle());
                    user.append(searchPost.getUsername());
                    post_user = searchPost.getUsername();
                    title.setText(searchPost.getTitle());
                    date.setText(searchPost.getDate());
                    tags.append(searchPost.getTags());
                    content.setText(searchPost.getContent());
                    likeNum = searchPost.getLikeNum();
                    reportNum = searchPost.getReportNum();
                    like.setText("Like " + likeNum);
                    comments = searchPost.getComments();
                    if(comments != null) {
                        String[] items = comments.split(getString(R.string.comment_break));
                        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, items);
                        ListView listView = findViewById(R.id.commList);
                        listView.setAdapter(itemsAdapter);
                    }
                }
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
                like.setText("Like " + (likeNum+1));
                setLikeFlag();
                //update the like into database
                DB.instance.execute(("update post set likes = ".toUpperCase() + (likeNum + 1) + " WHERE _ID = "+post_id), ()->{}, error-> Log.d("Update Likes Fail", error.getMessage()));
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
                DB.instance.execute(("update post set report = " + (reportNum + 1) + " where _ID = "+post_id).toUpperCase(),
                        ()-> runOnUiThread(() -> Toast.makeText(PostDetailUI.this,"Reported!", Toast.LENGTH_LONG).show()), error-> Log.d("Update Reports Fail", error.getMessage()));
                setReportFlag();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
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
                String newComments = user12.getUsername() + ": " + comments + comment.getText().toString().trim() + getString(R.string.comment_break);
                DB.instance.execute(("update post set COMMENTS = '" + newComments + "' where _ID = " + post_id),
                        () -> runOnUiThread(() -> Toast.makeText(PostDetailUI.this, "Commented!", Toast.LENGTH_LONG).show()), error -> Log.d("Update Comments Fail", error.getMessage()));
                Intent intent = getIntent();
                finish();
                startActivity(intent);
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
