package com.example.samuel.sawft.Share;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.samuel.sawft.Profile.ProfileActivity;
import com.example.samuel.sawft.Utils.Consts;
import com.example.samuel.sawft.Utils.GridImageAdapter;
import com.example.samuel.sawft.R;
import com.example.samuel.sawft.Utils.FilePaths;
import com.example.samuel.sawft.Utils.FileSearch;
import com.example.samuel.sawft.Utils.print;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment {
    ImageView close,share_image;
    GridView share_grid;
    TextView share_next;
    Spinner share_spinner;
    ProgressBar bar;
    String mSelectedImage;
    ArrayList<String> directories;
    private StorageReference mStorage;
    private FirebaseAuth mAuth;
    private DatabaseReference mRoot;
    boolean uploaded;
    String imgUrl;
    private static final String TAG = "GalleryFragment";


    public GalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        close = view.findViewById(R.id.close_share);
        directories = new ArrayList<>();
        share_image = view.findViewById(R.id.share_image);
        share_spinner = view.findViewById(R.id.share_spinner);
        share_next = view.findViewById(R.id.share_next);
        share_grid = view.findViewById(R.id.share_grid);
        bar = view.findViewById(R.id.share_progressBar);
        bar.setVisibility(View.VISIBLE);
        mAuth = FirebaseAuth.getInstance();
        mRoot = FirebaseDatabase.getInstance().getReference();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        share_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getActivity().getIntent().hasExtra("change_photo")){
                    uploadProfilePhoto();


                }
                else {
                    Intent shareIntent = new Intent(getActivity(), NextActivity.class);
                    shareIntent.putExtra(getString(R.string.next_image), mSelectedImage);
                    startActivity(shareIntent);
                }

            }
        });
        init();

        return view;
    }

    private void uploadProfilePhoto() {
        File profilePic = new File(mSelectedImage);
        mStorage = FirebaseStorage.getInstance().getReference();
       StorageReference profilePicPath = mStorage.child(Consts.FIREBASE_IMAGE_LOCATION + "/" +
               mAuth.getCurrentUser().getUid() +"/profilePhoto");
        print.t(getActivity(),"Please wait while we upload");
        profilePicPath.putFile(Uri.fromFile(profilePic))
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            print.l("                                            done uploading");

                            Intent editProfile = new Intent(getActivity(), ProfileActivity.class);
                            startActivity(editProfile);
                            getActivity().finish();
                            Map addPhoto = new HashMap<>();
                            addPhoto.put(Consts.USER_STATUS_KEY + "/" + mAuth.getCurrentUser().getUid() + "/" + "profile_photo",task.getResult().getDownloadUrl().toString());
                            mRoot.updateChildren(addPhoto);
                            uploaded = true;
                        }

                    }
                });

    }

    private void init(){

        FilePaths filePaths = new FilePaths();
        if(FileSearch.getDirectoryPath(filePaths.picture_path)!= null){
            directories = FileSearch.getDirectoryPath(filePaths.picture_path);

        }
        ArrayList<String> directoryNames = new ArrayList<>();
        for(int i= 0;i<directories.size();i++){
            int j = directories.get(i).lastIndexOf("/");
            String string = directories.get(i).substring(j+1);
            directoryNames.add(string);
        }
        directories.add(filePaths.camera_path);
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,directoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        share_spinner.setAdapter(adapter);

        share_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemSelected: we selected " + directories.get(i));
                setUpGridView(directories.get(i));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void setUpGridView(String s) {
        final ArrayList<String> imgUrls = FileSearch.getFilePaths(s);
        GridImageAdapter adapter = new GridImageAdapter(getActivity(),R.layout.single_image_row,imgUrls,"file://");
        int col_width = getResources().getDisplayMetrics().widthPixels;
        share_grid.setColumnWidth((col_width/3));
        share_grid.setAdapter(adapter);
        if(imgUrls.size()>0) {
            setImage(imgUrls.get(0));
            mSelectedImage = imgUrls.get(0);
            share_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    setImage(imgUrls.get(i));
                    mSelectedImage = imgUrls.get(i);
                }
            });
        }
    }

    private void setImage(String i) {
        Picasso.with(getActivity()).load(new File(i)).fit().centerCrop().placeholder(R.drawable.ic_default_avatar).into(share_image, new Callback() {
            @Override
            public void onSuccess() {
                bar.setVisibility(View.GONE);

            }

            @Override
            public void onError() {
                bar.setVisibility(View.GONE);
            }
        });
    }

}
