package com.example.eventmanageri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.*;
import android.os.Bundle;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    TextView mGoToList_ViewTxt;
    Button mbtnSignOut;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mGoToList_ViewTxt = findViewById(R.id.hello);
        mbtnSignOut = findViewById(R.id.btnSignOut);

        // Go to the login activity : "Sign out" Button
        mbtnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent GoToMain = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(GoToMain);
            }
        });

        mGoToList_ViewTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent GoToList = new Intent(HomeActivity.this, TestActivity.class);
                startActivity(GoToList);
            }
        });
    }
}
