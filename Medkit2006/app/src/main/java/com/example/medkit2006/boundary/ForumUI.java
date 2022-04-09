package com.example.medkit2006.boundary;

import static com.example.medkit2006.R.string.open;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.medkit2006.FeedAdapter;
import com.example.medkit2006.MainActivity;
import com.example.medkit2006.R;
import com.example.medkit2006.entity.Post;
import com.example.medkit2006.entity.User;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class ForumUI extends AppCompatActivity {

    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this,mDrawerlayout, open,R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Welcome " + getUser());
        ArrayList<Post> words = new ArrayList<Post>();
        //search database below

        FeedAdapter itemsAdapter = new FeedAdapter(this,words);
        final ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
            {
                Intent i = new Intent(ForumUI.this, PostDetailUI.class);
                Post selectedFromList = (Post)(listView.getItemAtPosition(position));
                i.putExtra("date" , selectedFromList.getDate());
                startActivity(i);
            }
        });

        listView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        navigation = (NavigationView) findViewById(R.id.nav_view);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                Intent i;
                switch (id) {
                    case R.id.drafts:
                        i = new Intent(ForumUI.this, MainActivity.accountMgr.isLoggedIn() ? PostDraftUI.class : LoginUI.class);
                        i.putExtra("username", getUser());
                        startActivity(i);
                        break;
                    case R.id.myPost:
                        i = new Intent(ForumUI.this, MainActivity.accountMgr.isLoggedIn() ? MyPostUI.class : LoginUI.class);
                        i.putExtra("username", getUser());
                        startActivity(i);
                }
                return false;
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        TextView name;
        name = (TextView)hView.findViewById(R.id.userName);
        name.setText(getUser());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.feed_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        if(mToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_new_post:
                // Do nothing for now
                goEdit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goEdit()
    {
        Intent i = new Intent(ForumUI.this, MainActivity.accountMgr.isLoggedIn() ? PostEditorUI.class : LoginUI.class);
        i.putExtra("username", getUser());
        startActivity(i);
    }

    public String getUser() {
        User cur_user = MainActivity.accountMgr.getLoggedInUser();
        return( cur_user == null ? "" : cur_user.getUsername());
    }
}
