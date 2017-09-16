package com.example.samuel.sawft.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.example.samuel.sawft.Home.HomeActivity;
import com.example.samuel.sawft.Likes.LikesActivity;
import com.example.samuel.sawft.Profile.ProfileActivity;
import com.example.samuel.sawft.R;
import com.example.samuel.sawft.Search.SearchActivity;
import com.example.samuel.sawft.Share.ShareActivity;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by Samuel on 15/09/2017.
 */

public class BottomNavigationHelper {

    public static void setUpBottomNavToolBar(BottomNavigationViewEx ex){
        ex.enableAnimation(false);
        ex.enableItemShiftingMode(false);
        ex.enableShiftingMode(false);
        ex.setTextVisibility(false);

    }
    public static void enableNavigation (final Context ctx, BottomNavigationViewEx view){

        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_house:
                        Intent intent1 = new Intent(ctx, HomeActivity.class);
                        ctx.startActivity(intent1);
                        break;
                    case R.id.ic_search:
                        Intent intent2 = new Intent(ctx, SearchActivity.class);
                        ctx.startActivity(intent2);
                        break;
                    case R.id.ic_circle:
                        Intent intent3 = new Intent(ctx, ShareActivity.class);
                        ctx.startActivity(intent3);
                        break;
                    case R.id.ic_alert:
                        Intent intent4 = new Intent(ctx, LikesActivity.class);
                        ctx.startActivity(intent4);
                        break;
                    case R.id.ic_android:
                        Intent intent5 = new Intent(ctx, ProfileActivity.class);
                        ctx.startActivity(intent5);
                        break;


                }
                return false;
            }
        });


    }
}
