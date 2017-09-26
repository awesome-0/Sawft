package com.example.samuel.sawft.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.samuel.sawft.Models.Like;
import com.example.samuel.sawft.Models.Photo;
import com.example.samuel.sawft.Models.User;
import com.example.samuel.sawft.Models.UserDetails;
import com.example.samuel.sawft.R;
import com.example.samuel.sawft.Utils.BottomNavigationHelper;
import com.example.samuel.sawft.Utils.Consts;
import com.example.samuel.sawft.Utils.Heart;
import com.example.samuel.sawft.Utils.SquareImageView;
import com.example.samuel.sawft.Utils.print;
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

import org.w3c.dom.Comment;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewPost extends AppCompatActivity {
    private static final int ACTIVITY_NUMBER = 4;
    Photo photo;
    SquareImageView mImage;
    TextView mBAckLabel, mUsername, mCaption, mTimestamp, imageLikes;
    TextView num_comments;
    ImageView mBackArrow, mEllipses, mHeartRed, mHeartWhite,commentBubble;
    CircleImageView mProfilePicture;
    UserDetails currentUser;
    private GestureDetector mGestureDetector;
    private GestureDetector mImageDetector;
    private Heart mHeart;
    private FirebaseAuth mAuth;
    private DatabaseReference mRoot;
    private String current_user_id;
    private Boolean mLikedByCurrentUser;
    private StringBuilder mUsers;
    private static final String TAG = "ViewPost";
    private String mLkesString = "";
    private int mNumComments= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        setUpWidgets();
        if (getIntent() != null) {
            photo = (Photo) getIntent().getExtras().getSerializable("photo");
            currentUser = (UserDetails) getIntent().getExtras().getSerializable("user");
            print.l(photo.toString());
        }
        mAuth = FirebaseAuth.getInstance();
        mRoot = FirebaseDatabase.getInstance().getReference();

        if (mAuth.getCurrentUser() != null) {
            current_user_id = mAuth.getCurrentUser().getUid();
        }

        mHeart = new Heart(mHeartWhite, mHeartRed);

        mGestureDetector = new GestureDetector(ViewPost.this, new GestureListener());
        mImageDetector = new GestureDetector(ViewPost.this, new ImageGestureDetector());
        mImage = (SquareImageView) findViewById(R.id.square_image);
        mImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                print.l("Image touched");
                return mImageDetector.onTouchEvent(motionEvent);
            }
        });

        setUpBottomNavBar();
       getLikesString();
        //setTestToggle();

    }


    private void setUpWidgets() {
        mBackArrow = (ImageView) findViewById(R.id.view_post_back_btn);
        mProfilePicture = (CircleImageView) findViewById(R.id.post_circle);
        mUsername = (TextView) findViewById(R.id.post_user_name);
        mEllipses = (ImageView) findViewById(R.id.post_ellipses);
        mHeartWhite = (ImageView) findViewById(R.id.heart);
        mHeartRed = (ImageView) findViewById(R.id.heart_red);
        mCaption = (TextView) findViewById(R.id.caption);
        mTimestamp = (TextView) findViewById(R.id.image_time_posted);
        imageLikes = (TextView) findViewById(R.id.image_likes);
        commentBubble = (ImageView) findViewById(R.id.commentBubble);
        num_comments = (TextView) findViewById(R.id.num_comments);
        commentBubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewPost.this, CommentsActivity.class);
                intent.putExtra("photo",photo);
                startActivity(intent);
            }
        });
        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setWidgets() {
        mTimestamp.setText(DateUtils.getRelativeTimeSpanString(Long.parseLong(photo.getDate_created())));
        imageLikes.setText(mLkesString);


        Picasso.with(ViewPost.this).load(photo.getImage_url()).placeholder(R.drawable.ic_default_avatar)
                .networkPolicy(NetworkPolicy.OFFLINE).fit().centerCrop()
                .into(mImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(ViewPost.this).load(photo.getImage_url()).placeholder(R.drawable.ic_default_avatar)
                                .resize(200, 200).centerCrop().into(mImage);

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
                                .resize(200, 200).centerCrop().into(mProfilePicture);

                    }
                });
        mUsername.setText(currentUser.getUsername());
        if (!mLikedByCurrentUser) {
            mHeartRed.setVisibility(View.GONE);
            mHeartWhite.setVisibility(View.VISIBLE);
            mHeartWhite.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    print.l("        white heart touch detected");
                    return mGestureDetector.onTouchEvent(motionEvent);
                }
            });
        } else {
            mHeartRed.setVisibility(View.VISIBLE);
            mHeartWhite.setVisibility(View.GONE);
            mHeartRed.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    print.l("        red heart touch detected");
                    return mGestureDetector.onTouchEvent(motionEvent);
                }
            });
        }
        mCaption.setText(photo.getCaption());


    }




    private void getLikesString() {
        Query likeQuery = mRoot.child(Consts.PHOTOS_KEY)
                .child(photo.getPhoto_id())
                .child(Consts.LIKES_KEY);
        likeQuery.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUsers = new StringBuilder();
                for (DataSnapshot singleSnap : dataSnapshot.getChildren()) {
                    Query user_id_query = mRoot.child(Consts.USERS_KEY)
                            .orderByChild(Consts.USER_ID)
                            .equalTo(singleSnap.getValue(Like.class).getUser_id());
                    user_id_query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snaps : dataSnapshot.getChildren()) {
                                Log.d(TAG, "onDataChange: found a like " + snaps.getValue(User.class).getUser_id());

                                mUsers.append(snaps.getValue(User.class).getUsername());
                                mUsers.append(",");
                            }
                            print.l(mUsers.toString());
                            String[] split = mUsers.toString().split(",");

                            if (mUsers.toString().contains(currentUser.getUsername() + ",")){
                                mLikedByCurrentUser = true;
                            } else {
                                mLikedByCurrentUser = false;
                            }

                            int length = split.length;

                            if (length == 1) {

                                mLkesString = "Liked by " + split[0];
                            } else if (length == 2) {
                                mLkesString = "Liked by " + split[0] + " and " + split[1];

                            } else if (length == 3) {
                                mLkesString = "Liked by " + split[0] + " , "
                                        + split[1] + " and " + split[2];
                            } else if (length == 4) {
                                mLkesString = "Liked by " + split[0] + " , "
                                        + split[1] + " , " + split[2] + " and " + split[3];
                            } else {
                                mLkesString = "Liked by " + split[0] + " , "
                                        + split[1] + " , " + split[2] + " and " + (split.length - 3)
                                        + " others";
                            }
                            setWidgets();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                if (!dataSnapshot.exists()) {
                    mLikedByCurrentUser = false;
                    mLkesString = "";
                    setWidgets();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }


        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            DatabaseReference likeQuery = mRoot.child(Consts.PHOTOS_KEY)
                    .child(photo.getPhoto_id())
                    .child(Consts.LIKES_KEY);
            likeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
               //     print.l("  number of children in the snapshot : " + dataSnapshot.getChildrenCount());
                    if(!dataSnapshot.exists()){

                        //add a new Like;
                        addANewLike();
                    //    print.l("                       no likes adding a new like");
                    }
                    else{
                    //case 1 : liked by current User
                    for(DataSnapshot single : dataSnapshot.getChildren()){
                        String KEYID = single.getKey();
                       // print.l("current status of liked by vurrent user is " + String.valueOf(mLikedByCurrentUser));

                        if(mLikedByCurrentUser && single.getValue(Like.class).getUser_id()
                                .equals(current_user_id)){
                            mRoot.child(Consts.PHOTOS_KEY)
                                    .child(photo.getPhoto_id())
                                    .child(Consts.LIKES_KEY)
                                    .child(KEYID).removeValue();
                            mRoot.child(Consts.USER_PHOTOS_KEY)
                                    .child(current_user_id)
                                    .child(photo.getPhoto_id())
                                    .child(Consts.LIKES_KEY)
                                    .child(KEYID).removeValue();
                            mHeart.toggleLikes();
                            getLikesString();
                        }
                        else if(!mLikedByCurrentUser){
                            addANewLike();
                            break;
                        }
                    }

                }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            return true;
        }
    }

    private void addANewLike() {
        String ID = mRoot.push().getKey();
        Like like = new Like(current_user_id);
        mRoot.child(Consts.PHOTOS_KEY)
                .child(photo.getPhoto_id())
                .child(Consts.LIKES_KEY)
                .child(ID)
                .setValue(like);
        mRoot.child(Consts.USER_PHOTOS_KEY)
                .child(current_user_id)
                .child(photo.getPhoto_id())
                .child(Consts.LIKES_KEY)
                .child(ID)
                .setValue(like);
      //  mLikedByCurrentUser = true;
        mHeart.toggleLikes();
        getLikesString();
    }

    public class ImageGestureDetector extends GestureDetector.SimpleOnGestureListener {


        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {

            print.l("      double tap recorded");
            mHeart.toggleLikes();

            return true;
        }
    }


    public void setUpBottomNavBar() {

        BottomNavigationViewEx bottomBar = (BottomNavigationViewEx) findViewById(R.id.bottom_nav_bar);
        BottomNavigationHelper.setUpBottomNavToolBar(bottomBar);
        BottomNavigationHelper.enableNavigation(ViewPost.this, this, bottomBar);
        Menu menu = bottomBar.getMenu();
        MenuItem item = menu.getItem(ACTIVITY_NUMBER);
        item.setChecked(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Query query = mRoot.child(Consts.PHOTOS_KEY).child(photo.getPhoto_id())
                .child(Consts.COMMENTS);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                mNumComments = (int) dataSnapshot.getChildrenCount();
                Log.e(TAG, "onDataChange: number of comments" + dataSnapshot.getChildrenCount() );
                if(mNumComments == 0){
                    num_comments.setText("No comments yet");
                }
                else{
                    num_comments.setText("View " + mNumComments  + " comments");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
