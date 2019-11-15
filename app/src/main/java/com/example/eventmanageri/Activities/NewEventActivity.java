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


public class NewEventActivity extends AppCompatActivity {

    // Define variables
    private EditText mTitle_editTxt, mMemo_editTxt;
    private ImageView mPhoto_imgView;
    private EditText mPhoto_editTxt, mVideo_editTxt, mLocation_editTxt;
    private TextView mDate_viewTxt;
    private Spinner mType_sp, mShare_sp;
    private Button mBtnDate, mBtnUpPhoto, mBtnUpVideo, mBtnAdd, mBtnCancel;

    // upload photo

    private Uri mPhotoUrl;
    private String downloadUrl;
    private StorageReference mStorageRef;
    //private DatabaseReference mDatabaseRef;
    //private StorageTask mUpPhoto;
    private static final int PICK_Photo_Request = 1;
    // end

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
    // Choose Date
    private void chooseDate() {
        Intent intent = new Intent(NewEventActivity.this, CalendarActivity.class);
        startActivity(intent);
    }
    // Go to Event Lists
    private void goToList() {
        Intent goToList = new Intent(NewEventActivity.this, EventListActivity.class);
        startActivity(goToList);
    }
    Event event = new Event();


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




    // Create New Event
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        mTitle_editTxt = (EditText) findViewById(R.id.title_editTxt);
        mMemo_editTxt = (EditText) findViewById(R.id.memo_editTxt);
        //mPhoto_imgView = (ImageView) findViewById(R.id.photo_imgView);
        mPhoto_editTxt = (EditText) findViewById(R.id.photo_editTxt);
        mVideo_editTxt = (EditText) findViewById(R.id.video_editTxt);
        mLocation_editTxt = (EditText) findViewById(R.id.location_editTxt);
        mType_sp = (Spinner) findViewById(R.id.type_sp);
        mShare_sp = (Spinner) findViewById(R.id.share_sp);
        mDate_viewTxt = (TextView) findViewById(R.id.date_viewTxt);

        mBtnDate = (Button) findViewById(R.id.btnDate);
        mBtnUpPhoto = (Button) findViewById(R.id.btnUpPhoto);
        mBtnUpVideo = (Button) findViewById(R.id.btnUpVideo);
        mBtnAdd = (Button) findViewById(R.id.btnAdd);
        mBtnCancel = (Button) findViewById(R.id.btnCancel);

        // Upload
        mStorageRef = FirebaseStorage.getInstance().getReference();
        //mDatabaseRef = FirebaseDatabase.getInstance().getReference("events");

        // Get the selected date (from CalendarActivity)
        Intent i = getIntent();
        String date = i.getStringExtra("date");
        // Display the date
        mDate_viewTxt.setText(date);


        // <------------------------ Button ------------------------>
        // Choose Event Date : "CHOOSE DATE" Button
        mBtnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDate();
            }
        });

        // Choose Photo to upload : "PHOTO" Button
        mBtnUpPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        // Cancel : "CANCEL" Button (Back to the event lists)
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToList();
            }
        });

        // Save New Event : "SAVE" Button
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
}
