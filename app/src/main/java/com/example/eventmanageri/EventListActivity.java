package com.example.eventmanageri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class EventListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_events);
        new Database().readEvents(new Database.DataStatus() {
            @Override
            public void DataIsLoaded(List<Event> events, List<String> keys) {
                new RecyclerView_Config().setConfig(mRecyclerView, EventListActivity.this, events, keys);
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
    }

    // Add New Event (menu)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.eventlist_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
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
