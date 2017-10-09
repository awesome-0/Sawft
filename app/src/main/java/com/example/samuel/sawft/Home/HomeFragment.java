package com.example.samuel.sawft.Home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
    private ArrayList<Photo> mPaginatedPhotos = new ArrayList<>();
    private  ArrayList<String> mFollowing;
    String current_user_id;
    public int Results;
    int iterations;
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
        mPaginatedPhotos = new ArrayList<>();
        if(mPhotos != null) {
            Collections.sort(mPhotos, new Comparator<Photo>() {
                @Override
                public int compare(Photo photo, Photo t1) {
                    return photo.getDate_created().compareTo(t1.getDate_created());

                }

            });
            Collections.reverse(mPhotos);
             Results =3;
             iterations = mPhotos.size();
            if(iterations > 3){
               iterations = 3;
            }
            for(int i = 0;i<iterations;i++){
                mPaginatedPhotos.add(mPhotos.get(i));
            }
            adapter = new MainFeedListAdapter(getContext(),R.layout.mainfeed,mPaginatedPhotos);

            listView.setAdapter(adapter);
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {

                }

                @Override
                public void onScroll(AbsListView absListView,int firstVisibleItem,int visibleItemCount, final int totalItemCount) {
                    if (totalItemCount > 0)
                    {
                        int lastInScreen = firstVisibleItem + visibleItemCount;
                        if(lastInScreen == totalItemCount)
                        {
                            displayMorePhotos();
                        }
                    }

                }
            });

        }

    }
    private void displayMorePhotos(){
        if(mPhotos.size() > Results && mPhotos.size() >0){
            try{
                if(mPhotos.size() > (Results + 3)){

                    Log.d(TAG, "displayMorePhotos: there are still more than 3 photos");
                    iterations = 3;
                }
                else{
                    iterations = mPhotos.size() - Results;
                }
                for(int i = Results ;i<Results+iterations;i++){
                 mPaginatedPhotos.add(mPhotos.get(i));
                }
                Results+=iterations;
                adapter.notifyDataSetChanged();

            }catch (NullPointerException e){
                Log.e(TAG, "displayMorePhotos: NullPointerException "+ e.getMessage() );
            }catch (IndexOutOfBoundsException e){
                Log.e(TAG, "displayMorePhotos: IndexOutOfBoundsException "+ e.getMessage() );
            }
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
                    mFollowing.add(fUid);
                    //juuu
                }
                getPhotos();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
