package com.example.samuel.sawft.Profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
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

import com.example.samuel.sawft.Home.HomeActivity;
import com.example.samuel.sawft.LoginActivity;
import com.example.samuel.sawft.R;
import com.example.samuel.sawft.RegisterActivity;
import com.example.samuel.sawft.Utils.BottomNavigationHelper;
import com.example.samuel.sawft.Utils.SectionStatePagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    private static final int ACTIVITY_NUMBER = 4;
    private SectionStatePagerAdapter pagerAdapter;
    private ViewPager pager;
    private RelativeLayout layout;
    private FirebaseAuth mAuth;
    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setUpListView();
        setUpFragments();
        setUpBottomNavBar();
        mAuth = FirebaseAuth.getInstance();
        layout = (RelativeLayout) findViewById(R.id.relLayout);
        pager = (ViewPager) findViewById(R.id.container);
        getFragPosition();

        ImageView back_btn = (ImageView) findViewById(R.id.edit_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getFragPosition() {
        Intent intent =  getIntent();
        if(intent.hasExtra(getString(R.string.calling_activity))){
            setUpViewPager(0);
            bundle = intent.getBundleExtra(getString(R.string.calling_activity));


        }
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
                switch(i){
                    case 0:
                        setUpViewPager(i);
                        break;
                    case 1:
                        confirmSignOut();
                        break;

                }
            }
        });


    }

    public void confirmSignOut(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setTitle("Sure to sign out?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(mAuth != null){
                    mAuth.signOut();
                    Intent login = new Intent(SettingsActivity.this, LoginActivity.class);
                    login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(login);
                    dialogInterface.dismiss();
                }

            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

            }
        });
        builder.create().show();

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
