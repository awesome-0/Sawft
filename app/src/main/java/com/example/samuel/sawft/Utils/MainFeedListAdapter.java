package com.example.samuel.sawft.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.samuel.sawft.Models.Comment;
import com.example.samuel.sawft.Models.Like;
import com.example.samuel.sawft.Models.Photo;
import com.example.samuel.sawft.Models.User;
import com.example.samuel.sawft.Models.UserDetails;
import com.example.samuel.sawft.Profile.CommentsActivity;
import com.example.samuel.sawft.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Samuel on 04/10/2017.
 */

public class MainFeedListAdapter extends ArrayAdapter<Photo> {
    private Context ctx;
    private DatabaseReference mRoot;
    private FirebaseAuth mAuth;
    String current_user_id;
    private static final String TAG = "MainFeedListAdapter";


    public MainFeedListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Photo> objects) {
        super(context, resource, objects);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser().getUid() != null) {
            current_user_id = mAuth.getCurrentUser().getUid();
        }
        ctx = context;
        mRoot = FirebaseDatabase.getInstance().getReference();

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mainfeed, parent, false);
            holder.mBackArrow = convertView.findViewById(R.id.view_post_back_btn);
            holder.mProfilePicture = convertView.findViewById(R.id.post_circle);
            holder.mUsername = convertView.findViewById(R.id.post_user_name);
            holder.mEllipses = convertView.findViewById(R.id.post_ellipses);
            holder.mHeartWhite = convertView.findViewById(R.id.heart);
            holder.mHeartRed = convertView.findViewById(R.id.heart_red);
            holder.mCaption = convertView.findViewById(R.id.caption);
            holder.mTimestamp = convertView.findViewById(R.id.image_time_posted);
            holder.imageLikes = convertView.findViewById(R.id.image_likes);
            holder.commentBubble = convertView.findViewById(R.id.commentBubble);
            holder.num_comments = convertView.findViewById(R.id.num_comments);
            holder.mImage = convertView.findViewById(R.id.square_image);
            holder.photo = getItem(position);
            holder.mHeart = new Heart(holder.mHeartWhite, holder.mHeartRed);
            holder.mGestureDetector = new GestureDetector(parent.getContext(), new GestureListener(holder));
            holder.mUsers = new StringBuilder();
            holder.mPhotoUser = new UserDetails();
            holder.mCurrentUser = new UserDetails();
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        getPhotoUser(holder,getItem(position));
        getCurrentUser(holder);
        getNumComments(getItem(position),holder);


        loadImage(getItem(position).getImage_url(),holder,ctx);
        loadPhotoDetails(holder,getItem(position));
        getLikesString(holder);
        //setLikesString(holder,holder.mLkesString);

//        List<Comment>comments = holder.photo.getComments();
//        try {
//            holder.num_comments.setText("View all " + comments.size() + " Comments");
//        }catch (NullPointerException e){
//            Log.e(TAG, "getView: NullPointer exception " + e.getMessage() );
//        }




        return convertView;
    }

    private void loadPhotoDetails(ViewHolder holder, Photo item) {
        holder.mCaption.setText(item.getCaption());
        holder.mTimestamp.setText(String.valueOf(DateUtils.getRelativeTimeSpanString(Long.parseLong(item.getDate_created()))));

    }


    private void getNumComments(final Photo photo, final ViewHolder holder) {
        final List<Comment>comments = new ArrayList<>();

        Query query = mRoot.child(Consts.PHOTOS_KEY)
                .child(photo.getPhoto_id()).child(Consts.COMMENTS);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                   Comment comment = singleSnapshot.getValue(Comment.class);
                   comments.add(comment);
               }
                try {
            holder.num_comments.setText("View all " + comments.size() + " Comments");
        }catch (NullPointerException e){
            Log.e(TAG, "getView: NullPointer exception " + e.getMessage() );
        }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        holder.num_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent commentsIntent = new Intent(ctx, CommentsActivity.class);
                commentsIntent.putExtra("photo",photo);
                ctx.startActivity(commentsIntent);
            }
        });
        holder.commentBubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent commentsIntent = new Intent(ctx, CommentsActivity.class);
                commentsIntent.putExtra("photo",photo);
                ctx.startActivity(commentsIntent);
            }
        });
    }

    private void getPhotoUser(final ViewHolder holder, final Photo photo) {
        Query query = mRoot.child(Consts.USER_STATUS_KEY)
                .child(photo.getUser_id());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                holder.mPhotoUser = dataSnapshot.getValue(UserDetails.class);
               // Log.e(TAG, "onDataChange: current user is " + holder.mPhotoUser.toString() );
                loadUserDetails(holder.mPhotoUser,holder);
                getLikesString(holder);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void loadUserDetails(final UserDetails mCurrentUser, final ViewHolder holder) {
        Picasso.with(ctx).load(mCurrentUser.getProfile_photo()).placeholder(R.drawable.ic_default_avatar)
                .networkPolicy(NetworkPolicy.OFFLINE).resize(200, 200).centerCrop()
                .into( holder.mProfilePicture, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(ctx).load(mCurrentUser.getProfile_photo()).placeholder(R.drawable.ic_default_avatar)
                                .resize(200, 200).centerCrop().into(holder.mProfilePicture);

                    }
                });
        holder.mUsername.setText(mCurrentUser.getUsername());

    }

    private void loadImage(final String image_url, final ViewHolder holder, final Context ctx) {
        Picasso.with(ctx).load(image_url).networkPolicy(NetworkPolicy.OFFLINE)
                .fit().centerCrop().into(holder.mImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(ctx).load(image_url)
                        .fit().centerCrop().into(holder.mImage);
            }
        });

    }
    private void getCurrentUser(final ViewHolder holder){
        Query query = mRoot.child(Consts.USER_STATUS_KEY)
                .child(current_user_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                holder.mCurrentUser = dataSnapshot.getValue(UserDetails.class);
                // Log.e(TAG, "onDataChange: current user is " + holder.mPhotoUser.toString() );

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void addANewLike(ViewHolder holder) {
        String ID = mRoot.push().getKey();
        Like like = new Like(current_user_id);
        mRoot.child(Consts.PHOTOS_KEY)
                .child(holder.photo.getPhoto_id())
                .child(Consts.LIKES_KEY)
                .child(ID)
                .setValue(like);
//        if(holder.photo.getUser_id().equals(current_user_id)){
//            mRoot.child(Consts.USER_PHOTOS_KEY)
//                    .child(current_user_id)
//                    .child(holder.photo.getPhoto_id())
//                    .child(Consts.LIKES_KEY)
//                    .child(ID)
//                    .setValue(like);
//        }


        holder.mHeart.toggleLikes();
        getLikesString(holder);
    }

    private void getLikesString(final ViewHolder holder) {
        try {
            Query likeQuery = mRoot.child(Consts.PHOTOS_KEY)
                    .child(holder.photo.getPhoto_id())
                    .child(Consts.LIKES_KEY);
            likeQuery.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    holder.mUsers = new StringBuilder();
                    for (DataSnapshot singleSnap : dataSnapshot.getChildren()) {
                        Query user_id_query = mRoot.child(Consts.USERS_KEY)
                                .orderByChild(Consts.USER_ID)
                                .equalTo(singleSnap.getValue(Like.class).getUser_id());
                        user_id_query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snaps : dataSnapshot.getChildren()) {
                                    Log.d(TAG, "onDataChange: found a like " + snaps.getValue(User.class).getUser_id());

                                    holder.mUsers.append(snaps.getValue(User.class).getUsername());
                                    holder.mUsers.append(",");
                                }

                                String[] split = holder.mUsers.toString().split(",");

                                if (holder.mUsers.toString().contains(holder.mCurrentUser.getUsername() + ",")) {
                                    holder.mLikedByCurrentUser = true;
                                } else {
                                    holder.mLikedByCurrentUser = false;
                                }

                                int length = split.length;

                                if (length == 1) {

                                    holder.mLkesString = "Liked by " + split[0];
                                } else if (length == 2) {
                                    holder.mLkesString = "Liked by " + split[0] + " and " + split[1];

                                } else if (length == 3) {
                                    holder.mLkesString = "Liked by " + split[0] + " , "
                                            + split[1] + " and " + split[2];
                                } else if (length == 4) {
                                    holder.mLkesString = "Liked by " + split[0] + " , "
                                            + split[1] + " , " + split[2] + " and " + split[3];
                                } else {
                                    holder.mLkesString = "Liked by " + split[0] + " , "
                                            + split[1] + " , " + split[2] + " and " + (split.length - 3)
                                            + " others";
                                }
                                //setWidgets();
                                setLikesString(holder,holder.mLkesString);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    if (!dataSnapshot.exists()) {
                        holder.mLikedByCurrentUser = false;
                        holder.mLkesString = "";
                        //  setWidgets();
                        setLikesString(holder,holder.mLkesString);
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        } catch (NullPointerException e) {
            Log.e(TAG, "getLikesString: NullPointerException " + e.getMessage());
            holder.mLikedByCurrentUser = false;
            holder.mLkesString = "";
            //  setWidgets();
            setLikesString(holder,holder.mLkesString);
        }

    }

    private void setLikesString(final ViewHolder holder,String likesString) {
        if (!holder.mLikedByCurrentUser) {
           holder. mHeartRed.setVisibility(View.GONE);
            holder.mHeartWhite.setVisibility(View.VISIBLE);
            holder.mHeartWhite.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    print.l("        white heart touch detected");
                    return holder.mGestureDetector.onTouchEvent(motionEvent);
                }
            });
        } else {
            holder.mHeartRed.setVisibility(View.VISIBLE);
            holder.mHeartWhite.setVisibility(View.GONE);
            holder.mHeartRed.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    print.l("        red heart touch detected");
                    return holder.mGestureDetector.onTouchEvent(motionEvent);
                }
            });
        }
        holder.imageLikes.setText(likesString);
    }

    static class ViewHolder {
        Photo photo;
        SquareImageView mImage;
        TextView mBAckLabel, mUsername, mCaption, mTimestamp, imageLikes;
        TextView num_comments;
        ImageView mBackArrow, mEllipses, mHeartRed, mHeartWhite, commentBubble;
        private GestureDetector mGestureDetector;
        private GestureDetector mImageDetector;
        private Heart mHeart;
        private FirebaseAuth mAuth;
        private DatabaseReference mRoot;
        private String current_user_id;
        private Boolean mLikedByCurrentUser;
        private StringBuilder mUsers;
        private String mLkesString = "";
        private int mNumComments = 0;
        CircleImageView mProfilePicture;
        UserDetails mPhotoUser;
        UserDetails mCurrentUser;

    }

    public class GestureListener extends GestureDetector.SimpleOnGestureListener {
        ViewHolder mHolder;

        public GestureListener(ViewHolder holder) {
            mHolder = holder;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }


        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            DatabaseReference likeQuery = mRoot.child(Consts.PHOTOS_KEY)
                    .child(mHolder.photo.getPhoto_id())
                    .child(Consts.LIKES_KEY);
            likeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //     print.l("  number of children in the snapshot : " + dataSnapshot.getChildrenCount());
                    if (!dataSnapshot.exists()) {

                        //add a new Like;
                        addANewLike(mHolder);
                        //    print.l("                       no likes adding a new like");
                    } else {
                        //case 1 : liked by current User
                        for (DataSnapshot single : dataSnapshot.getChildren()) {
                            String KEYID = single.getKey();
                            // print.l("current status of liked by vurrent user is " + String.valueOf(mLikedByCurrentUser));

                            if (mHolder.mLikedByCurrentUser && single.getValue(Like.class).getUser_id()
                                    .equals(current_user_id)) {
                                mRoot.child(Consts.PHOTOS_KEY)
                                        .child(mHolder.photo.getPhoto_id())
                                        .child(Consts.LIKES_KEY)
                                        .child(KEYID).removeValue();
                                mRoot.child(Consts.USER_PHOTOS_KEY)
                                        .child(current_user_id)
                                        .child(mHolder.photo.getPhoto_id())
                                        .child(Consts.LIKES_KEY)
                                        .child(KEYID).removeValue();
                                mHolder.mHeart.toggleLikes();
                                getLikesString(mHolder);
                            } else if (!mHolder.mLikedByCurrentUser) {
                                addANewLike(mHolder);
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

}
