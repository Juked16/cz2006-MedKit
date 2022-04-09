package com.example.medkit2006.boundary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.BoardiesITSolutions.AndroidMySQLConnector.ColumnDefinition;
import com.BoardiesITSolutions.AndroidMySQLConnector.Exceptions.SQLColumnNotFoundException;
import com.BoardiesITSolutions.AndroidMySQLConnector.MySQLRow;
import com.example.medkit2006.MainActivity;
import com.example.medkit2006.R;
import com.example.medkit2006.data.DB;

import java.util.ArrayList;

public class MainMenuUI extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);

        new DB();
        DB.instance.conn.returnCallbackToMainThread(true, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(MainActivity.accountMgr.isLoggedIn()) {

            findViewById(R.id.mainChatBtn).setVisibility(View.VISIBLE);
            findViewById(R.id.mainChatBtn).setOnClickListener(btn -> {
                Intent intent = new Intent(this, ChatUI.class);
                startActivity(intent);
            });

            findViewById(R.id.bookmark_btn).setVisibility(View.VISIBLE);
            findViewById(R.id.bookmark_btn).setOnClickListener(btn -> {
                Intent intent = new Intent(this, BookmarkUI.class);
                startActivity(intent);
            });
        }
    }

    public void onClickSearch(View view) {
        Intent intent = new Intent(MainMenuUI.this, SearchUI.class);
        startActivity(intent);
    }

    public void onClickForum(View view) {
        Intent intent = new Intent(MainMenuUI.this, ForumUI.class);
        startActivity(intent);
    }

    public void onClickChat(View view) {
        Intent intent = new Intent(MainMenuUI.this, ChatUsersUI.class);
        startActivity(intent);
    }

    public void onClickBookmark(View view) {
        Intent intent = new Intent(MainMenuUI.this, BookmarkUI.class);
        startActivity(intent);
    }

    public void onClickAccount(View view) {
        Intent intent = new Intent(MainMenuUI.this, AccountUI.class);
        startActivity(intent);
    }

    public void onClickDB(View view) {
        Button btn = ((Button) view);
        btn.setText("Executing");
        String query = ((EditText) findViewById(R.id.mainDBQuery)).getText().toString().trim();
        if (query.toUpperCase().contains("SELECT"))
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
            }
        }, e -> btn.setText(e.getMessage()));
        else
        DB.instance.execute(query, () -> btn.setText("OK"), e -> btn.setText(e.getMessage()));
    }
}
