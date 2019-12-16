package com.example.eventmanageri.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventmanageri.Database;
import com.example.eventmanageri.Models.Event;
import com.example.eventmanageri.Models.User;
import com.example.eventmanageri.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.List;

public class ProfileEditActivity extends AppCompatActivity {

    // UI reference
    ImageView mClose;
    TextView mEmail;
    EditText mUname, mMsg;
    Button btnSaveProfile;

    // Data reference
    String key, role, email;

    // firebase reference
    private FirebaseUser mUser;
    private String mUid;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        // Define UI reference
        mClose = (ImageView) findViewById(R.id.close);
        mEmail = (TextView) findViewById(R.id.email_txtView);
        mUname = (EditText) findViewById(R.id.uname_editView);
        mMsg = (EditText) findViewById(R.id.msg_editView);
        btnSaveProfile = (Button) findViewById(R.id.btn_save_profile);

        // Define firebase reference
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUid = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference("users").child(mUid);


        // Read the profile for current user
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mEmail.setText(user.getEmail());
                mUname.setText(user.getUname());
                mMsg.setText(user.getBio());
                email = user.getEmail();
                key = user.getKey();
                role = user.getRole();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // <------------------------ Button ------------------------>
        // "EDIT PROFILE" bar msg : Return to the profile page
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // "SAVE" Button : Save updated profile
        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(mEmail.getText().toString(), mUname.getText().toString(), mMsg.getText().toString());
                Toast.makeText(ProfileEditActivity.this, "Profile has been " +
                        "updated successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    // <------------------------ Function ------------------------>
    // Update the user profile
    private void updateProfile(String email, String uname, String msg) {
        User user = new User();
        user.setEmail(email);
        user.setUname(uname);
        user.setBio(msg);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("email", email);
        hashMap.put("uname", uname);
        hashMap.put("bio", msg);

        UserProfileChangeRequest updateDisplayName = new UserProfileChangeRequest.Builder()
                .setDisplayName(uname)
                .build();
        mUser.updateProfile(updateDisplayName);

        mDatabaseRef.updateChildren(hashMap);
    }
}
