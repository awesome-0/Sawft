package com.example.samuel.sawft.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.samuel.sawft.Models.Follow;
import com.example.samuel.sawft.Models.Photo;
import com.example.samuel.sawft.Models.User;
import com.example.samuel.sawft.Models.UserDetails;
import com.example.samuel.sawft.R;
import com.example.samuel.sawft.Utils.BottomNavigationHelper;
import com.example.samuel.sawft.Utils.Consts;
import com.example.samuel.sawft.Utils.GridImageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
    private TextView mfollowers, mfollowing, mPosts, name, desc, website, username, edit_profile;
    private ValueEventListener getDetails;
    private FirebaseAuth mAuth;
    private DatabaseReference mRoot;
    private String user_id;
    Bundle userStatBundle = new Bundle();
    Bundle userBundle = new Bundle();
    UserDetails current_user;
    int mNumFollowers,mNumFollowing,mNumPosts = 0;

    boolean mFromSearch;
    User searchedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        mAuth = FirebaseAuth.getInstance();
        mRoot = FirebaseDatabase.getInstance().getReference();


        if (getIntent() != null) {
            if (getIntent().hasExtra(Consts.USERS_KEY)) {
                searchedUser = (User) getIntent().getExtras().getSerializable(Consts.USERS_KEY);
                mFromSearch = true;
                Log.e(TAG, "onCreate:                          coming from search activity");

                user_id = searchedUser.getUser_id();
                Log.e(TAG, "onResume: found user " + searchedUser.toString());
            } else {
                mFromSearch = false;
                if (mAuth.getCurrentUser() != null) {
                    if (mAuth.getCurrentUser() != null) {
                        user_id = mAuth.getCurrentUser().getUid();
                        mRoot.child(Consts.USER_STATUS_KEY).child(user_id).keepSynced(true);
                        mRoot.child(Consts.USER_PHOTOS_KEY).child(user_id).keepSynced(true);
                    }

                }
                Log.e(TAG, "onCreate:                          not from search activity");
            }
        }


        setUpWidgets();
        setProfileDetails();
        setUpBottomNavBar();
        setUpToolbar();
        manager = new GridLayoutManager(this, 3);
        setUpGridView(user_id);


    }
    private void setProfileDetails(){
        if (getIntent() != null) {
            if (!mFromSearch) {
                Query followingquery = mRoot.child(Consts.USERS_KEY).child(mAuth.getCurrentUser().getUid())
                        .child(Consts.FOLLOWING);
                followingquery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mNumFollowing = (int) dataSnapshot.getChildrenCount();
                        mfollowing.setText(String.valueOf(mNumFollowing));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Query Followersquery = mRoot.child(Consts.USERS_KEY).child(mAuth.getCurrentUser().getUid())
                        .child(Consts.FOLLOWERS);
                Followersquery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mNumFollowers = (int) dataSnapshot.getChildrenCount();
                        mfollowers.setText(String.valueOf(mNumFollowers));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Query postQuery = mRoot.child(Consts.USER_PHOTOS_KEY).child(mAuth.getCurrentUser().getUid());
                postQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mNumPosts = (int) dataSnapshot.getChildrenCount();
                        mPosts.setText(String.valueOf(mNumPosts));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
            else {
                Query followingquery = mRoot.child(Consts.USERS_KEY).child(searchedUser.getUser_id())
                        .child(Consts.FOLLOWING);
                followingquery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mNumFollowing = (int) dataSnapshot.getChildrenCount();
                        mfollowing.setText(String.valueOf(mNumFollowing));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Query Followersquery = mRoot.child(Consts.USERS_KEY).child(searchedUser.getUser_id())
                        .child(Consts.FOLLOWERS);
                Followersquery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mNumFollowers = (int) dataSnapshot.getChildrenCount();
                        mfollowers.setText(String.valueOf(mNumFollowers));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Query postQuery = mRoot.child(Consts.USER_PHOTOS_KEY).child(searchedUser.getUser_id());
                postQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mNumPosts = (int) dataSnapshot.getChildrenCount();
                        mPosts.setText(String.valueOf(mNumPosts));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
    }

    private void setUpGridView(String Uid) {
        final ArrayList<Photo> photoArrayList = new ArrayList<>();
        final ArrayList<Photo> reverse = new ArrayList<>();
        final ArrayList<String> photo_url = new ArrayList<>();

        mRoot.child(Consts.USER_PHOTOS_KEY).child(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snaps : dataSnapshot.getChildren()) {
                    Photo photo = snaps.getValue(Photo.class);
                    photoArrayList.add(photo);
                    reverse.add(photo);

                }

                Collections.reverse(photoArrayList);

                for (int i = 0; i < photoArrayList.size(); i++) {
                    photo_url.add(photoArrayList.get(i).getImage_url());
                }

                GridImageAdapter adapter = new GridImageAdapter(ProfileActivity.this,
                        R.layout.single_image_row, photo_url, "");
                int col_width = getResources().getDisplayMetrics().widthPixels;
                recyclerView.setColumnWidth((col_width / 3));
                recyclerView.setAdapter(adapter);
                recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(ProfileActivity.this, ViewPost.class);
                        intent.putExtra("photo", photoArrayList.get(i));
                        intent.putExtra("user", current_user);
                        startActivity(intent);
                    }
                });
                // print.l("number of photoArrayList ::::::::::::::::::::::::" + photoArrayList.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void setUpToolbar() {
        Toolbar bar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(bar);
        ImageView back_btn = (ImageView) findViewById(R.id.profile_to_settings);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsIntent = new Intent(ProfileActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
            }
        });
    }

    private void setUpWidgets() {
        profilePhoto = (CircleImageView) findViewById(R.id.profile_picture);
        recyclerView = (GridView) findViewById(R.id.grid_view);
        adapter = new GridAdapter(ProfileActivity.this);
        mfollowers = (TextView) findViewById(R.id.tvFollowers);
        mfollowing = (TextView) findViewById(R.id.tvFollowing);
        mPosts = (TextView) findViewById(R.id.tvPost);
        name = (TextView) findViewById(R.id.name);
        desc = (TextView) findViewById(R.id.desc);
        website = (TextView) findViewById(R.id.website);
        username = (TextView) findViewById(R.id.username);
        edit_profile = (TextView) findViewById(R.id.editProfile);
        Log.e(TAG, "setUpWidgets: value of mFromSearch" + String.valueOf(mFromSearch));
        if (mFromSearch) {
            Query following = mRoot.child(Consts.USERS_KEY).child(mAuth.getCurrentUser().getUid())
                    .child(Consts.FOLLOWING).orderByChild(Consts.USER_ID)
                    .equalTo(searchedUser.getUser_id());
            following.keepSynced(true);
            following.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(!dataSnapshot.exists()){
                                Log.e(TAG, "onDataChange: you are not mfollowing this person" );
                                edit_profile.setText("Follow");
                                edit_profile.setBackgroundColor(getResources().getColor(R.color.blue));
                                edit_profile.setTextColor(getResources().getColor(R.color.white));
                            }
                            else {
                                Log.e(TAG, "onDataChange: yes you are mfollowing person" );
                                edit_profile.setText("UnFollow");
                                edit_profile.setBackgroundColor(getResources().getColor(R.color.red));
                                edit_profile.setTextColor(getResources().getColor(R.color.white));
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


        }
        else{
            edit_profile.setText("Edit Profile");
           // edit_profile.setBackgroundColor(getResources().getColor(R.color.white));
            edit_profile.setTextColor(getResources().getColor(R.color.black));
        }
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit_profile.getText().toString().toLowerCase().contains("edit profile")) {
                    Intent editProf = new Intent(ProfileActivity.this, SettingsActivity.class);
                    editProf.putExtra(getString(R.string.calling_activity), getString(R.string.profile_activity));
                    editProf.putExtra(getString(R.string.calling_activity), userStatBundle);
                    editProf.putExtra(getString(R.string.userBundle), userBundle);
                    startActivity(editProf);
                } else if(edit_profile.getText().toString().toLowerCase().equals("follow")){
                    Log.e(TAG, "onClick: mfollowing user now");
                    followUser();


                }
                else if(edit_profile.getText().toString().toLowerCase().equals("unfollow")){
                    Log.e(TAG, "onClick: Unfollowing user" );
                    unfollowUser();
                }
            }
        });
    }

    private void unfollowUser() {
        DatabaseReference following = mRoot.child(Consts.USERS_KEY).child(mAuth.getCurrentUser().getUid())
                .child(Consts.FOLLOWING);

        Log.e(TAG, "unfollowUser: searched user id is " +searchedUser.getUser_id()  );
        following.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singlesnapshot : dataSnapshot.getChildren()){
                    if(singlesnapshot.getValue(Follow.class).getUser_id().equals(searchedUser.getUser_id())){
                        String keyId = singlesnapshot.getKey();
                        Log.e(TAG, "onDataChange: key id to unfollow user " + keyId );
                        mRoot.child(Consts.USERS_KEY).child(mAuth.getCurrentUser().getUid())
                                .child(Consts.FOLLOWING).child(keyId)
                                .removeValue();
                        mRoot.child(Consts.USERS_KEY).child(searchedUser.getUser_id())
                                .child(Consts.FOLLOWERS).child(keyId)
                                .removeValue();
                        edit_profile.setText("Follow");
                        edit_profile.setBackgroundColor(getResources().getColor(R.color.blue));
                        edit_profile.setTextColor(getResources().getColor(R.color.white));

                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void followUser() {
        String pushId = mRoot.push().getKey();
        String currentUser = mAuth.getCurrentUser().getUid();
       // Follow follow = new Follow(currentUser);

        mRoot.child(Consts.USERS_KEY).child(mAuth.getCurrentUser().getUid())
                .child(Consts.FOLLOWING).child(pushId)
                .setValue(new Follow(searchedUser.getUser_id()));
        mRoot.child(Consts.USERS_KEY).child(searchedUser.getUser_id())
                .child(Consts.FOLLOWERS).child(pushId)
                .setValue(new Follow(currentUser));
        edit_profile.setText("UnFollow");
        edit_profile.setBackgroundColor(getResources().getColor(R.color.red));
        edit_profile.setTextColor(getResources().getColor(R.color.white));
    }

    private void getUserStatus() {
        getDetails = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                current_user = dataSnapshot.getValue(UserDetails.class);

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
        userBundle.putString(getString(R.string.email), user.getEmail());
        userBundle.putString(getString(R.string.phone_no), String.valueOf(user.getPhone_number()));

    }

    private void bindViews(final UserDetails current_user) {
        progressBar.setVisibility(View.INVISIBLE);
        name.setText(current_user.getDisplay_name());
        desc.setText(current_user.getDescription());
        website.setText(current_user.getWebsite());
        username.setText(current_user.getUsername());
        userStatBundle.putString(getString(R.string.username), current_user.getUsername());
        userStatBundle.putString(getString(R.string.display_name), current_user.getDisplay_name());
        userStatBundle.putString(getString(R.string.website), current_user.getWebsite());
        userStatBundle.putString(getString(R.string.description), current_user.getDescription());
        userStatBundle.putString(getString(R.string.userId), user_id);
        userStatBundle.putString(getString(R.string.profile_photo), current_user.getProfile_photo());
        Picasso.with(ProfileActivity.this).load(current_user.getProfile_photo()).placeholder(R.drawable.ic_default_avatar)
                .networkPolicy(NetworkPolicy.OFFLINE).resize(200, 200).centerCrop()
                .into(profilePhoto, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(ProfileActivity.this).load(current_user.getProfile_photo()).placeholder(R.drawable.ic_default_avatar)
                                .resize(200, 200).centerCrop().into(profilePhoto);

                    }
                });
    }


    public void setUpBottomNavBar() {

        BottomNavigationViewEx bottomBar = (BottomNavigationViewEx) findViewById(R.id.bottom_nav_bar);
        BottomNavigationHelper.setUpBottomNavToolBar(bottomBar);
        BottomNavigationHelper.enableNavigation(ProfileActivity.this, this, bottomBar);
        Menu menu = bottomBar.getMenu();
        MenuItem item = menu.getItem(ACTIVITY_NUMBER);
        item.setChecked(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserStatus();
        if(mAuth.getCurrentUser()!= null){
            mRoot.child(Consts.USER_STATUS_KEY).child(user_id).addListenerForSingleValueEvent(getDetails);
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuth.getCurrentUser() != null) {
            mRoot.child(Consts.USER_STATUS_KEY).child(user_id).removeEventListener(getDetails);
        }
    }
}
