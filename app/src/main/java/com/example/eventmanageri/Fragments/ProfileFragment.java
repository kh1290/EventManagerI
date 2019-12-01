package com.example.eventmanageri.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eventmanageri.Models.Event;
import com.example.eventmanageri.Models.User;
import com.example.eventmanageri.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class ProfileFragment extends Fragment {

    // UI reference
    ImageView mOptions;
    TextView mEvents, mFollowers, mFollowing, mUname, mBio, mEmail;
    Button btnEditProfile;
    String profileid;

    // DB reference
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseUserRef;
    private DatabaseReference mDatabaseFollowRef;
    private DatabaseReference mDatabaseEventRef;
    private FirebaseUser mUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profileid = prefs.getString("profileid", "none");

        // Define DB reference
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseUserRef = mDatabase.getReference("users").child(profileid);
        mDatabaseEventRef = mDatabase.getReference("events");
        mDatabaseFollowRef = mDatabase.getReference("follow");
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        Log.d("ProfileFragment","profileid sss: " + profileid);

        //mProfile = view.findViewById(R.id.)
        mOptions = (ImageView) view.findViewById(R.id.options);
        mEvents = (TextView) view.findViewById(R.id.events);
        mFollowers = (TextView) view.findViewById(R.id.followers);
        mFollowing = (TextView) view.findViewById(R.id.following);
        mUname = (TextView) view.findViewById(R.id.uname);
        mBio = (TextView) view.findViewById(R.id.bio);
        mEmail = (TextView) view.findViewById(R.id.fullname);
        btnEditProfile = (Button) view.findViewById(R.id.btn_edit_profile);

        userInfo();
        getFollowers();
        getNrEvents();

        if (profileid.equals(mUser.getUid())) {
            btnEditProfile.setText("Edit Profile");
        } else {
            checkFollow();

        }

        // Button
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn = btnEditProfile.getText().toString();

                if (btn.equals("Edit Profile")) {

                } else if (btn.equals("follow")) {
                    mDatabaseFollowRef.child(mUser.getUid())
                            .child("following").child(profileid).setValue(true);
                    mDatabaseFollowRef.child(profileid)
                            .child("followers").child(mUser.getUid()).setValue(true);

                } else if (btn.equals("following")) {
                    mDatabaseFollowRef.child(mUser.getUid())
                            .child("following").child(profileid).removeValue();
                    mDatabaseFollowRef.child(profileid)
                            .child("followers").child(mUser.getUid()).removeValue();
                }
            }
        });

        return view;
    }

    // Get user profile
    private void userInfo() {
        mDatabaseUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (getContext() == null) {
                    return;
                }

                User user = dataSnapshot.getValue(User.class);

                mUname.setText(user.getUname());
                mEmail.setText(user.getEmail());
                mBio.setText(user.getBio());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }

    // Check if followed
    private void checkFollow() {
        DatabaseReference reference = mDatabaseFollowRef.child(mUser.getUid())
                .child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(profileid).exists()) {
                    btnEditProfile.setText("following");
                } else {
                    btnEditProfile.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Get the number of followers
    private void getFollowers() {
        DatabaseReference reference = mDatabaseFollowRef.child(profileid)
                .child("followers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mFollowers.setText(""+dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference referencel = mDatabaseFollowRef.child(profileid)
                .child("following");
        referencel.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mFollowing.setText(""+dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Get the number of events
    private void getNrEvents() {
        mDatabaseEventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Event event = snapshot.getValue(Event.class);

                    if (event.getUserId().equals(profileid)) {
                        i++;
                    }
                }
                mEvents.setText(""+i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
