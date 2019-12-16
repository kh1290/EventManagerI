package com.example.eventmanageri.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventmanageri.Activities.FollowerActivity;
import com.example.eventmanageri.Activities.ProfileEditActivity;
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


public class ProfileFragment extends Fragment {
    // UI reference
    ImageView mOptions;
    TextView mEvents, mFollowers, mFollowing, mUname, mBio, mEmail;
    Button btnEditProfile;
    String profileid, uDisplayName, uname;

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

        // Define UI reference
        mOptions = (ImageView) view.findViewById(R.id.options);
        mEvents = (TextView) view.findViewById(R.id.events);
        mFollowers = (TextView) view.findViewById(R.id.followers);
        mFollowing = (TextView) view.findViewById(R.id.following);
        mUname = (TextView) view.findViewById(R.id.uname);
        mBio = (TextView) view.findViewById(R.id.bio);
        mEmail = (TextView) view.findViewById(R.id.fullname);
        btnEditProfile = (Button) view.findViewById(R.id.btn_edit_profile);

        // Define DB reference
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseUserRef = mDatabase.getReference("users").child(profileid);
        mDatabaseEventRef = mDatabase.getReference("events");
        mDatabaseFollowRef = mDatabase.getReference("follow");
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        uDisplayName = mUser.getDisplayName();


        userInfo();
        getFollowers();
        getNrEvents();

        // <------------------------ Button ------------------------>
        // Button "text" depending on user
        if (profileid.equals(mUser.getUid())) {
            // Compare current user's uid with selected user's uid
            if ("Admin".equals(mUser.getDisplayName())) {
                // If the user is Administrator, button says "ADMINISTRATOR"
                btnEditProfile.setText("ADMINISTRATOR");
            } else {
                // If the user selects his own profile, button says "Edit Profile"
                btnEditProfile.setText("Edit Profile");
            }
        } else {
            // If the user selects other user's profile, check follow/following
            checkFollow();
        }

        // Button "function" depending on user
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text on button
                String btn = btnEditProfile.getText().toString();

                if (btn.equals("Edit Profile")) {
                    // Go to ProfileEditActivity
                    startActivity(new Intent(getContext(), ProfileEditActivity.class));
                } else if (btn.equals("follow")) {
                    // Add follow info into firebase
                    mDatabaseFollowRef.child(mUser.getUid())
                            .child("following").child(profileid).setValue(true);
                    mDatabaseFollowRef.child(profileid)
                            .child("followers").child(mUser.getUid()).setValue(true);
                } else if (btn.equals("following")) {
                    // Delete following info from firebase
                    mDatabaseFollowRef.child(mUser.getUid())
                            .child("following").child(profileid).removeValue();
                    mDatabaseFollowRef.child(profileid)
                            .child("followers").child(mUser.getUid()).removeValue();
                } else if (btn.equals("ADMINISTRATOR")) {
                    // Do nothing
                }
            }
        });

        // When the user clicks "followers"
        mFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FollowerActivity.class);
                intent.putExtra("id", profileid);
                intent.putExtra("title", "followers");
                intent.putExtra("uname", uname);
                startActivity(intent);
            }
        });

        // When the user clicks "following"
        mFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FollowerActivity.class);
                intent.putExtra("id", profileid);
                intent.putExtra("title", "following");
                intent.putExtra("uname", uname);
                startActivity(intent);
            }
        });

        return view;
    }

    // <------------------------ Function ------------------------>
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
                uname = user.getUname();
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
