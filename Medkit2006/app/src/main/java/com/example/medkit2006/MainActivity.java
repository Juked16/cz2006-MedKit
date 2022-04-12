package com.example.medkit2006;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medkit2006.boundary.SearchUI;
import com.example.medkit2006.control.AccountMgr;
import com.example.medkit2006.control.BookmarkMgr;
import com.example.medkit2006.control.ChatMgr;
import com.example.medkit2006.control.ForumMgr;
import com.example.medkit2006.control.MedicalFacilityMgr;

public class MainActivity extends AppCompatActivity {
    public static AccountMgr accountMgr = new AccountMgr();
    public static MedicalFacilityMgr facilityMgr = new MedicalFacilityMgr();
    public static ForumMgr forumMgr = new ForumMgr();
    public static BookmarkMgr bookmarkMgr = new BookmarkMgr();
    public static ChatMgr chatMgr = new ChatMgr();
    private boolean displayed = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);
        new DB(
                ()-> startActivity(new Intent(this, SearchUI.class)),
                e -> runOnUiThread(() -> new AlertDialog.Builder(this)
                        .setTitle("Connection Failed")
                        .setMessage("Please check your internet connection and restart the app")
                        .setOnDismissListener(dialog -> finishAffinity())
                        .show()
                )
        );
        DB.instance.conn.returnCallbackToMainThread(true, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(displayed)
            finishAffinity();
        displayed = true;
    }
}
