package com.example.samuel.sawft.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.samuel.sawft.Home.HomeActivity;
import com.example.samuel.sawft.Models.User;
import com.example.samuel.sawft.Models.UserDetails;
import com.example.samuel.sawft.R;
import com.example.samuel.sawft.Utils.Consts;
import com.example.samuel.sawft.Utils.StringMan;
import com.example.samuel.sawft.Utils.print;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private Button register_btn;
    private TextInputLayout email_field, password_field, fullname_field;
    private RelativeLayout layout;
    private FirebaseAuth mAuth;
    private DatabaseReference mRoot;
    private DatabaseReference mUsers;
    private static boolean exists = false;
    private static final String TAG = "RegisterActivity";
    private  String append = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mRoot = FirebaseDatabase.getInstance().getReference();
        mUsers = mRoot.child(Consts.USER_STATUS_KEY);


        setUpwidgets();
    }

    public void RegisterNewUser(View view) {
        Intent login = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(login);
    }

    public void setUpwidgets() {
        register_btn = (Button) findViewById(R.id.login_btn);
        email_field = (TextInputLayout) findViewById(R.id.email_field);
        password_field = (TextInputLayout) findViewById(R.id.Password_field);
        fullname_field = (TextInputLayout) findViewById(R.id.fullname_field);
        layout = (RelativeLayout) findViewById(R.id.dL);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register_btn.setEnabled(false);
                getDetails();
            }
        });
    }

    public void getDetails() {
        String email = email_field.getEditText().getText().toString();
        String password = password_field.getEditText().getText().toString();
        String fullname = fullname_field.getEditText().getText().toString();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(fullname)) {
            print.t(RegisterActivity.this, "Please fill all fields");

        } else {
            layout.setVisibility(View.VISIBLE);
            registerUser(email, password, fullname);

        }


    }

    public boolean checkIfUserNameExists(final String name) {

        mUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildren() == null) {
                    exists = false;
                    return;
                }
                for (DataSnapshot snaps : dataSnapshot.getChildren()) {
                    UserDetails savedUser = snaps.getValue(UserDetails.class);
                    print.l("                      " + "usernames from database : " + savedUser.getUsername() + " vs " + name);
                    if (name.equals(savedUser.getUsername())) {
                        exists = true;
                    } else {
                        exists = false;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        print.l("                                                 exist status:  " + String.valueOf(exists));

        return exists;
    }

    private void registerUser(final String email, String password, final String name) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                layout.setVisibility(View.INVISIBLE);
                register_btn.setEnabled(true);

                if (task.isSuccessful()) {
                    Map userStatus = new HashMap<>();
                    String desc = "....Sawft...";
                    String user_id = mAuth.getCurrentUser().getUid();
                    String website = "www.google.com";
                    String user_name = StringMan.getUserName(name);
                    append = mAuth.getCurrentUser().getUid().substring(0,6);
                    print.l("formed username:                                                             " + user_name);
                    print.l( "                                                                              "+append );
                    String randUsername = StringMan.getUserName(name) + append;
                    UserDetails newUser;
                    User registeredUser;


                    if (checkIfUserNameExists(user_name)) {
                        Log.d(TAG, "onComplete: user name exists");

                        newUser = new UserDetails(desc, name, randUsername, website
                                , "default", 0, 0, 0);
                        registeredUser = new User(email,randUsername,0,user_id);

                    } else {
                        Log.d(TAG, "onComplete: username doesn't exist");

                        newUser = new UserDetails(desc, name, user_name, website
                                , "default", 0, 0, 0);
                        registeredUser = new User(email,user_name,0,user_id);
                    }

                    userStatus.put(Consts.USER_STATUS_KEY + "/" +user_id, newUser);
                    userStatus.put(Consts.USERS_KEY + "/" +user_id,registeredUser);
                    mRoot.updateChildren(userStatus);
                    sendToHome();

                } else {
                    print.t(RegisterActivity.this, "Some error occured");
                }
            }
        });
    }
    public  String getUserName(String name){
        return name.replace(" ",".");
    }
    public  String getnewUsername(String name){
        return  name.replace(" ",".") + append;

    }

    private void sendToHome() {
        Intent Home = new Intent(RegisterActivity.this, HomeActivity.class);
        startActivity(Home);
        finish();
    }

}
