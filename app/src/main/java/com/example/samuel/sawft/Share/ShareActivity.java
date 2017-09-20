package com.example.samuel.sawft.Share;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.samuel.sawft.R;
import com.example.samuel.sawft.Utils.BottomNavigationHelper;
import com.example.samuel.sawft.Utils.Permissions;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class ShareActivity extends AppCompatActivity {

    private static final int ACTIVITY_NUMBER = 2;
    private static final int PERMS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
       // setUpBottomNavBar();
        if(checkPerms(Permissions.permissions)){

        }else{
            verifyPerms(Permissions.permissions);
        }

    }

    private void verifyPerms(String[] permissions) {
        ActivityCompat.requestPermissions(ShareActivity.this,permissions,PERMS);
    }

    private boolean checkPerms(String[] permissions) {
        for(String perms : permissions){
            if(ActivityCompat.checkSelfPermission(ShareActivity.this,perms)!= PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    public void setUpBottomNavBar(){

        BottomNavigationViewEx bottomBar = (BottomNavigationViewEx) findViewById(R.id.bottom_nav_bar);
        BottomNavigationHelper.setUpBottomNavToolBar(bottomBar);
        BottomNavigationHelper.enableNavigation(ShareActivity.this,bottomBar);
        Menu menu = bottomBar.getMenu();
        MenuItem item = menu.getItem(ACTIVITY_NUMBER);
        item.setChecked(true);
    }
}
