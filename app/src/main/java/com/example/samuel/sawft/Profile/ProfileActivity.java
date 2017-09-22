package com.example.samuel.sawft.Profile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.samuel.sawft.Models.Photo;
import com.example.samuel.sawft.Models.User;
import com.example.samuel.sawft.Models.UserDetails;
import com.example.samuel.sawft.R;
import com.example.samuel.sawft.Utils.BottomNavigationHelper;
import com.example.samuel.sawft.Utils.Consts;
import com.example.samuel.sawft.Utils.GridImageAdapter;
import com.example.samuel.sawft.Utils.print;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

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
    private TextView followers,following,posts,name,desc,website,username,edit_profile;
    private ValueEventListener getDetails;
    private FirebaseAuth mAuth;
    private DatabaseReference mRoot;
    private String current_user_id;
    Bundle userStatBundle = new Bundle();
    Bundle userBundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        mAuth = FirebaseAuth.getInstance();
        mRoot = FirebaseDatabase.getInstance().getReference();

        if (mAuth.getCurrentUser() != null) {
            current_user_id = mAuth.getCurrentUser().getUid();
        }
        mRoot.child(Consts.USER_STATUS_KEY).child(current_user_id).keepSynced(true);
        mRoot.child(Consts.USER_PHOTOS_KEY).child(current_user_id).keepSynced(true);
        setUpWidgets();
        setUpBottomNavBar();
        setUpToolbar();   manager = new GridLayoutManager(this,3);
//        recyclerView.setLayoutManager(manager);
//        DividerItemDecoration decor = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
//        recyclerView.addItemDecoration(decor);
//        recyclerView.setAdapter(adapter);


//        GridImageAdapter addapter = new GridImageAdapter(ctx,R.layout.single_image_row);
//        recyclerView.setAdapter(addapter);



//

        setUpGridView();

    }

    private void setUpGridView() {
        final ArrayList<Photo> photoArrayList = new ArrayList<>();
        final ArrayList<Photo> reverse = new ArrayList<>();
        final ArrayList<String> photo_url = new ArrayList<>();

        mRoot.child(Consts.USER_PHOTOS_KEY).child(current_user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snaps : dataSnapshot.getChildren()){
                    Photo photo = snaps.getValue(Photo.class);
                    photoArrayList.add(photo);
                }
                Collections.reverse(photoArrayList);

                for(int i = 0;i<photoArrayList.size();i++){
                    photo_url.add(photoArrayList.get(i).getImage_url());
                    print.l("photo urls :::::::::::::::::::::::::::::::::::::::::; " + photoArrayList.get(i).getImage_url());
                }

                GridImageAdapter adapter = new GridImageAdapter(ProfileActivity.this,
                        R.layout.single_image_row,photo_url,"");
                int col_width = getResources().getDisplayMetrics().widthPixels;
                recyclerView.setColumnWidth((col_width/3));
                recyclerView.setAdapter(adapter);
                print.l("number of photoArrayList ::::::::::::::::::::::::" + photoArrayList.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
       followers = (TextView) findViewById(R.id.tvFollowers);
       following = (TextView) findViewById(R.id.tvFollowing);
       posts = (TextView) findViewById(R.id.tvPost);
       name = (TextView) findViewById(R.id.name);
       desc = (TextView) findViewById(R.id.desc);
       website = (TextView) findViewById(R.id.website);
       username = (TextView) findViewById(R.id.username);
       edit_profile = (TextView) findViewById(R.id.editProfile);
       edit_profile.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent editProf = new Intent(ProfileActivity.this,SettingsActivity.class);
               editProf.putExtra(getString(R.string.calling_activity),getString(R.string.profile_activity));
              editProf.putExtra(getString(R.string.calling_activity), userStatBundle);
               editProf.putExtra(getString(R.string.userBundle),userBundle);
               startActivity(editProf);
           }
       });
   }

   private void getUserStatus(){
       getDetails = new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {

              UserDetails current_user =  dataSnapshot.getValue(UserDetails.class);

               bindViews(current_user);
               User user = dataSnapshot.getValue(User.class);
               sendDetailsToEditProfileFragment(user);
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       };
   }

    private void sendDetailsToEditProfileFragment(User user) {
        userBundle.putString(getString(R.string.email),user.getEmail());
        userBundle.putString(getString(R.string.phone_no),String.valueOf(user.getPhone_number()));

    }

    private void bindViews(final UserDetails current_user) {
        progressBar.setVisibility(View.INVISIBLE);

        posts.setText(String.valueOf(current_user.getPosts()));
        name.setText(current_user.getDisplay_name());
        desc.setText(current_user.getDescription());
        following.setText(String.valueOf(current_user.getFollowing()));
        followers.setText(String.valueOf(current_user.getFollowers()));
        website.setText(current_user.getWebsite());
        username.setText(current_user.getUsername());
        userStatBundle.putString(getString(R.string.username),current_user.getUsername());
        userStatBundle.putString(getString(R.string.display_name),current_user.getDisplay_name());
        userStatBundle.putString(getString(R.string.website),current_user.getWebsite());
        userStatBundle.putString(getString(R.string.description),current_user.getDescription());
        userStatBundle.putString(getString(R.string.userId),current_user_id);
        userStatBundle.putString(getString(R.string.profile_photo),current_user.getProfile_photo());
        Picasso.with(ProfileActivity.this).load(current_user.getProfile_photo()).placeholder(R.drawable.ic_default_avatar)
                .networkPolicy(NetworkPolicy.OFFLINE).resize(200,200).centerCrop()
                .into(profilePhoto, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(ProfileActivity.this).load(current_user.getProfile_photo()).placeholder(R.drawable.ic_default_avatar)
                                .resize(200,200).centerCrop().into(profilePhoto);

                    }
                });
    }


    public void setUpBottomNavBar(){

        BottomNavigationViewEx bottomBar = (BottomNavigationViewEx) findViewById(R.id.bottom_nav_bar);
        BottomNavigationHelper.setUpBottomNavToolBar(bottomBar);
        BottomNavigationHelper.enableNavigation(ProfileActivity.this,this,bottomBar);
        Menu menu = bottomBar.getMenu();
        MenuItem item = menu.getItem(ACTIVITY_NUMBER);
        item.setChecked(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserStatus();
        if(mAuth.getCurrentUser()!=null){
            mRoot.child(Consts.USER_STATUS_KEY).child(current_user_id).addListenerForSingleValueEvent(getDetails);

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuth.getCurrentUser() != null) {
            mRoot.child(Consts.USER_STATUS_KEY).child(current_user_id).removeEventListener(getDetails);
        }
    }
}
