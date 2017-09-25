package com.example.samuel.sawft.Utils;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.samuel.sawft.Models.Comment;
import com.example.samuel.sawft.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Samuel on 25/09/2017.
 */

public class CommentsAdapter extends ArrayAdapter<Comment> {
    private int layoutResource;
    private Context ctx;


    public CommentsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull Comment[] objects) {
        super(context, resource, objects);
        layoutResource = resource;
        ctx = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

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
        holder.time_stamp.setText(DateUtils.getRelativeTimeSpanString(Long.parseLong(getItem(position)
        .getTimestamp())));

        return  convertView;
    }

    public static class ViewHolder{
        TextView username,num_likes,time_stamp,reply,comment;
        private CircleImageView profile_image;
        private ImageView like_btn;

    }
}
