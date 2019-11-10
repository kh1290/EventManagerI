package com.example.eventmanageri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class EventModActivity extends AppCompatActivity {

    // UI references
    private EditText mTitle_editTxt, mMemo_editTxt,
            mPhoto_editTxt, mVideo_editTxt, mLocation_editTxt, mShare_sw;
    private TextView mDate_viewTxt;
    private Spinner mType_sp;
    private Button mBtnDate, mBtnUpdate, mBtnDelete, mBtnCancel;

    // Data references
    private String key, title, date, memo, type, photo, video, location, share;

    // Choose Date
    private void chooseDate() {
        Intent intent = new Intent(EventModActivity.this, CalendarActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_mod);

        // Define UI references
        mTitle_editTxt = (EditText) findViewById(R.id.title_editTxt);
        mDate_viewTxt = (TextView) findViewById(R.id.date_viewTxt);
        mMemo_editTxt = (EditText) findViewById(R.id.memo_editTxt);
        mPhoto_editTxt = (EditText) findViewById(R.id.photo_editTxt);
        mVideo_editTxt = (EditText) findViewById(R.id.video_editTxt);
        mLocation_editTxt = (EditText) findViewById(R.id.location_editTxt);
        mType_sp = (Spinner) findViewById(R.id.type_sp);
        mShare_sw = (EditText) findViewById(R.id.share_sw);
        mBtnDate = (Button) findViewById(R.id.btnDate);
        mBtnUpdate = (Button) findViewById(R.id.btnUpdate);
        mBtnDelete = (Button) findViewById(R.id.btnDelete);
        mBtnCancel = (Button) findViewById(R.id.btnCancel);

        // Get data from EventListActivity
        key = getIntent().getStringExtra("key");
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
        mShare_sw.setText(share);


        // <------------------------ Button ------------------------>
        // Choose Event Date : "CHOOSE DATE" Button
        mBtnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDate();
            }
        });

        // Update Event : "UPDATE" Button
        mBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Event event = new Event();

                event.setEventId(key);
                event.setTitle(mTitle_editTxt.getText().toString());
                event.setDate(mDate_viewTxt.getText().toString());
                event.setMemo(mMemo_editTxt.getText().toString());
                event.setPhoto(mPhoto_editTxt.getText().toString());
                event.setVideo(mVideo_editTxt.getText().toString());
                event.setLocation(mLocation_editTxt.getText().toString());
                event.setType(mType_sp.getSelectedItem().toString());
                event.setShare(mShare_sw.getText().toString());

                new Database().updateEvent(key, event, new Database.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Event> events, List<String> keys) {

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

        // Delete Event : "DELETE" Button
        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database().deleteEvent(key, new Database.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Event> events, List<String> keys) {

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

        // Cancel : "CANCEL" Button (Back to the event lists)
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToList();
            }
        });
    }

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

    // Go to Event Lists
    private void goToList() {
        Intent goToList = new Intent(EventModActivity.this, EventListActivity.class);
        startActivity(goToList);
    }
}
