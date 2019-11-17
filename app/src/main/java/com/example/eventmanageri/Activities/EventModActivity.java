package com.example.eventmanageri.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventmanageri.Database;
import com.example.eventmanageri.Models.Event;
import com.example.eventmanageri.R;

import java.util.List;
import com.example.eventmanageri.Activities.CalendarActivity.ActivityConstants;

public class EventModActivity extends AppCompatActivity {

    // UI reference
    private EditText mTitle_editTxt, mMemo_editTxt, mPhoto_editTxt,
            mVideo_editTxt, mLocation_editTxt;
    private TextView mDate_viewTxt;
    private Spinner mType_sp, mShare_sp;
    private Button mBtnDate, mBtnUpdate, mBtnDelete, mBtnCancel;

    // Data reference
    private String key, userid, title, date, memo, type, photo, video, location, share;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_mod);

        // Define UI reference
        mTitle_editTxt = (EditText) findViewById(R.id.title_editTxt);
        mDate_viewTxt = (TextView) findViewById(R.id.date_viewTxt);
        mMemo_editTxt = (EditText) findViewById(R.id.memo_editTxt);
        mPhoto_editTxt = (EditText) findViewById(R.id.photo_editTxt);
        mVideo_editTxt = (EditText) findViewById(R.id.video_editTxt);
        mLocation_editTxt = (EditText) findViewById(R.id.location_editTxt);
        mType_sp = (Spinner) findViewById(R.id.type_sp);
        mShare_sp = (Spinner) findViewById(R.id.share_sp);
        mBtnDate = (Button) findViewById(R.id.btnDate);
        mBtnUpdate = (Button) findViewById(R.id.btnUpdate);
        mBtnDelete = (Button) findViewById(R.id.btnDelete);
        mBtnCancel = (Button) findViewById(R.id.btnCancel);

        // Get data from EventListActivity
        key = getIntent().getStringExtra("key");
        userid = getIntent().getStringExtra("userid");
        title = getIntent().getStringExtra("title");
        date = getIntent().getStringExtra("date");
        memo = getIntent().getStringExtra("memo");
        photo = getIntent().getStringExtra("photo");
        video = getIntent().getStringExtra("video");
        location = getIntent().getStringExtra("location");
        share = getIntent().getStringExtra("share");
        type = getIntent().getStringExtra("type");

        // Set data to display
        mTitle_editTxt.setText(title);
        mDate_viewTxt.setText(date);
        mMemo_editTxt.setText(memo);
        mPhoto_editTxt.setText(photo);
        mVideo_editTxt.setText(video);
        mLocation_editTxt.setText(location);
        mType_sp.setSelection(getIndex_SpinnerItem(mType_sp, type));
        mShare_sp.setSelection(getIndex_SpinnerItem(mShare_sp, share));


        // <------------------------ Button ------------------------>
        // "CHOOSE DATE" Button : Choose the date
        mBtnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDate();
            }
        });

        // "UPDATE" Button : Update the event
        mBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Event event = new Event();
                event.setEventId(key);
                event.setUserId(userid);
                event.setTitle(mTitle_editTxt.getText().toString());
                event.setDate(mDate_viewTxt.getText().toString());
                event.setMemo(mMemo_editTxt.getText().toString());
                event.setPhoto(mPhoto_editTxt.getText().toString());
                event.setVideo(mVideo_editTxt.getText().toString());
                event.setLocation(mLocation_editTxt.getText().toString());
                event.setType(mType_sp.getSelectedItem().toString());
                event.setShare(mShare_sp.getSelectedItem().toString());

                new Database().updateEvent(key, event, new Database.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Event> events) { //, List<String> keys

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {
                        Toast.makeText(EventModActivity.this, "The event has been " +
                                "updated successfully", Toast.LENGTH_LONG).show();
                        goToList();
                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });
            }
        });

        // "DELETE" Button : Delete the event
        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database().deleteEvent(key, new Database.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Event> events) { //, List<String> keys

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {
                        Toast.makeText(EventModActivity.this, "The event has been " +
                                "deleted successfully", Toast.LENGTH_LONG).show();
                        goToList();
                    }
                });
            }
        });

        // "CANCEL" Button : Back to the event lists
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToList();
            }
        });
    }

    // <------------------------ Function ------------------------>
    // Choose Date
    private void chooseDate() {
        // Put data to CalendarActivity
        Intent intent = new Intent(EventModActivity.this, CalendarActivity.class);
        intent.putExtra("calling-activity",ActivityConstants.ACTIVITY_2);
        intent.putExtra("title",mTitle_editTxt.getText().toString());
        intent.putExtra("memo",mMemo_editTxt.getText().toString());
        intent.putExtra("photo",mPhoto_editTxt.getText().toString());
        intent.putExtra("video",mVideo_editTxt.getText().toString());
        intent.putExtra("location",mLocation_editTxt.getText().toString());
        intent.putExtra("type",mType_sp.getSelectedItem().toString());
        intent.putExtra("share",mShare_sp.getSelectedItem().toString());
        startActivity(intent);
    }

    // Get spinner value
    private int getIndex_SpinnerItem(Spinner spinner, String item) {
        int index = 0;
        for (int i = 0; i<spinner.getCount(); i++) {
            if(spinner.getItemAtPosition(i).equals(item)) {
                index = i;
                break;
            }
        }
        return index;
    }

    // Go to EventListActivity
    private void goToList() {
        Intent goToList = new Intent(EventModActivity.this, EventListActivity.class);
        startActivity(goToList);
    }
}
