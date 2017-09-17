package com.example.samuel.sawft.Profile;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.samuel.sawft.R;
import com.example.samuel.sawft.Utils.BottomNavigationHelper;
import com.example.samuel.sawft.Utils.SectionStatePagerAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    private static final int ACTIVITY_NUMBER = 4;
    private SectionStatePagerAdapter pagerAdapter;
    private ViewPager pager;
    private RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setUpListView();
        setUpFragments();
        setUpBottomNavBar();
        layout = (RelativeLayout) findViewById(R.id.relLayout);
        pager = (ViewPager) findViewById(R.id.container);

        ImageView back_btn = (ImageView) findViewById(R.id.edit_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setUpListView(){
        ListView listView = (ListView) findViewById(R.id.settings_listview);
        ArrayList<String>options = new ArrayList<>();
        options.add(getString(R.string.edit_profile));
        options.add(getString(R.string.sign_out));
        ArrayAdapter adapter = new ArrayAdapter(SettingsActivity.this,android.R.layout.simple_list_item_1,options);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setUpViewPager(i);
            }
        });


    }
    private void setUpFragments(){
       pagerAdapter = new SectionStatePagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragments(new EditProfileFragment(),getString(R.string.edit_profile));
        pagerAdapter.addFragments(new SignOutFragment(),getString(R.string.sign_out));
    }

    private void setUpViewPager(int position){
        layout.setVisibility(View.GONE);
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(position);
    }
    public void setUpBottomNavBar(){

        BottomNavigationViewEx bottomBar = (BottomNavigationViewEx) findViewById(R.id.bottom_nav_bar);
        BottomNavigationHelper.setUpBottomNavToolBar(bottomBar);
        BottomNavigationHelper.enableNavigation(SettingsActivity.this,bottomBar);
        Menu menu = bottomBar.getMenu();
        MenuItem item = menu.getItem(ACTIVITY_NUMBER);
        item.setChecked(true);
    }
}
