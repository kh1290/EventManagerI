package com.example.eventmanageri;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class EventModActivity extends AppCompatActivity {


    private EditText mTitle_editTxt;
    private EditText mMemo_editTxt;
    private EditText mPhoto_editTxt;
    private EditText mVideo_editTxt;
    private EditText mLocation_editTxt;
    private Spinner mType_sp;
    private EditText mShare_sw;
    private Button mBtnUpdate;
    private Button mBtnDelete;
    private Button mBtnCancel;

    private String key;
    private String title;
    private String memo;
    private String photo;
    private String video;
    private String location;
    private String share;
    private String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_mod);

        key = getIntent().getStringExtra("key");
        title = getIntent().getStringExtra("title");
        memo = getIntent().getStringExtra("memo");
        photo = getIntent().getStringExtra("photo");
        video = getIntent().getStringExtra("video");
        location = getIntent().getStringExtra("location");
        share = getIntent().getStringExtra("share");
        type = getIntent().getStringExtra("type");

        mTitle_editTxt = (EditText) findViewById(R.id.title_editTxt);
        mTitle_editTxt.setText(title);
        mMemo_editTxt = (EditText) findViewById(R.id.memo_editTxt);
        mMemo_editTxt.setText(memo);
        mPhoto_editTxt = (EditText) findViewById(R.id.photo_editTxt);
        mPhoto_editTxt.setText(photo);
        mVideo_editTxt = (EditText) findViewById(R.id.video_editTxt);
        mVideo_editTxt.setText(video);
        mLocation_editTxt = (EditText) findViewById(R.id.location_editTxt);
        mLocation_editTxt.setText(location);
        mType_sp = (Spinner) findViewById(R.id.type_sp);
        mType_sp.setSelection(getIndex_SpinnerItem(mType_sp, type));

        mShare_sw = (EditText) findViewById(R.id.share_sw);
        mShare_sw.setText(share);

        mBtnUpdate = (Button) findViewById(R.id.btnUpdate);
        mBtnDelete = (Button) findViewById(R.id.btnDelete);
        mBtnCancel = (Button) findViewById(R.id.btnCancel);

        mBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Event event = new Event();
                event.setTitle(mTitle_editTxt.getText().toString());
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
                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });
            }
        });

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
                        finish(); return;
                    }
                });
            }
        });

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); return;
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
}
