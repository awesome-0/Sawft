package com.example.samuel.sawft.Share;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.samuel.sawft.R;
import com.example.samuel.sawft.Utils.Permissions;
import com.example.samuel.sawft.Utils.print;

/**
 * A simple {@link Fragment} subclass.
 */
public class CameraShareFragment extends Fragment {


    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int PHOTO_FRAGMENT = 1;
    private Button camera_btn;

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


      return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST_CODE){


        }
    }
}
