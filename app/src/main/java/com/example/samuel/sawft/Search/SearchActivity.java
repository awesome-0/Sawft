package com.example.samuel.sawft.Search;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.samuel.sawft.Models.User;
import com.example.samuel.sawft.R;
import com.example.samuel.sawft.Utils.BottomNavigationHelper;
import com.example.samuel.sawft.Utils.Consts;
import com.example.samuel.sawft.Utils.UserListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    private static final int ACTIVITY_NUMBER = 1;
    private EditText search;
    private ListView listview;
    private List<User> mUsers;
    private UserListAdapter adapter;
    private static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        listview = (ListView) findViewById(R.id.search_list_view);
        search = (EditText) findViewById(R.id.search_users);
        setUpBottomNavBar();
        hideKeyboard();
        initTextWatcher();
    }

    public void setUpBottomNavBar(){

        BottomNavigationViewEx bottomBar = (BottomNavigationViewEx) findViewById(R.id.bottom_nav_bar);
        BottomNavigationHelper.setUpBottomNavToolBar(bottomBar);
        BottomNavigationHelper.enableNavigation(SearchActivity.this,this,bottomBar);
        Menu menu = bottomBar.getMenu();
        MenuItem item = menu.getItem(ACTIVITY_NUMBER);
        item.setChecked(true);
    }
    private void searchUsers(String keyword){
        Log.d(TAG, "searchUsers: searching for uses");
        mUsers.clear();
        if(keyword.length() == 0){

        }else{
            Query usersQuery = FirebaseDatabase.getInstance().getReference().child(Consts.USERS_KEY)
                    .orderByChild(Consts.USERNAME).equalTo(keyword);
            usersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                 //   Log.d(TAG, "onDataChange: found a match" + dataSnapshot.getValue(User.class).toString());
                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        Log.d(TAG, "onDataChange: number of users found " + dataSnapshot.getChildrenCount());
                        mUsers.add(singleSnapshot.getValue(User.class));
                    }
                    updateUserList();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    private void updateUserList() {
        adapter = new UserListAdapter(SearchActivity.this,R.layout.search_row,mUsers);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick: navigating to profile ");
            }
        });
    }

    private void initTextWatcher(){
        mUsers = new ArrayList<>();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged: search changed");
                String text = search.getText().toString().toLowerCase(Locale.getDefault());
                searchUsers(text);

            }
        });
    }
    private void hideKeyboard(){
        if(getCurrentFocus() != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

        }
    }
}
