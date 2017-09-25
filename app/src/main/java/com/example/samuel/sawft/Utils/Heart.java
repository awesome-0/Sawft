package com.example.samuel.sawft.Utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

/**
 * Created by Samuel on 24/09/2017.
 */

public class Heart {

    private ImageView heartRed,heartWhite;
    private static final String TAG = "Heart";
    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();


    public Heart( ImageView heartWhite,ImageView heartRed) {
        this.heartRed = heartRed;
        this.heartWhite = heartWhite;
    }

    public void toggleLikes(){

        AnimatorSet animationSet = new AnimatorSet();
        if(heartRed.getVisibility() == View.VISIBLE){
            Log.d(TAG, "toggleLikes: toggling red heart off");

            heartRed.setScaleX(0.1f);
            heartRed.setScaleY(0.1f);

            ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(heartRed,"scaleY",1f,0f);
            scaleDownY.setDuration(300);
            scaleDownY.setInterpolator(ACCELERATE_INTERPOLATOR);


            ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(heartRed,"scaleX",1f,0f);
            scaleDownX.setDuration(300);
            scaleDownX.setInterpolator(ACCELERATE_INTERPOLATOR);

            heartRed.setVisibility(View.GONE);
            heartWhite.setVisibility(View.VISIBLE);

            animationSet.playTogether(scaleDownY,scaleDownX);

        }
        else if(heartRed.getVisibility() == View.GONE){
            Log.d(TAG, "toggleLikes: toggling red heart on");

            heartRed.setScaleX(0.1f);
            heartRed.setScaleY(0.1f);

            ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(heartRed,"scaleY",0.1f,1f);
            scaleDownY.setDuration(300);
            scaleDownY.setInterpolator(DECCELERATE_INTERPOLATOR);


            ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(heartRed,"scaleX",0.1f,1f);
            scaleDownX.setDuration(300);
            scaleDownX.setInterpolator(DECCELERATE_INTERPOLATOR);

            heartRed.setVisibility(View.VISIBLE);
            heartWhite.setVisibility(View.GONE);

            animationSet.playTogether(scaleDownY,scaleDownX);

        }


        animationSet.start();

    }
}
