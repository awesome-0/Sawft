package com.example.samuel.sawft.Utils;

import com.example.samuel.sawft.print;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Samuel on 18/09/2017.
 */

public class StringMan {
    private FirebaseAuth mAuth;

    static String append = "";
    public StringMan() {
        mAuth = FirebaseAuth.getInstance();
        append = mAuth.getCurrentUser().getUid().substring(0,6);
        print.l(append);
    }

    public static String getUserName(String name){
        return name.replace(" ",".");
    }
    public static String getnewUsername(String name){
        return  name.replace(" ",".") + append;

    }
}
