package com.example.samuel.sawft.Home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.samuel.sawft.Models.Follow;
import com.example.samuel.sawft.Models.Photo;
import com.example.samuel.sawft.R;
import com.example.samuel.sawft.Utils.Consts;
import com.example.samuel.sawft.Utils.MainFeedListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private FirebaseAuth mAuth;
    private DatabaseReference mRoot;
    private ListView listView;
    private ArrayList<Photo> mPhotos;
    private  ArrayList<String> mFollowing;
    String current_user_id;
    MainFeedListAdapter adapter;




    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_home, container, false);
        listView = view.findViewById(R.id.home_listview);
        mRoot = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mFollowing = new ArrayList<>();
        mPhotos = new ArrayList<>();
        if(mAuth.getCurrentUser()!= null){
            current_user_id = mAuth.getCurrentUser().getUid();
        }


        getFollowing();



        return view;
    }

    private void getPhotos() {
        for(int i = 0;i<mFollowing.size();i++){
            Query query = mRoot.child(Consts.PHOTOS_KEY)
                    .orderByChild(Consts.USER_ID).equalTo(mFollowing.get(i));
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        Photo photo = singleSnapshot.getValue(Photo.class);
                        mPhotos.add(photo);
                    }

                    displayphotos();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        Log.e(TAG, "onDataChange: photos size " + mPhotos.size() );

    }

    private void displayphotos() {
        if(mPhotos != null) {
            Collections.sort(mPhotos, new Comparator<Photo>() {
                @Override
                public int compare(Photo photo, Photo t1) {
                    return photo.getDate_created().compareTo(t1.getDate_created());

                }

            });
            Collections.reverse(mPhotos);
            adapter = new MainFeedListAdapter(getContext(),R.layout.mainfeed,mPhotos);
            listView.setAdapter(adapter);

        }

    }

    private void getFollowing() {
        mFollowing.clear();
        mFollowing.add(current_user_id);
        Query query = mRoot.child(Consts.USERS_KEY)
        .child(current_user_id).child(Consts.FOLLOWING);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnaphot : dataSnapshot.getChildren()){
                    String fUid = singleSnaphot.getValue(Follow.class).getUser_id();
                   // Log.e(TAG, "onDataChange: following " + fUid );
                    mFollowing.add(fUid);

                }
                Log.e(TAG, "onDataChange: inside the get following method folowing size is " + mFollowing.size() );
                getPhotos();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
