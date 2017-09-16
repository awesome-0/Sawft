package com.example.samuel.sawft;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Samuel on 15/09/2017.
 */

public class print {

    public static void l (String message){
        Log.d("AWESOME::::",message);
    }
    public static void t (Context ctx,String message){
        Toast.makeText(ctx,message,Toast.LENGTH_LONG).show();
    }
}
