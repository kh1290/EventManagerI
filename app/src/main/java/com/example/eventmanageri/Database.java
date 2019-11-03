package com.example.eventmanageri;

import androidx.annotation.NonNull;
import java.util.List;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Database {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceEvents;
    private List<Event> events = new ArrayList<>();

    public interface DataStatus {
        void DataIsLoaded(List<Event> events, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public Database() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceEvents = mDatabase.getReference("events");
    }

    // DataIsLoaded
    public void readEvents(final DataStatus dataStatus) {
        mReferenceEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                events.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Event event = keyNode.getValue(Event.class);
                    events.add(event);
                }
                dataStatus.DataIsLoaded(events,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // DataIsInserted
    public void addEvent(Event event, final DataStatus dataStatus) {
        String key = mReferenceEvents.push().getKey();
        mReferenceEvents.child(key).setValue(event).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsInserted();
            }
        });
    }

    // DataIsUpdated
    public void updateEvent(String key, Event event, final DataStatus dataStatus) {
        mReferenceEvents.child(key).setValue(event)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsUpdated();
            }
        });
    }

    // DataIsDeleted
    public void deleteEvent(String key, final DataStatus dataStatus) {
        mReferenceEvents.child(key).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsDeleted();
            }
        });
    }

}
