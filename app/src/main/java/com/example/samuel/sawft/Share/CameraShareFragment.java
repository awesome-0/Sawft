package com.example.samuel.sawft.Share;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.samuel.sawft.NextActivity;
import com.example.samuel.sawft.Profile.ProfileActivity;
import com.example.samuel.sawft.R;
import com.example.samuel.sawft.Utils.Consts;
import com.example.samuel.sawft.Utils.Permissions;
import com.example.samuel.sawft.Utils.print;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CameraShareFragment extends Fragment {


    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int PHOTO_FRAGMENT = 1;
    private Button camera_btn;
    private StorageReference mStorage;
    private FirebaseAuth mAuth;
    private DatabaseReference mRoot;

    public CameraShareFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera_share, container, false);
        camera_btn = view.findViewById(R.id.open_camera);
        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((ShareActivity)getActivity()).getCurrentToolbarNumber() == PHOTO_FRAGMENT) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                    } else {
                        if (ActivityCompat.checkSelfPermission(getActivity(),Permissions.permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                            Intent shareIntent = new Intent(getActivity(), ShareActivity.class);
                            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            print.t(getActivity(), "Pemissions denied");
                            startActivity(shareIntent);

                        } else {

                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);

                        }

                    }
                }

            }
        });
        mAuth = FirebaseAuth.getInstance();
        mRoot = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();


      return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST_CODE){
            if(data != null) {
                Uri imgUri = data.getData();
                if(getActivity().getIntent().hasExtra("change_photo")){
                    uploadProfilePhoto(imgUri);

                }
                else {
                    Intent shareIntent = new Intent(getActivity(), NextActivity.class);
                    shareIntent.putExtra(getString(R.string.next_uri_image), imgUri.toString());
                    startActivity(shareIntent);
                }
            }
            else{
                print.t(getActivity(),"Some eroor occured");
            }

        }
    }

    private void uploadProfilePhoto(Uri uri) {
        mStorage = FirebaseStorage.getInstance().getReference();
        StorageReference profilePicPath = mStorage.child(Consts.FIREBASE_IMAGE_LOCATION + "/" +
                mAuth.getCurrentUser().getUid() +"/profilePhoto");
        print.t(getActivity(),"Please wait while we upload");
        profilePicPath.putFile(uri)
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
                        }

                    }
                });


    }
}
