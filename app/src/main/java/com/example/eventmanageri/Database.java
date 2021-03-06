package com.example.eventmanageri;

import androidx.annotation.NonNull;
import java.util.List;

import com.example.eventmanageri.Models.Event;
import com.example.eventmanageri.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.ArrayList;

public class Database {
    // Firebase Database
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseUserRef;

    // Firebase Storage
    private FirebaseStorage mStorage;
    private StorageReference mStoragePhotoRef;
    private StorageReference mStorageVideoRef;
    private FirebaseUser mUser;

    // Events
    private List<Event> events = new ArrayList<>();
    private String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String userId, share, uDisplayName;

    public interface UserStatus {
        void UserIsInserted();
    }

    public interface DataStatus {
        void DataIsLoaded(List<Event> events); //, List<String> keys
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public Database() {
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference("events");
        mDatabaseUserRef = mDatabase.getReference("users");
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        uDisplayName = mUser.getDisplayName();

        mStorage = FirebaseStorage.getInstance();
        mStoragePhotoRef = mStorage.getReference("photo");
        mStorageVideoRef = mStorage.getReference("video");
    }

    // UserIsInserted
    public void addUser(User user, final UserStatus userStatus) {
        String key = currentUser;

        user.setKey(currentUser);
        user.setRole("user");
        user.setBio("");

        mDatabaseUserRef.child(key).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                userStatus.UserIsInserted();
            }
        });
    }

    // DataIsLoaded
    public void readEvents(final DataStatus dataStatus) {
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();

                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Event event = keyNode.getValue(Event.class);
                    share = event.getShare();
                    userId = event.getUserId();

                    // If an event is shared, then display the event
                    if(share.equals("Yes") || userId.equals(currentUser)) {
                        events.add(event);
                    }
                }
                dataStatus.DataIsLoaded(events);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // DataIsInserted
    public void addEvent(Event event, final DataStatus dataStatus) {
        String key = mDatabaseRef.push().getKey();

        event.setEventId(key);
        event.setUserId(currentUser);
        event.setEmail(uDisplayName);

        mDatabaseRef.child(key).setValue(event).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsInserted();
            }
        });
    }

    // DataIsUpdated
    public void updateEvent(String key, Event event, final DataStatus dataStatus) {
        mDatabaseRef.child(key).setValue(event)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                            dataStatus.DataIsUpdated();
                    }
                });
    }

    // DataIsDeleted
    public void deleteEvent(String key, final DataStatus dataStatus) {
        mDatabaseRef.child(key).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsDeleted();
            }
        });
    }
}
