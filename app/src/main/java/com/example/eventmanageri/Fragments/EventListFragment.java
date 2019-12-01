package com.example.eventmanageri.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventmanageri.Adapters.CommentAdapter;
import com.example.eventmanageri.Adapters.EventAdapter;
import com.example.eventmanageri.Models.Comment;
import com.example.eventmanageri.Models.Event;
import com.example.eventmanageri.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;


public class EventListFragment extends Fragment {
    // UI reference
    private RecyclerView mRecyclerView;
    MaterialSearchView materialSearchView;

    // Data reference
    private String userId, share, following, currentUser, uDisplayName;
    private List<Event> events = new ArrayList<>();

    // Adapter reference
    public EventAdapter mEventAdapter;

    // DB reference
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseFollowRef;
    private DatabaseReference mDatabaseFollowing;
    private FirebaseUser mUser;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);


        // Define UI reference
        materialSearchView = (MaterialSearchView) view.findViewById(R.id.searchView);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_events);
        mRecyclerView.setHasFixedSize(true);

        // Define DB reference
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference("events");
        mDatabaseFollowRef = mDatabase.getReference("follow");
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUser = mUser.getUid();
        uDisplayName = mUser.getDisplayName();

        // List events
        loadEvent();

        return view;
    }


    // <------------------------ List ------------------------>
    // List events
    public void loadEvent() {
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                events.clear();
                List<String> keys = new ArrayList<>();

                for(DataSnapshot keyNode : dataSnapshot.getChildren()){

                    keys.add(keyNode.getKey());
                    Event event = keyNode.getValue(Event.class);

                    // Get value of share, userId to check permission
                    share = event.getShare();
                    userId = event.getUserId();
                    mDatabaseFollowing = mDatabaseFollowRef.child(currentUser).child("following").child(userId);


                    // If an event is shared or the user is the owner or Admin
                    // then display the event
                    // if(currentUser.equals(userId)) {
                    if (share.equals("Yes") && mDatabaseFollowing.equals(userId)) {
                        events.add(event);
                        setUpRecyclerView();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void setUpRecyclerView() {
        mEventAdapter = new EventAdapter(events);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mEventAdapter);
    }

    // <------------------------ Menu ------------------------>
    // Menu: "Search"
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.eventlist_activity_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.findItem(R.id.search_event);
        materialSearchView.setMenuItem(item);

        // Search Events
        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                final List<Event> filterList = filter(events, newText);
                mEventAdapter.setFilter(filterList);
                return false;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    // <----------------------- Search ----------------------->
    // Search events
    private List<Event> filter(List<Event> pl, String query) {
        query = query.toLowerCase();
        final List<Event> filterList = new ArrayList<>();
        for (Event model:pl) {
            final String text = model.getTitle().toLowerCase();
            if (text.startsWith(query)) {
                filterList.add(model);
            }
        }
        return filterList;
    }
}
