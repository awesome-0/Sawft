package com.example.samuel.sawft;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.samuel.sawft.Home.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private Button loginBtn ;
    private TextInputLayout email_field,password_field;
    private RelativeLayout layout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        setUpwidgets();
    }

    public void RegisterUser(View view) {

        Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(registerIntent);
    }

    public void setUpwidgets(){
        loginBtn = (Button) findViewById(R.id.login_btn);
        email_field = (TextInputLayout) findViewById(R.id.email_field);
        password_field = (TextInputLayout) findViewById(R.id.Password_field);
        layout = (RelativeLayout) findViewById(R.id.dL);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginBtn.setEnabled(false);
                getDetails();
            }
        });
    }
    public void getDetails(){
        String email = email_field.getEditText().getText().toString();
        String password = password_field.getEditText().getText().toString();
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            print.t(LoginActivity.this,"Please fill all fields");

        }
        else{
            layout.setVisibility(View.VISIBLE);
            loginUser(email,password);

        }


    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                layout.setVisibility(View.INVISIBLE);
                loginBtn.setEnabled(true);

                if(task.isSuccessful()){
                    Intent Home = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(Home);
                    finish();
                }
                else{
                    print.t(LoginActivity.this,"Some error occured");
                }
            }
        });
    }


}
