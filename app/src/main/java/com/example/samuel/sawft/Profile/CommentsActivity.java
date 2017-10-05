package com.example.samuel.sawft.Profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.samuel.sawft.Models.Comment;
import com.example.samuel.sawft.Models.Photo;
import com.example.samuel.sawft.R;
import com.example.samuel.sawft.Utils.CommentsAdapter;
import com.example.samuel.sawft.Utils.Consts;
import com.example.samuel.sawft.Utils.print;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CommentsActivity extends AppCompatActivity {
    private Photo photo;
    private static final String TAG = "CommentsActivity";
    private ListView listview;
    private ArrayList<Comment> comList;
    private CommentsAdapter adapter;
    private ChildEventListener getComments;
    private DatabaseReference mRoot;
    private FirebaseAuth mAuth;
    private EditText mComments;
    private ImageView mSendComments, back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        listview = (ListView) findViewById(R.id.comments_list_view);
        comList = new ArrayList<>();
        mRoot = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        if (getIntent() != null) {
            photo = (Photo) getIntent().getExtras().getSerializable("photo");
            Log.d(TAG, "onCreate: photo:" + photo.toString());
        }

        mComments = (EditText) findViewById(R.id.comments_edit_text);
        mSendComments = (ImageView) findViewById(R.id.send_comments);

        setChildEventListener();
        adapter = new CommentsAdapter(CommentsActivity.this, R.layout.comments_row, comList);
        listview.setAdapter(adapter);
        mSendComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mComments.getText().toString().equals("")) {
                    sendComments(mComments.getText().toString());
                    mComments.setText("");

                } else {
                    print.t(CommentsActivity.this, "You cant post an empty comment");
                }
            }
        });
        back_btn = (ImageView) findViewById(R.id.backarrow);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void sendComments(String s) {
        String comId = mRoot.push().getKey();
        Comment comment = new Comment();
        comment.setUser_id(mAuth.getCurrentUser().getUid());
        comment.setTimestamp(String.valueOf(System.currentTimeMillis()));
        comment.setComment(s);
        mRoot.child(Consts.PHOTOS_KEY).child(photo.getPhoto_id())
                .child(Consts.COMMENTS).child(comId).setValue(comment);
        if (photo.getUser_id() == mAuth.getCurrentUser().getUid()) {

            mRoot.child(Consts.USER_PHOTOS_KEY).
                    child(mAuth.getCurrentUser().getUid()).child(photo.getPhoto_id())
                    .child(Consts.COMMENTS).child(comId).setValue(comment);
        }
        closeKeyboard();
    }

    private void closeKeyboard() {
        View view = getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void setChildEventListener() {

        getComments = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e(TAG, "onChildAdded: new child added " + dataSnapshot.toString());


                Comment comment = dataSnapshot.getValue(Comment.class);
                comList.add(comment);


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        Comment firstComment = new Comment();
        firstComment.setComment(photo.getCaption());
        firstComment.setTimestamp(photo.getDate_created());
        firstComment.setUser_id(photo.getUser_id());
        comList.add(firstComment);

        mRoot.child(Consts.PHOTOS_KEY).child(photo.getPhoto_id())
                .child(Consts.COMMENTS).addChildEventListener(getComments);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRoot.child(Consts.PHOTOS_KEY).child(photo.getPhoto_id())
                .child(Consts.COMMENTS).removeEventListener(getComments);
        comList.clear();
    }
}
