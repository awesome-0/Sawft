package com.example.samuel.sawft;

import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.samuel.sawft.Models.Photo;
import com.example.samuel.sawft.Utils.Consts;
import com.example.samuel.sawft.Utils.StringMan;
import com.example.samuel.sawft.Utils.print;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NextActivity extends AppCompatActivity {
    EditText mCaption;
    FirebaseAuth mAuth;
    StorageReference mStorage;
    DatabaseReference mRoot;
    int imagecount = 0;
    String current_user_id;
    TextView share;
    String photo_to_be_shared;

    private static final String TAG = "NextActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        setUpFirebase();
        if(mAuth.getCurrentUser() != null){
            current_user_id = mAuth.getCurrentUser().getUid();
        }
        setUpWidgets();
        getImageCount();
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPhoto();
            }
        });
       // uploadPhoto();
    }

    private void uploadPhoto() {
      StorageReference ImagePath = mStorage.child(Consts.FIREBASE_IMAGE_LOCATION + "/" + current_user_id + "/photo" + (imagecount + 1));

        File file = new File(photo_to_be_shared);
        ImagePath.putFile(Uri.fromFile(file)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if(task.isSuccessful()){
                    String time = String.valueOf(System.currentTimeMillis());
                    String caption = mCaption.getText().toString();
                    String tag = StringMan.getTags(caption);
                    String imgUrl = task.getResult().getDownloadUrl().toString();
                    String photo_id = mRoot.child(Consts.USER_PHOTS_KEY).child(current_user_id)
                            .push().getKey();
                    Photo newPhoto = new Photo(caption,time,tag,imgUrl,photo_id,current_user_id);
                    Map addPhoto = new HashMap<>();
                    addPhoto.put(Consts.USER_PHOTS_KEY + "/" + current_user_id + "/" + photo_id,newPhoto);
                    addPhoto.put(Consts.PHOTOS_KEY + "/" + photo_id,newPhoto);
                    mRoot.updateChildren(addPhoto);
                    print.t(NextActivity.this,"Photo upload Successful");

                }
                else{
                    print.t(NextActivity.this,"Photo upload failed");

                }
            }
        });

    }

    public void setUpWidgets(){

        ImageView image = (ImageView) findViewById(R.id.shareImage);
         share = (TextView) findViewById(R.id.share_post);

        mCaption = (EditText) findViewById(R.id.share_caption);
        ImageView back_btn = (ImageView) findViewById(R.id.share_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if(getIntent()!= null) {
            photo_to_be_shared = getIntent().getStringExtra(getString(R.string.next_image));
            Picasso.with(NextActivity.this).load("file://" + photo_to_be_shared).fit().centerCrop().placeholder(R.drawable.ic_default_avatar)
            .into(image);
        }

    }
    private void setUpFirebase(){
        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        mRoot = FirebaseDatabase.getInstance().getReference();

    }
    public void getImageCount(){
        mRoot.child(Consts.USER_PHOTS_KEY).child(current_user_id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               imagecount = (int) dataSnapshot.getChildrenCount();
                Log.d(TAG, "onDataChange: number of photos " + imagecount);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
