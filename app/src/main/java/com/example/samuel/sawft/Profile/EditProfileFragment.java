package com.example.samuel.sawft.Profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.samuel.sawft.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment {
    private CircleImageView profilePhoto;
    private ImageView back_btn;
    private String dummy = "https://thumbs.dreamstime.com/z/profile-icon-male-avatar-man-hipster-style-fashion-cartoon-guy-beard-glasses-portrait-casual-person-silhouette-face-flat-design-62449823.jpg";



    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_edit_profile, container, false);
        setUpWidgets(view);
        Picasso.with(container.getContext()).load(dummy).placeholder(R.drawable.ic_default_avatar)
                .fit().centerCrop().into(profilePhoto, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

                Picasso.with(container.getContext()).load(dummy).placeholder(R.drawable.ic_default_avatar).fit().centerCrop()
                        .into(profilePhoto);
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        return view ;
    }

    private void setUpWidgets(View v){

       profilePhoto =  v.findViewById(R.id.profile_photo);
        back_btn = v.findViewById(R.id.edit_back_btn);

    }

}
