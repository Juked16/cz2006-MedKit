package com.example.medkit2006.boundary;

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
import com.example.medkit2006.BookmarkActivity;
import com.example.medkit2006.DB;
import com.example.medkit2006.MainActivity;
import com.example.medkit2006.R;

import java.util.ArrayList;

public class MainMenuUI extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);

        button = (Button) findViewById(R.id.btnSearch);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuUI.this, SearchUI.class);
                startActivity(intent);
            }
        });

        new DB();

        //Intent intent = new Intent(MainActivity.this, Success.class);
        //startActivity(intent);
        //finish();
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
        Intent intent = new Intent(MainMenuUI.this, MainActivity.accountMgr.isLoggedIn() ? BookmarkActivity.class : LoginUI.class);
        startActivity(intent);
    }

    public void onClickAccount(View view) {
        Intent intent = new Intent(MainMenuUI.this, AccountUI.class);
        startActivity(intent);
    }

    public void onClickDB(View view) {
        Button btn = ((Button) view);
        try {
            DB.instance.conn.returnCallbackToMainThread(true, this);
        } catch (Exception e) {
            btn.setText(DB.instance.lastMsg);
            return;
        }
        btn.setText("Executing");
        String query = ((EditText) findViewById(R.id.textDBQuery)).getText().toString().trim();
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
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }, e -> btn.setText(e.getMessage()));
        else
            DB.instance.execute(query, () -> btn.setText("OK"));
    }
}
