package com.example.eventmanageri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.os.Bundle;
import android.widget.CalendarView;
import android.content.Intent;

public class CalendarActivity extends AppCompatActivity {

    private static final String TAG = "CalendarActivity";
    private CalendarView mCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        mCalendarView = (CalendarView) findViewById(R.id.calendarView);

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "/" + month + "/" + year;
                Log.d(TAG, "onSelectedDayChange: date " + date);

                Intent intent = new Intent(CalendarActivity.this, NewEventActivity.class);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });
    }
}
