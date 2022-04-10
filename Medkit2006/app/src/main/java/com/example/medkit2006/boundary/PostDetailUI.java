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

public class PostDetailUI extends AppCompatActivity {
    private int post_id;
    private int likeFlag = 0;
    private int reportFlag = 0;
    private int likeNum = 0;
    private int reportNum = 0;
    private String comments = null;
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
            Toast.makeText(this, "Can't find post!", Toast.LENGTH_SHORT).show();
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
                            title.setText(searchPost.getTitle());
                            date.setText(searchPost.getDate());
                            tags.append(searchPost.getTags());
                            content.setText(searchPost.getContent());
                            likeNum = searchPost.getLikeNum();
                            reportNum = searchPost.getReportNum();
                            numLikes.append("" + likeNum);
                            comments = searchPost.getComments();
                            String items[] = comments.split("\n");
                            ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, items);
                            ListView listView = findViewById(R.id.commList);
                            listView.setAdapter(itemsAdapter);
                        }
                    }
                });
            }, error -> {
                Log.d("Detail Getting Failed", error.getMessage());
            });
        }

        final Button like = findViewById(R.id.like);
        final Button report = findViewById(R.id.report);
        Button comIt = findViewById(R.id.comIt);
        final EditText comment = findViewById(R.id.comment);

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getLikeFlag() == 0) {
                    TextView newNumLikes = (TextView) findViewById(R.id.numLikes);
                    newNumLikes.setText("Likes: " + (likeNum + 1));
                    setLikeFlag();
                    //update the like into database
                    DB.instance.execute(("update post set likes = " + (likeNum + 1) + " where _ID = "+post_id).toUpperCase(), ()->{}, error->{Log.d("Update Likes Fail", error.getMessage());});
                }
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getReportFlag() != 0)
                    Toast.makeText(getApplicationContext(), "Already Reported", Toast.LENGTH_LONG);
                else
                {
                    DB.instance.execute(("update post set reports = " + (reportNum + 1) + " where _ID = "+post_id).toUpperCase(),
                            ()->{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"Reported!", Toast.LENGTH_LONG);
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

        comIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newComments = comments + comment.getText().toString().trim() + '\n';
                DB.instance.execute(("update post set COMMENTS = '" + newComments + "' where _ID = "+post_id),
                        ()->{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Commented!", Toast.LENGTH_SHORT);
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
