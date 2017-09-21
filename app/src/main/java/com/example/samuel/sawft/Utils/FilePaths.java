package com.example.samuel.sawft.Utils;

import android.os.Environment;

/**
 * Created by Samuel on 21/09/2017.
 */

public class FilePaths {

    public String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();
    public  String camera_path = ROOT_DIR + "/DCIM/Camera";
    public  String picture_path = ROOT_DIR + "/Pictures";

}
