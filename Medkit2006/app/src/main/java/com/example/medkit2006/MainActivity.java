package com.example.medkit2006;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.BoardiesITSolutions.AndroidMySQLConnector.ColumnDefinition;
import com.BoardiesITSolutions.AndroidMySQLConnector.Exceptions.SQLColumnNotFoundException;
import com.BoardiesITSolutions.AndroidMySQLConnector.MySQLRow;
import com.example.medkit2006.boundary.AccountUI;
import com.example.medkit2006.boundary.BookmarkUI;
import com.example.medkit2006.boundary.ChatUsersUI;
import com.example.medkit2006.boundary.ForumUI;
import com.example.medkit2006.boundary.LoginUI;
import com.example.medkit2006.boundary.SearchUI;
import com.example.medkit2006.control.AccountMgr;
import com.example.medkit2006.control.BookmarkMgr;
import com.example.medkit2006.control.ChatMgr;
import com.example.medkit2006.control.ForumMgr;
import com.example.medkit2006.control.MedicalFacilityMgr;
import com.example.medkit2006.data.DB;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static AccountMgr accountMgr = new AccountMgr();
    public static MedicalFacilityMgr facilityMgr = new MedicalFacilityMgr();
    public static ForumMgr forumMgr = new ForumMgr();
    public static BookmarkMgr bookmarkMgr = new BookmarkMgr();
    public static ChatMgr chatMgr = new ChatMgr();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(new ProgressBar(this));
        //builder.setCancelable(false); //TODO: uncomment before submitting
        builder.setTitle("Connecting");
        AlertDialog dialog = builder.show();
        new DB(
                dialog::dismiss,
                e -> runOnUiThread(() -> {
                    dialog.setTitle("Connection failed");
                })
        );
        DB.instance.conn.returnCallbackToMainThread(true, this);
    }

    public void onClickSearch(View view) {
        Intent intent = new Intent(MainActivity.this, SearchUI.class);
        startActivity(intent);
    }

    public void onClickForum(View view) {
        Intent intent = new Intent(MainActivity.this, ForumUI.class);
        startActivity(intent);
    }

    public void onClickChat(View view) {
        Intent intent = new Intent(MainActivity.this, accountMgr.isLoggedIn() ? ChatUsersUI.class : LoginUI.class);
        startActivity(intent);
    }

    public void onClickBookmark(View view) {
        Intent intent = new Intent(MainActivity.this, accountMgr.isLoggedIn() ? BookmarkUI.class : LoginUI.class);
        startActivity(intent);
    }

    public void onClickAccount(View view) {
        Intent intent = new Intent(MainActivity.this, AccountUI.class);
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
