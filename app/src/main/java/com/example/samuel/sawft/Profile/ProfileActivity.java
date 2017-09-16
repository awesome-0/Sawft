package com.example.samuel.sawft.Profile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.samuel.sawft.R;
import com.example.samuel.sawft.Utils.BottomNavigationHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class ProfileActivity extends AppCompatActivity {

    private static final int ACTIVITY_NUMBER = 4;
    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setUpBottomNavBar();
        setUpToolbar();

    }

   public void setUpToolbar(){
       Toolbar bar = (Toolbar) findViewById(R.id.profile_toolbar);
       setSupportActionBar(bar);
       bar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
           @Override
           public boolean onMenuItemClick(MenuItem item) {

               switch (item.getItemId()){
                   case R.id.profile_Activity_menu:
                       Log.d(TAG, "onMenuItemClick: profile menu clicked");

                       break;

               }
               return true;
           }
       });




   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return true;
    }

    public void setUpBottomNavBar(){

        BottomNavigationViewEx bottomBar = (BottomNavigationViewEx) findViewById(R.id.bottom_nav_bar);
        BottomNavigationHelper.setUpBottomNavToolBar(bottomBar);
        BottomNavigationHelper.enableNavigation(ProfileActivity.this,bottomBar);
        Menu menu = bottomBar.getMenu();
        MenuItem item = menu.getItem(ACTIVITY_NUMBER);
        item.setChecked(true);
    }
}
