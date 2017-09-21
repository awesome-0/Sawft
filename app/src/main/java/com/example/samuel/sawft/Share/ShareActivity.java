package com.example.samuel.sawft.Share;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.samuel.sawft.Home.*;
import com.example.samuel.sawft.R;
import com.example.samuel.sawft.Utils.BottomNavigationHelper;
import com.example.samuel.sawft.Utils.Permissions;
import com.example.samuel.sawft.Utils.print;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class ShareActivity extends AppCompatActivity {

    private static final int ACTIVITY_NUMBER = 2;
    private static final int PERMS = 1;
    ViewPager container ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
       // setUpBottomNavBar();
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            setUpViewPager();
        }
        else {
            if (checkPerms(Permissions.permissions)) {
                setUpViewPager();

            } else {
                verifyPerms(Permissions.permissions);

            }
        }



    }
    public int getCurrentToolbarNumber(){
        return container.getCurrentItem();
    }

    private void verifyPerms(String[] permissions) {
        ActivityCompat.requestPermissions(ShareActivity.this,permissions,PERMS);
    }
    private void setUpViewPager(){
        container = (ViewPager) findViewById(R.id.container);
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragments(new GalleryFragment());
        adapter.addFragments(new CameraShareFragment());
        TabLayout layout = (TabLayout) findViewById(R.id.share_bottom_tab);
        container.setAdapter(adapter);
        layout.setupWithViewPager(container);

        layout.getTabAt(0).setText("GALLERY");
        layout.getTabAt(1).setText("CAMERA");

    }

    public boolean checkPerms(String[] permissions) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMS){
            setUpViewPager();
        }
        else{
            print.t(ShareActivity.this, "permissions denied");
        }

    }
}
