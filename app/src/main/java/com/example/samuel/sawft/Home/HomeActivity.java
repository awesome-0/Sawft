package com.example.samuel.sawft.Home;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.samuel.sawft.R;
import com.example.samuel.sawft.Utils.BottomNavigationHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class HomeActivity extends AppCompatActivity {

    private static final int ACTIVITY_NUMBER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setUpBottomNavBar();
        setUpViewPager();
    }

    public void setUpBottomNavBar(){

        BottomNavigationViewEx bottomBar = (BottomNavigationViewEx) findViewById(R.id.bottom_nav_bar);
        BottomNavigationHelper.setUpBottomNavToolBar(bottomBar);
        BottomNavigationHelper.enableNavigation(HomeActivity.this,bottomBar);
        Menu menu = bottomBar.getMenu();
        MenuItem item = menu.getItem(ACTIVITY_NUMBER);
        item.setChecked(true);
    }
    public void setUpViewPager(){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragments(new CameraFragment());
        adapter.addFragments(new HomeFragment());
        adapter.addFragments(new MessagesFragment());

        ViewPager pager = (ViewPager)findViewById(R.id.container);
        pager.setAdapter(adapter);
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(pager);
        tabs.getTabAt(0).setIcon(R.drawable.ic_camera);
        tabs.getTabAt(1).setIcon(R.drawable.ic_house);
        tabs.getTabAt(2).setIcon(R.drawable.ic_send);



    }
}
