package com.example.samuel.sawft;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.samuel.sawft.Models.Photo;
import com.example.samuel.sawft.Models.UserDetails;
import com.example.samuel.sawft.Profile.ProfileActivity;
import com.example.samuel.sawft.Utils.BottomNavigationHelper;
import com.example.samuel.sawft.Utils.SquareImageView;
import com.example.samuel.sawft.Utils.print;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewPost extends AppCompatActivity {
    private static final int ACTIVITY_NUMBER = 4;
    Photo photo;
    SquareImageView mImage;
    TextView mBAckLabel,mUsername,mCaption,mTimestamp;
    ImageView mBackArrow,mEllipses,mHeartRed,mHeart;
    CircleImageView mProfilePicture;
    UserDetails currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        setUpWidgets();
        if(getIntent()!= null){
            photo = (Photo) getIntent().getExtras().getSerializable("photo");
            currentUser = (UserDetails) getIntent().getExtras().getSerializable("user");
            print.l("                           " + photo.toString());

        }
        mTimestamp.setText(DateUtils.getRelativeTimeSpanString(Long.parseLong(photo.getDate_created())));
        mImage = (SquareImageView) findViewById(R.id.square_image);
        Picasso.with(ViewPost.this).load(photo.getImage_url()).placeholder(R.drawable.ic_default_avatar)
                .networkPolicy(NetworkPolicy.OFFLINE).fit().centerCrop()
                .into(mImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(ViewPost.this).load(photo.getImage_url()).placeholder(R.drawable.ic_default_avatar)
                                .resize(200,200).centerCrop().into(mImage);

                    }
                });
        Picasso.with(ViewPost.this).load(currentUser.getProfile_photo()).placeholder(R.drawable.ic_default_avatar)
                .networkPolicy(NetworkPolicy.OFFLINE).fit().centerCrop()
                .into(mProfilePicture, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(ViewPost.this).load(currentUser.getProfile_photo()).placeholder(R.drawable.ic_default_avatar)
                                .resize(200,200).centerCrop().into(mProfilePicture);

                    }
                });
        mUsername.setText(currentUser.getUsername());
        setUpBottomNavBar();
    }

    private void setUpWidgets() {
        mBackArrow = (ImageView) findViewById(R.id.view_post_back_btn);
        mProfilePicture = (CircleImageView) findViewById(R.id.post_circle);
        mUsername = (TextView) findViewById(R.id.post_user_name);
        mEllipses = (ImageView) findViewById(R.id.post_ellipses);
        mHeart = (ImageView) findViewById(R.id.heart);
        mHeartRed = (ImageView) findViewById(R.id.heart_red);
        mCaption = (TextView) findViewById(R.id.caption);
        mTimestamp = (TextView) findViewById(R.id.image_time_posted);

    }


    public void setUpBottomNavBar(){

        BottomNavigationViewEx bottomBar = (BottomNavigationViewEx) findViewById(R.id.bottom_nav_bar);
        BottomNavigationHelper.setUpBottomNavToolBar(bottomBar);
        BottomNavigationHelper.enableNavigation(ViewPost.this,this,bottomBar);
        Menu menu = bottomBar.getMenu();
        MenuItem item = menu.getItem(ACTIVITY_NUMBER);
        item.setChecked(true);
    }

}
