package com.example.medkit2006;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.BoardiesITSolutions.AndroidMySQLConnector.ColumnDefinition;
import com.BoardiesITSolutions.AndroidMySQLConnector.Exceptions.SQLColumnNotFoundException;
import com.BoardiesITSolutions.AndroidMySQLConnector.MySQLRow;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);

        button = (Button)findViewById(R.id.btnSearch);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        new DB();

        //Intent intent = new Intent(MainActivity.this, Success.class);
        //startActivity(intent);
        //finish();
    }

    public void onClickSearch(View view){
        Intent intent = new Intent(MainMenu.this, SearchActivity.class);
        startActivity(intent);
    }

    public void onClickForum(View view){
        Intent intent = new Intent(MainMenu.this, ForumActivity.class);
        startActivity(intent);
    }
    public void onClickChat(View view){
        Intent intent = new Intent(MainMenu.this, ChatActivity.class);
        startActivity(intent);
    }
    public void onClickBookmark(View view){
        Intent intent = new Intent(MainMenu.this, BookmarkActivity.class);
        startActivity(intent);
    }
    public void onClickAccount(View view){
        Intent intent = new Intent(MainMenu.this, AccountActivity.class);
        startActivity(intent);
    }

    public void onClickDB(View view) {
        Button btn = ((Button) view);
        DB.instance.conn.returnCallbackToMainThread(true,this);
        try {
            btn.setText(DB.instance.conn.getServerVersion());
        } catch (Exception e) {
            btn.setText(DB.instance.lastMsg);
            return;
        }
        String query = ((EditText) findViewById(R.id.textDBQuery)).getText().toString().trim();
        if (query.contains("SELECT") || query.contains("select"))
            DB.instance.executeQuery(query, resultSet -> {
                try {
                    StringBuilder text = new StringBuilder();
                    ArrayList<String> fields = new ArrayList<>();
                    for (ColumnDefinition field : resultSet.getFields()) {
                        fields.add(field.getColumnName());
                        text.append(" | ").append(field.getColumnName());
                    }
                    text.append('\n');
                    MySQLRow row;
                    while ((row = resultSet.getNextRow()) != null) {
                        for (String field : fields)
                            text.append(" | ").append(row.getString(field));
                        text.append('\n');
                    }
                    btn.setText(text);
                } catch (SQLColumnNotFoundException e) {
                    btn.setText(e.getMessage());
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        else
            DB.instance.execute(query, () -> btn.setText("OK"));
        btn.setText(DB.instance.lastMsg);
    }
}
