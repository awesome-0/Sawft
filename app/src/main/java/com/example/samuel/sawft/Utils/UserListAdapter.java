package com.example.samuel.sawft.Utils;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.samuel.sawft.Models.User;
import com.example.samuel.sawft.Models.UserDetails;
import com.example.samuel.sawft.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Samuel on 27/09/2017.
 */

public class UserListAdapter extends ArrayAdapter<User> {
    private List<User> mUsers;
    public UserListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<User> objects) {
        super(context, resource, objects);
        mUsers = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        final viewHolder holder;
       if(convertView == null){
           convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_row,parent,false);
           holder = new viewHolder();
           holder.username = convertView.findViewById(R.id.search_username);
           holder.displayname = convertView.findViewById(R.id.search_displayname);
           holder.picture = convertView.findViewById(R.id.search_image);

           convertView.setTag(holder);

       }else{
           holder = (viewHolder)convertView.getTag();
       }
       holder.username.setText(getItem(position).getUsername());
        Query query = FirebaseDatabase.getInstance().getReference().child(Consts.USER_STATUS_KEY)
                .child(getItem(position).getUser_id());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                holder.displayname.setText(dataSnapshot.getValue(UserDetails.class).getDisplay_name());
                loadImage(parent.getContext(),holder.picture,dataSnapshot.getValue(UserDetails.class).getProfile_photo());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       return convertView;
    }

    public class viewHolder{

        TextView username,displayname;
        CircleImageView picture;
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
