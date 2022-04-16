package com.example.medkit2006.boundary;

import static com.example.medkit2006.R.string.open;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.medkit2006.adapter.FeedAdapter;
import com.example.medkit2006.MainActivity;
import com.example.medkit2006.R;
import com.example.medkit2006.entity.Post;
import com.example.medkit2006.entity.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class ForumUI extends AppCompatActivity {

    public static String EXTRA = "post_id";
    public static String USER_EXTRA = "username";
    private ActionBarDrawerToggle mToggle;
    private ListView listView;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        //Set up bottom navigation bar
        BottomNavigationView btmNav = findViewById(R.id.navigation);
        btmNav.getMenu().clear();
        btmNav.inflateMenu(R.menu.bottom_navigation);
        btmNav.setOnItemSelectedListener(item -> {
            Intent i;
            switch (item.getItemId()) {
                case R.id.nav_search:
                    i = new Intent(getApplicationContext(), SearchUI.class);
                    startActivity(i);
                    break;
                case R.id.nav_forum:
                    i = new Intent(getApplicationContext(), ForumUI.class);
                    startActivity(i);
                    break;
                case R.id.nav_account:
                    i = new Intent(getApplicationContext(), MainActivity.accountMgr.isLoggedIn()? AccountUI.class: LoginUI.class);
                    startActivity(i);
                    break;
            }
            return false;
        });

        DrawerLayout mDrawerLayout = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        listView = findViewById(R.id.list);
        setTitle("Welcome " + getUser());
        //search database below
        MainActivity.forumMgr.getAllPostAbstract(postList -> {
            Log.d("Received Post List", String.valueOf(postList.size()));
            runOnUiThread(() -> {// Stuff that updates the UI
                FeedAdapter itemsAdapter = new FeedAdapter(ForumUI.this, postList);
                listView.setAdapter(itemsAdapter);
            });
        }, e -> {
                    Log.d("Received Post List Unsuccessful", e.toString().trim());
                    runOnUiThread(() -> {// Stuff that updates the UI
                        Toast.makeText(getApplicationContext(), e.toString().trim(), Toast.LENGTH_SHORT).show();
                    });
        });

        listView.setOnItemClickListener((adapter, v, position, arg3) -> {
            Intent i = new Intent(ForumUI.this, PostDetailUI.class);
            Post selectedFromList = (Post)(listView.getItemAtPosition(position));
            i.putExtra(ForumUI.EXTRA, selectedFromList.getID());
            startActivity(i);
        });

        NavigationView navigation = findViewById(R.id.nav_view);
        navigation.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            Intent i;
            switch (id) {
                case R.id.drafts:
                    i = new Intent(ForumUI.this, MainActivity.accountMgr.isLoggedIn() ? PostDraftUI.class : LoginUI.class);
                    i.putExtra(USER_EXTRA, getUser());
                    startActivity(i);
                    break;
                case R.id.myPost:
                    i = new Intent(ForumUI.this, MainActivity.accountMgr.isLoggedIn() ? MyPostUI.class : LoginUI.class);
                    i.putExtra(USER_EXTRA, getUser());
                    startActivity(i);
                    break;
                case R.id.goChat:
                    i = new Intent(ForumUI.this, MainActivity.accountMgr.isLoggedIn() ? ChatUsersUI.class : LoginUI.class);
                    i.putExtra(USER_EXTRA, getUser());
                    startActivity(i);
                    break;
                case R.id.goAccount:
                    i = new Intent(ForumUI.this, MainActivity.accountMgr.isLoggedIn() ? AccountUI.class : LoginUI.class);
                    i.putExtra(USER_EXTRA, getUser());
                    startActivity(i);
                    break;
            }
            return false;
        });
        View hView =  navigation.getHeaderView(0);
        TextView name = hView.findViewById(R.id.userName);
        name.setText(getUser());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //refresh page
        MainActivity.forumMgr.getAllPostAbstract(postList -> {
            Log.d("Received Post List", String.valueOf(postList.size()));
            runOnUiThread(() -> {// Stuff that updates the UI
                FeedAdapter itemsAdapter = new FeedAdapter(ForumUI.this, postList);
                listView.setAdapter(itemsAdapter);
            });
        }, e -> {
            Log.d("Received Post List Unsuccessful", e.toString().trim());
            runOnUiThread(() -> {// Stuff that updates the UI
                Toast.makeText(getApplicationContext(), e.toString().trim(), Toast.LENGTH_SHORT).show();
            });
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forum_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        if(item.getItemId() == R.id.action_new_post) {
            goEdit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goEdit()
    {
        Intent i;
        if(!MainActivity.accountMgr.isLoggedIn()) {
            Toast.makeText(ForumUI.this, "Please Login First!", Toast.LENGTH_LONG).show();
            i = new Intent(ForumUI.this, LoginUI.class);
        }
        else {
            i = new Intent(ForumUI.this, PostEditorUI.class);
            i.putExtra(ForumUI.USER_EXTRA, getUser());
        }
        startActivity(i);
    }

    public String getUser() {
        User cur_user = MainActivity.accountMgr.getLoggedInUser();
        return( cur_user == null ? "" : cur_user.getUsername());
    }
}
