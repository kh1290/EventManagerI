package com.example.eventmanageri.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CalendarView;
import android.content.Intent;

import com.example.eventmanageri.R;

public class CalendarActivity extends AppCompatActivity {

    // UI reference
    private CalendarView mCalendarView;

    // Data reference
    private String title, date, type, memo, photo, video, location, share;

    // Activity reference
    public interface ActivityConstants {
        int ACTIVITY_1 = 1001;  // NewEventActivity
        int ACTIVITY_2 = 1002;  // EventModActivity
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // Define UI reference
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);

        // Get data from the calling activity
        title = getIntent().getStringExtra("title");
        memo = getIntent().getStringExtra("memo");
        photo = getIntent().getStringExtra("photo");
        video = getIntent().getStringExtra("video");
        location = getIntent().getStringExtra("location");
        type = getIntent().getStringExtra("type");
        share = getIntent().getStringExtra("share");

        // Pick date
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                // Set the selected date (format: dd/mm/yyyy)
                month = month + 1;
                date = dayOfMonth + "/" + month + "/" + year;

                // Check the calling activity and return data
                int callingActivity = getIntent().getIntExtra("calling-activity", 0);
                switch (callingActivity) {
                    case ActivityConstants.ACTIVITY_1:
                        goToNewEvent();
                        break;
                    case ActivityConstants.ACTIVITY_2:
                        goToEventMod();
                        break;
                }
            }
        });
    }

    // Return data to NewEventActivity
    private void goToNewEvent() {
        Intent intent = new Intent(CalendarActivity.this, NewEventActivity.class);
        intent.putExtra("date", date);
        intent.putExtra("title", title);
        intent.putExtra("memo", memo);
        intent.putExtra("photo", photo);
        intent.putExtra("video", video);
        intent.putExtra("location", location);
        intent.putExtra("type", type);
        intent.putExtra("share", share);
        startActivity(intent);
    }

    // Return data to EventModActivity
    private void goToEventMod() {
        Intent intent = new Intent(CalendarActivity.this, EventModActivity.class);
        intent.putExtra("date", date);
        intent.putExtra("title", title);
        intent.putExtra("memo", memo);
        intent.putExtra("photo", photo);
        intent.putExtra("video", video);
        intent.putExtra("location", location);
        intent.putExtra("type", type);
        intent.putExtra("share", share);
        startActivity(intent);
    }
}
