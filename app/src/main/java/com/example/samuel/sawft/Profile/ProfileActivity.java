package com.example.samuel.sawft.Profile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.samuel.sawft.GridImageAdapter;
import com.example.samuel.sawft.R;
import com.example.samuel.sawft.Utils.BottomNavigationHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    Context ctx = ProfileActivity.this;

    private static final int ACTIVITY_NUMBER = 4;
    private static final String TAG = "ProfileActivity";
    private ProgressBar progressBar;
    private CircleImageView profilePhoto;
    private String dummy = "https://thumbs.dreamstime.com/z/profile-icon-male-avatar-man-hipster-style-fashion-cartoon-guy-beard-glasses-portrait-casual-person-silhouette-face-flat-design-62449823.jpg";

    private GridAdapter adapter;
    private GridView recyclerView;
    private GridLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        setUpBottomNavBar();
        setUpToolbar();   manager = new GridLayoutManager(this,3);
//        recyclerView.setLayoutManager(manager);
//        DividerItemDecoration decor = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
//        recyclerView.addItemDecoration(decor);
//        recyclerView.setAdapter(adapter);
        setUpWidgets();
        GridImageAdapter addapter = new GridImageAdapter(ctx,R.layout.single_image_row);
        recyclerView.setAdapter(addapter);

//
        Picasso.with(ctx).load(dummy).placeholder(R.drawable.ic_default_avatar)
                .fit().centerCrop().into(profilePhoto, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

                Picasso.with(ctx).load(dummy).placeholder(R.drawable.ic_default_avatar).fit().centerCrop()
                        .into(profilePhoto);
            }
        });

    }

   public void setUpToolbar(){
       Toolbar bar = (Toolbar) findViewById(R.id.profile_toolbar);
       setSupportActionBar(bar);
       ImageView back_btn = (ImageView)findViewById(R.id.profile_to_settings);
       back_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent settingsIntent =new Intent(ProfileActivity.this, SettingsActivity.class);
               startActivity(settingsIntent);
           }
       });
   }
   private void setUpWidgets(){
       profilePhoto = (CircleImageView) findViewById(R.id.profile_picture);
       recyclerView = (GridView) findViewById(R.id.grid_view);
       adapter = new GridAdapter(ProfileActivity.this);


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
