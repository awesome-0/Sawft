package com.example.samuel.sawft.Utils;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.samuel.sawft.Models.Comment;
import com.example.samuel.sawft.Models.UserDetails;
import com.example.samuel.sawft.Profile.ViewPost;
import com.example.samuel.sawft.R;
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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Samuel on 25/09/2017.
 */

public class CommentsAdapter extends ArrayAdapter<Comment> {
    private int layoutResource;
    private Context ctx;
    private DatabaseReference mRoot;
    private static final String TAG = "CommentsAdapter";


    public CommentsAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<Comment> comments) {
        super(context, resource, comments);
        layoutResource = resource;
        ctx = context;
        mRoot =  FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        final ViewHolder holder;

        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(layoutResource,parent,false);
            holder = new ViewHolder();

            holder.username = convertView.findViewById(R.id.commenter_username);
            holder.comment = convertView.findViewById(R.id.comment);
            holder.num_likes = convertView.findViewById(R.id.comment_likes);
            holder.reply = convertView.findViewById(R.id.comment_reply);
            holder.time_stamp = convertView.findViewById(R.id.comment_time_stamp);
            holder.profile_image = convertView.findViewById(R.id.commenter_image);
            holder.like_btn = convertView.findViewById(R.id.comments_like_btn);
            convertView.setTag(holder);
        }
        else {
           holder = (ViewHolder) convertView.getTag();

        }
        holder.comment.setText(getItem(position).getComment());
        String Ts = (String) DateUtils.getRelativeTimeSpanString(Long.parseLong(getItem(position)
                .getTimestamp()));
        if(Ts.contains("min")){
            int mId = Ts.indexOf("m");
            holder.time_stamp.setText(Ts.substring(0,mId+1));

        }
        else if(Ts.contains("hou")){
            int mId = Ts.indexOf("h");
            holder.time_stamp.setText(Ts.substring(0,mId+1));

        }
        else if(Ts.contains("day")){
            int mId = Ts.indexOf("d");
            holder.time_stamp.setText(Ts.substring(0,mId+1));

        }




        Query userquery = mRoot.child(Consts.USER_STATUS_KEY)
                .child(getItem(position).getUser_id());
        userquery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    Log.e(TAG, "onDataChange: single snap shot" + dataSnapshot.toString() );
                    holder.username.setText(
                            dataSnapshot.getValue(UserDetails.class).getUsername()
                    );
                    loadImage(parent.getContext(),holder.profile_image,dataSnapshot.getValue(UserDetails.class).getProfile_photo());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

            if(position == 0){
                holder.num_likes.setVisibility(View.GONE);
                holder.reply.setVisibility(View.GONE);
                holder.like_btn.setVisibility(View.GONE);

            }
            else{
                holder.num_likes.setVisibility(View.VISIBLE);
                holder.reply.setVisibility(View.VISIBLE);
                holder.like_btn.setVisibility(View.VISIBLE);
            }






        return  convertView;
    }

    public static class ViewHolder{
        TextView username,num_likes,time_stamp,reply,comment;
        private CircleImageView profile_image;
        private ImageView like_btn;

    }
    public void loadImage(final Context ctx, final CircleImageView img,final String url){

        Picasso.with(ctx).load(url).placeholder(R.drawable.ic_default_avatar)
                .networkPolicy(NetworkPolicy.OFFLINE).fit().centerCrop()
                .into(img, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(ctx).load(url).placeholder(R.drawable.ic_default_avatar)
                                .resize(200, 200).centerCrop().into(img);

                    }
                });


    }
}
