package com.example.medkit2006.boundary;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BoardiesITSolutions.AndroidMySQLConnector.Exceptions.SQLColumnNotFoundException;
import com.BoardiesITSolutions.AndroidMySQLConnector.MySQLRow;
import com.example.medkit2006.MainActivity;
import com.example.medkit2006.R;
import com.example.medkit2006.data.DB;
import com.example.medkit2006.data.ForumContract;
import com.example.medkit2006.data.ForumDbHelper;
import com.example.medkit2006.entity.Post;
import com.example.medkit2006.entity.User;

import java.util.Locale;

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
        final TextView numLikes = findViewById(R.id.numLikes);
        if(post_id == -1)
            Toast.makeText(this, "Can't find post!", Toast.LENGTH_LONG).show();
        else {
            MainActivity.forumMgr.getPostDetail(post_id, searchPost -> {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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
                            numLikes.append("" + likeNum);
                            comments = searchPost.getComments();
                            if(comments != null) {
                                String items[] = comments.split("\n");
                                ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, items);
                                ListView listView = findViewById(R.id.commList);
                                listView.setAdapter(itemsAdapter);
                            }
                        }
                    }
                });
            }, error -> {
                Log.d("Detail Getting Failed", error.getMessage());
            });
        }

        final Button like = findViewById(R.id.like);
        final Button report = findViewById(R.id.report);
        final ImageButton message = findViewById(R.id.message);
        Button comIt = findViewById(R.id.comIt);
        final EditText comment = findViewById(R.id.comment);

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getLikeFlag() != 0)
                    Toast.makeText(PostDetailUI.this, "Already Liked!", Toast.LENGTH_LONG).show();
                if(getLikeFlag() == 0) {
                    TextView newNumLikes = (TextView) findViewById(R.id.numLikes);
                    newNumLikes.setText("Likes: " + (likeNum + 1));
                    setLikeFlag();
                    //update the like into database
                    DB.instance.execute(("update post set likes = ".toUpperCase() + (likeNum + 1) + " WHERE _ID = "+post_id), ()->{}, error->{Log.d("Update Likes Fail", error.getMessage());});
                }
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getReportFlag() != 0)
                    Toast.makeText(PostDetailUI.this, "Already Reported!", Toast.LENGTH_LONG).show();
                else
                {
                    DB.instance.execute(("update post set report = " + (reportNum + 1) + " where _ID = "+post_id).toUpperCase(),
                            ()->{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(PostDetailUI.this,"Reported!", Toast.LENGTH_LONG).show();
                            }
                        });
                            }, error->{Log.d("Update Reports Fail", error.getMessage());});
                    setReportFlag();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            }
        });

        message.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //check if the account is logged in
                User cur_user = MainActivity.accountMgr.getLoggedInUser();
                if(cur_user == null){
                    Toast.makeText(PostDetailUI.this, "Please log in first!", Toast.LENGTH_LONG).show();
                    Intent toLogin = new Intent(PostDetailUI.this, LoginUI.class);
                    startActivity(toLogin);
                }
                else{
                    MainActivity.chatMgr.startPrivateMessage(cur_user.getUsername(), post_user, ()->{}, error->{Log.d("Message Unable to Start", error.getMessage());} );
                    Intent intent = new Intent(PostDetailUI.this, ChatUsersUI.class);
                    startActivity(intent);
                    //intent.putExtra("chatId", chat.getKey());
                    //intent.putExtra("chatName", holder.username.getText().toString());
                }
            }
        });

        comIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newComments = comments + comment.getText().toString().trim() + '\n';
                DB.instance.execute(("update post set COMMENTS = '" + newComments + "' where _ID = "+post_id),
                        ()->{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PostDetailUI.this, "Commented!", Toast.LENGTH_LONG).show();
                        }
                    });
                }, error->{Log.d("Update Comments Fail", error.getMessage());});
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
    }
}
