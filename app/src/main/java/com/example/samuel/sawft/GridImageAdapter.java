package com.example.samuel.sawft;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Samuel on 17/09/2017.
 */

public class GridImageAdapter extends ArrayAdapter<String> {
    private int layout;
    private Context ctx;
    private ArrayList<String> imgUrls = new ArrayList<>();
    public GridImageAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        layout = resource;
        ctx = context;
        setImages();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Viewholder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(ctx).inflate(layout,parent,false);
            holder = new Viewholder();
            holder.imgs = convertView.findViewById(R.id.Rimage);
            convertView.setTag(holder);

        }
        else{
            holder = (Viewholder) convertView.getTag();
        }

        final Viewholder finalHolder = holder;
        Picasso.with(ctx).load(imgUrls.get(position)).placeholder(R.drawable.ic_default_avatar).networkPolicy(NetworkPolicy.OFFLINE)
                .fit().centerCrop().into(holder.imgs, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(ctx).load(imgUrls.get(position)).placeholder(R.drawable.ic_default_avatar)
                        .fit().centerCrop().into(finalHolder.imgs);
            }
        });

        return convertView;
    }

    private void setImages(){
        imgUrls.add("https://thumbs.dreamstime.com/z/profile-icon-male-avatar-man-hipster-style-fashion-cartoon-guy-beard-glasses-portrait-casual-person-silhouette-face-flat-design-62449823.jpg");
        imgUrls.add("https://thumbs.dreamstime.com/m/profile-icon-male-hispanic-avatar-portrait-casual-person-silhouette-face-flat-design-vector-52547844.jpg");
        imgUrls.add("https://thumbs.dreamstime.com/m/profile-icon-female-avatar-woman-portrait-casual-person-silhouette-face-flat-design-vector-illustration-58249445.jpg");
        imgUrls.add("https://thumbs.dreamstime.com/m/profile-icon-male-avatar-portrait-casual-person-silhouette-face-flat-design-vector-illustration-58249394.jpg");
        imgUrls.add("https://thumbs.dreamstime.com/m/businesswoman-profile-icon-avatar-fashion-style-female-portrait-business-woman-flat-vector-illustration-69046084.jpg");
        imgUrls.add("https://thumbs.dreamstime.com/m/profile-icon-female-avatar-woman-portrait-casual-person-silhouette-face-flat-design-vector-illustration-58249495.jpg");
        imgUrls.add("https://thumbs.dreamstime.com/m/profile-icon-female-avatar-woman-portrait-casual-person-silhouette-face-flat-design-vector-illustration-58249495.jpg");
        imgUrls.add("https://thumbs.dreamstime.com/m/profile-icon-male-hispanic-avatar-portrait-casual-person-silhouette-face-flat-design-vector-52547844.jpg");
        imgUrls.add("https://thumbs.dreamstime.com/m/profile-icon-male-avatar-portrait-casual-person-silhouette-face-flat-design-vector-illustration-58249394.jpg");
    }

    public class Viewholder{
        ImageView imgs;


    }

    @Override
    public int getCount() {
        return imgUrls.size();
    }
}
