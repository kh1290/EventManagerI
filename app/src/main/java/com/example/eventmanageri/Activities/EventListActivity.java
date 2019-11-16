package com.example.eventmanageri.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.eventmanageri.Database;
import com.example.eventmanageri.Models.Event;
import com.example.eventmanageri.R;
import com.example.eventmanageri.Adapters.RecyclerView_Config;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;

public class EventListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    MaterialSearchView materialSearchView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_events);
        materialSearchView = (MaterialSearchView) findViewById(R.id.searchView);

        // Load Events
        new Database().readEvents(new Database.DataStatus() {
            @Override
            public void DataIsLoaded(List<Event> events, List<String> keys) {
                new RecyclerView_Config().setConfig(mRecyclerView,
                        EventListActivity.this, events, keys);
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });


        // Search Events
        materialSearchView.closeSearch();
        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                //// add
                return false;
            }
        });
    }


    // Menu: "Search", "New Event"
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.eventlist_activity_menu, menu);
        MenuItem item = menu.findItem(R.id.search_event);
        materialSearchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_event:
                startActivity(new Intent(this, NewEventActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}