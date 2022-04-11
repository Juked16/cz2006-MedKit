package com.example.medkit2006;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

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
        setContentView(R.layout.activity_main_menu);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(new ProgressBar(this));

        builder.setCancelable(false); //TODO: uncomment before submitting
        builder.setTitle("Connecting");
        AlertDialog dialog = builder.show();
        new DB(
                ()->{
                    final Runnable runnable = dialog::dismiss;
                    runnable.run();
                    Intent i = new Intent(this, SearchUI.class);
                    startActivity(i);
                    },
                e -> runOnUiThread(() -> {
                    dialog.setTitle("Connection failed");
                })
        );
        DB.instance.conn.returnCallbackToMainThread(true, this);
    }
}
