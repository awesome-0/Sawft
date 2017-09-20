package com.example.samuel.sawft.Profile;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.samuel.sawft.Models.User;
import com.example.samuel.sawft.Models.UserDetails;
import com.example.samuel.sawft.R;
import com.example.samuel.sawft.Utils.Consts;
import com.example.samuel.sawft.Utils.print;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment {
    private CircleImageView profilePhoto;
    private ImageView back_btn;
    private ImageView saveBtn;
    private String dummy = "https://thumbs.dreamstime.com/z/profile-icon-male-avatar-man-hipster-style-fashion-cartoon-guy-beard-glasses-portrait-casual-person-silhouette-face-flat-design-62449823.jpg";
    private EditText username, desc, website, display_name, email, phoneNo;
    private DatabaseReference mRoot;
    private String current_user_id;
    public static final String USERNAME = "username";
    String saved_user_name, saved_phoneNo, saved_website, saved_desc, saved_displayname;
    String saved_email;
    UserDetails updatedDetails ;
    Map update ;
    User updateUserDetails;

    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        setUpWidgets(view);
        mRoot = FirebaseDatabase.getInstance().getReference();
        Intent intent = getActivity().getIntent();
        updatedDetails = new UserDetails();
        update = new HashMap();
        updateUserDetails =new User();
        if (intent.hasExtra(getString(R.string.calling_activity))) {

            Bundle bundle = intent.getBundleExtra(getString(R.string.calling_activity));
            saved_user_name = bundle.getString(getString(R.string.username));
            saved_displayname = bundle.getString(getString(R.string.display_name));
            saved_website = bundle.getString(getString(R.string.website));
            saved_desc = bundle.getString(getString(R.string.description));
            Bundle userBundle = intent.getBundleExtra(getString(R.string.userBundle));
            saved_email = userBundle.getString(getString(R.string.email));
            current_user_id = bundle.getString(getString(R.string.userId));
            saved_phoneNo = userBundle.getString(getString(R.string.phone_no));
            bindPersonalnfo(saved_email, saved_phoneNo);
            bindBundle(saved_user_name, saved_displayname, saved_website, saved_desc);
        }
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
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getDetails();

            }
        });


        return view;
    }

    private void bindPersonalnfo(String e_mail, String phone_no) {
        email.setText(e_mail);
        phoneNo.setText(phone_no);

    }

    private void bindBundle(String name, String displayname, String web, String descr) {
        display_name.setText(displayname);
        username.setText(name);
        website.setText(web);
        desc.setText(descr);
    }

    private void setUpWidgets(View v) {

        profilePhoto = v.findViewById(R.id.profile_photo);
        back_btn = v.findViewById(R.id.edit_back_btn);
        username = v.findViewById(R.id.username);
        desc = v.findViewById(R.id.description);
        display_name = v.findViewById(R.id.displayname);
        email = v.findViewById(R.id.email);
        phoneNo = v.findViewById(R.id.phoneNumber);
        website = v.findViewById(R.id.website);
        saveBtn = v.findViewById(R.id.settings_save_changes);
    }

    private void getDetails() {
        String gname = username.getText().toString().trim();
        String gdisplayname = display_name.getText().toString().trim();
        String gdesc = desc.getText().toString().trim();
        String gemail = email.getText().toString().trim();
        String gwebsite = website.getText().toString().trim();
        String gphoneNo = phoneNo.getText().toString().trim();
        saveChanges(gname, gdisplayname, gdesc, gwebsite, gemail, gphoneNo);

    }

    private void saveChanges(final String gname, final String gdisplayname, final String gdesc, final String gwebsite
            , final String gemail, final String gphoneNo) {



        if (gname.equals(saved_user_name) && gdisplayname.equals(saved_displayname) && gdesc.equals(saved_desc)
                && gwebsite.equals(saved_website) && gemail.equals(saved_email) && gphoneNo.equals(saved_phoneNo)) {

            //no change made
            getActivity().finish();
        } else {

//            if(!gname.equals(saved_user_name) ||!gemail.equals(saved_email) ||
//                    !gdisplayname.equals(saved_displayname) || !gdesc.equals(saved_desc)
//                            || !gwebsite.equals(saved_website)) {
                if (gname.equals(saved_user_name)) {
                    mRoot.child(Consts.USER_STATUS_KEY).child(current_user_id).child("description").setValue(gdesc);
                    mRoot.child(Consts.USER_STATUS_KEY).child(current_user_id).child("website").setValue(gwebsite);
                    mRoot.child(Consts.USER_STATUS_KEY).child(current_user_id).child("display_name").setValue(gdisplayname);
                    getActivity().finish();


                } else {

                    Query query = mRoot.child(Consts.USER_STATUS_KEY).orderByChild(USERNAME)
                            .equalTo(gname);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                print.t(getActivity(), "username exists");
                                return;
                            } else {
                                mRoot.child(Consts.USER_STATUS_KEY).child(current_user_id).child("username").setValue(gname);
                                mRoot.child(Consts.USER_STATUS_KEY).child(current_user_id).child("description").setValue(gdesc);
                                mRoot.child(Consts.USER_STATUS_KEY).child(current_user_id).child("website").setValue(gwebsite);
                                mRoot.child(Consts.USER_STATUS_KEY).child(current_user_id).child("display_name").setValue(gdisplayname);

                                getActivity().finish();
                            }
//                        if(!gemail.equals(saved_email)){
//                            getPassword();
//
//                        }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });
                }
            }


    }
    private void getPassword(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View view = getActivity().getLayoutInflater().inflate(R.layout.getpassword, null, false);
        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText edit = view.findViewById(R.id.getpassword);
                String password = edit.getText().toString();

                print.t(getActivity(), password);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

            }
        });
        builder.create().show();
    }
}





