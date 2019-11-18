package com.example.eventmanageri.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.eventmanageri.Adapters.EventAdapter;
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


public class EventListActivity extends AppCompatActivity {
    // UI reference
    private RecyclerView mRecyclerView;
    MaterialSearchView materialSearchView;

    // Data reference
    private String userId, share, uDisplayName;
    private List<Event> events = new ArrayList<>();

    // Adapter reference
    public EventAdapter mEventAdapter;

    // DB reference
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        // Define UI reference
        materialSearchView = (MaterialSearchView) findViewById(R.id.searchView);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_events);
        mRecyclerView.setHasFixedSize(true);

        // Define DB reference
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference("events");
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        uDisplayName = mUser.getDisplayName();

        // List events
        loadEvent();
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

                    // If an event is shared or the user is the owner or Admin
                    // then display the event
                    if(share.equals("Yes") || uDisplayName.equals(userId) || uDisplayName.equals("Admin")) {
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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mEventAdapter);
    }


    // <------------------------ Menu ------------------------>
    // Menu: "Search", "New Event"
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.eventlist_activity_menu, menu);
        MenuItem item = menu.findItem(R.id.search_event);
        materialSearchView.setMenuItem(item);

        // Search Events
        // materialSearchView.closeSearch();
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
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_event:
                startActivity(new Intent(this, NewEventActivity.class));
                return true;
            case R.id.home:
                startActivity(new Intent(this, HomeBarActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
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