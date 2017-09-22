package com.example.samuel.sawft.Utils;

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
    public static String getTags(String caption){
        StringBuilder builder = new StringBuilder();
        char[] array = caption.toCharArray();
        boolean foundword = false;
        if(caption.indexOf('#') >0){
        for(char c: array){
            if(c == '#'){
                foundword = true;
                builder.append(c);
            }
            else{
                if(foundword){
                    builder.append(c);
                }
                if(c==' '){
                    foundword = false;
                }
            }
        }

            String tag = builder.toString().replace(" ","").replace("#",",#");
            return tag.substring(1,tag.length());


        }
        else {
            return "";
        }

    }
}
