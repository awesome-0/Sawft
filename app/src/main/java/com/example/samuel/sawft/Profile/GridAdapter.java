package com.example.samuel.sawft.Profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.samuel.sawft.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Samuel on 17/09/2017.
 */

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolder>{
    private ArrayList<String>imgUrls = new ArrayList<>();
    private Context ctx;

    public GridAdapter(Context ctx) {
        this.ctx = ctx;
        setImages();
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_image_row,parent,false);

        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GridViewHolder holder, final int position) {
        Picasso.with(ctx).load(imgUrls.get(position)).placeholder(R.drawable.ic_default_avatar).networkPolicy(NetworkPolicy.OFFLINE)
                .fit().centerCrop().into(holder.images, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(ctx).load(imgUrls.get(position)).placeholder(R.drawable.ic_default_avatar)
                        .fit().centerCrop().into(holder.images);
            }
        });

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

    @Override
    public int getItemCount() {
        return imgUrls.size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder{
        ImageView images;


        public GridViewHolder(View itemView) {
            super(itemView);
            images = itemView.findViewById(R.id.Rimage);
        }
    }
}
