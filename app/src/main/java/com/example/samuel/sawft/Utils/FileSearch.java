package com.example.samuel.sawft.Utils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Samuel on 21/09/2017.
 */

public class FileSearch {

    public static ArrayList<String> getDirectoryPath(String directory){
        ArrayList<String> fileDirsPath = new ArrayList<>();
        File file = new File(directory);

        File[] dirs = file.listFiles();
        for(int i = 0;i < dirs.length; i++){
            if(dirs[i].isDirectory()){
                fileDirsPath.add(dirs[i].getAbsolutePath());
            }
        }
        return fileDirsPath;
    }

    public static ArrayList<String> getFilePaths(String directory){
        ArrayList<String> filePath = new ArrayList<>();
        File file = new File(directory);

        File[] dirs = file.listFiles();
        for(int i = 0;i < dirs.length; i++){
            if(dirs[i].isFile()){
                filePath.add(dirs[i].getAbsolutePath());
            }
        }
        return filePath;

    }
}
