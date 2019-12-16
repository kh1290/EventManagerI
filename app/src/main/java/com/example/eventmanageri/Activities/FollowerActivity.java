package com.example.eventmanageri.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.eventmanageri.Adapters.UserAdapter;
import com.example.eventmanageri.Models.User;
import com.example.eventmanageri.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FollowerActivity extends AppCompatActivity {
    // Data reference
    String id, title, uname;
    TextView mUname;
    RecyclerView mRecyclerView;
    UserAdapter userAdapter;
    List<String> idList;
    List<User> userList;

    // DB reference
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseFollowerRef;
    private DatabaseReference mDatabaseFollowingRef;
    private DatabaseReference mDatabaseUserRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);

        // Get data from ProfileFragment
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        title = intent.getStringExtra("title");
        uname = intent.getStringExtra("uname");

        // Define DB reference
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseFollowingRef = mDatabase.getReference("follow").child(id).child("following");
        mDatabaseFollowerRef = mDatabase.getReference("follow").child(id).child("followers");
        mDatabaseUserRef = mDatabase.getReference("users");

        // Define data reference
        mUname = findViewById(R.id.uname);
        mRecyclerView = findViewById(R.id.follower_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        idList = new ArrayList<>();
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList, this);
        mRecyclerView.setAdapter(userAdapter);

        mUname.setText(uname);


        // Check if an user chooses following or followers
        switch (title) {
            case "following":
                getFollowing();
                break;
            case "followers":
                getFollowers();
                break;
        }
    }

    // <------------------------ Function ------------------------>
    // Get following info
    private void getFollowing() {
        mDatabaseFollowingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                idList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    idList.add(snapshot.getKey());
                }
                showUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Get followers info
    private void getFollowers() {
        mDatabaseFollowerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                idList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    idList.add(snapshot.getKey());
                }
                showUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // List users depending on following/followers
    private void showUsers() {
        mDatabaseUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    for (String id : idList) {
                        if (user.getKey().equals(id)) {
                            userList.add(user);
                        }
                    }
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
