package com.example.eventmanageri.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.UUID;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;

import com.example.eventmanageri.Database;
import com.example.eventmanageri.Models.Event;
import com.example.eventmanageri.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import android.content.ContentResolver;

import java.util.List;
import android.content.Context;

public class NewEventActivity extends AppCompatActivity {

    // UI reference
    private EditText mTitle_editTxt, mMemo_editTxt, mPhoto_editTxt,
            mVideo_editTxt, mLocation_editTxt;
    private TextView mDate_viewTxt;
    private ImageView mPhoto_imgView;
    private Spinner mType_sp, mShare_sp;
    private Button mBtnDate, mBtnUpPhoto, mBtnUpVideo, mBtnAdd, mBtnCancel;

    // Data reference
    private String title, date, type, memo, photo, video,  location, share;

    // Photo reference
    private Uri mPhotoUrl;
    private String downloadUrl;
    private StorageReference mStorageRef;
    //private DatabaseReference mDatabaseRef;
    //private StorageTask mUpPhoto;
    private static final int PICK_Photo_Request = 1;
    // end
    Event event = new Event();


    // Create New Event
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        // Define UI reference
        mTitle_editTxt = (EditText) findViewById(R.id.title_editTxt);
        mDate_viewTxt = (TextView) findViewById(R.id.date_viewTxt);
        mMemo_editTxt = (EditText) findViewById(R.id.memo_editTxt);
        //mPhoto_imgView = (ImageView) findViewById(R.id.photo_imgView);
        mPhoto_editTxt = (EditText) findViewById(R.id.photo_editTxt);
        mVideo_editTxt = (EditText) findViewById(R.id.video_editTxt);
        mLocation_editTxt = (EditText) findViewById(R.id.location_editTxt);
        mType_sp = (Spinner) findViewById(R.id.type_sp);
        mShare_sp = (Spinner) findViewById(R.id.share_sp);
        mBtnDate = (Button) findViewById(R.id.btnDate);
        mBtnUpPhoto = (Button) findViewById(R.id.btnUpPhoto);
        mBtnUpVideo = (Button) findViewById(R.id.btnUpVideo);
        mBtnAdd = (Button) findViewById(R.id.btnAdd);
        mBtnCancel = (Button) findViewById(R.id.btnCancel);

        // Upload
        mStorageRef = FirebaseStorage.getInstance().getReference();
        //mDatabaseRef = FirebaseDatabase.getInstance().getReference("events");


        // Get data from CalendarActivity
        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        title = intent.getStringExtra("title");
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

        // "PHOTO" Button : Choose photo to upload
        mBtnUpPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        // "CANCEL" Button : Back to the event lists
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToList();
            }
        });

        // "SAVE" Button : Save new event
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();

                event.setPhotoUrl(downloadUrl);
                event.setTitle(mTitle_editTxt.getText().toString());
                event.setDate(mDate_viewTxt.getText().toString());
                event.setMemo(mMemo_editTxt.getText().toString());
                event.setPhotoUrl(mPhoto_editTxt.getText().toString());
                event.setPhoto(mPhoto_editTxt.getText().toString());
                event.setVideo(mVideo_editTxt.getText().toString());
                event.setLocation(mLocation_editTxt.getText().toString());
                event.setType(mType_sp.getSelectedItem().toString());
                event.setShare(mShare_sp.getSelectedItem().toString());

                new Database().addEvent(event, new Database.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Event> events, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {

                        Toast.makeText(NewEventActivity.this, "The event has been " +
                                "saved successfully", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });
                goToList();
            }
        });
    }

    // <------------------------ Function ------------------------>
    // Choose Photo
    private void choosePhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_Photo_Request);
    }
    private String getFileExtension(Uri uri) {
        ContentResolver Cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(Cr.getType(uri));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_Photo_Request && requestCode == RESULT_OK && data != null && data.getData() != null) {
            mPhotoUrl = data.getData();
            Picasso.with(this).load(mPhotoUrl).into(mPhoto_imgView);
        }
    }

    private void uploadFile() {
        if (mPhotoUrl != null) {
            StorageReference PhotoRef = mStorageRef.child("images"+ UUID.randomUUID().toString());
            PhotoRef.putFile(mPhotoUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    downloadUrl = taskSnapshot.getStorage().getDownloadUrl().toString();
                }
            });
            /*
            StorageReference PhotoRef = mStorageRef.child(System.currentTimeMillis() + "." +
                    getFileExtension(mPhotoUrl));
            //mUpPhoto =
            PhotoRef.putFile(mPhotoUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Event event = new Event();
                    event.setTitle(mTitle_editTxt.getText().toString());
                    event.setDate(mDate_viewTxt.getText().toString());
                    event.setMemo(mMemo_editTxt.getText().toString());
                    event.setPhotoUrl(taskSnapshot.getStorage().getDownloadUrl().toString());
                    event.setPhoto(mPhoto_editTxt.getText().toString());
                    event.setVideo(mVideo_editTxt.getText().toString());
                    event.setLocation(mLocation_editTxt.getText().toString());
                    event.setType(mType_sp.getSelectedItem().toString());
                    event.setShare(mShare_sp.getSelectedItem().toString());

                    String upId = mDatabaseRef.push().getKey();
                    mDatabaseRef.child(upId).setValue(event);
                }
            });

             */
        } else {
            Toast.makeText(this, "You haven't selected any file", Toast.LENGTH_SHORT).show();
        }
    }


    // Choose Date
    private void chooseDate() {
        // Put data to CalendarActivity
        Intent intent = new Intent(NewEventActivity.this, CalendarActivity.class);
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
        Intent goToList = new Intent(NewEventActivity.this, EventListActivity.class);
        startActivity(goToList);
    }
}
