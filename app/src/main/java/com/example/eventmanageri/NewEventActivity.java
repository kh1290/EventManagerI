package com.example.eventmanageri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;


public class NewEventActivity extends AppCompatActivity {

    private EditText mTitle_editTxt;
    private EditText mMemo_editTxt;
    private EditText mPhoto_editTxt;
    private EditText mVideo_editTxt;
    private EditText mLocation_editTxt;
    private TextView mDate_viewTxt;
    private Spinner mType_sp;
    private EditText mShare_sw;

    private Button mBtnAdd;
    private Button mBtnCancel;
    private Button mBtnDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        mTitle_editTxt = (EditText) findViewById(R.id.title_editTxt);
        mMemo_editTxt = (EditText) findViewById(R.id.memo_editTxt);
        mPhoto_editTxt = (EditText) findViewById(R.id.photo_editTxt);
        mVideo_editTxt = (EditText) findViewById(R.id.video_editTxt);
        mLocation_editTxt = (EditText) findViewById(R.id.location_editTxt);
        mType_sp = (Spinner) findViewById(R.id.type_sp);
        mShare_sw = (EditText) findViewById(R.id.share_sw);
        mDate_viewTxt = (TextView) findViewById(R.id.date_viewTxt);

        mBtnDate = (Button) findViewById(R.id.btnDate);
        mBtnAdd = (Button) findViewById(R.id.btnAdd);
        mBtnCancel = (Button) findViewById(R.id.btnCancel);

        // Get the selected date (from CalendarActivity)
        Intent incomingIntent = getIntent();
        String date = incomingIntent.getStringExtra("date");
        // Display the date
        mDate_viewTxt.setText(date);

        // Choose Event Date : "CHOOSE DATE" Button
        mBtnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewEventActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        // Create New Event : "SAVE" Button
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event event = new Event();
                event.setTitle(mTitle_editTxt.getText().toString());
                event.setDate(mDate_viewTxt.getText().toString());
                event.setMemo(mMemo_editTxt.getText().toString());
                event.setPhoto(mPhoto_editTxt.getText().toString());
                event.setVideo(mVideo_editTxt.getText().toString());
                event.setLocation(mLocation_editTxt.getText().toString());
                event.setType(mType_sp.getSelectedItem().toString());
                event.setShare(mShare_sw.getText().toString());

                new Database().addEvent(event, new Database.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Event> events, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {
                        Toast.makeText(NewEventActivity.this, "The event has been saved successfully", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });

                // Back to the event lists
                Intent goToList = new Intent(NewEventActivity.this, EventListActivity.class);
                startActivity(goToList);
            }
        });

        // Cancel : "CANCEL" Button (Back to the event lists)
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToList = new Intent(NewEventActivity.this, EventListActivity.class);
                startActivity(goToList);
            }
        });
    }
}
