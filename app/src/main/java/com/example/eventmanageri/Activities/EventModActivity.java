package com.example.eventmanageri.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventmanageri.Database;
import com.example.eventmanageri.Models.Event;
import com.example.eventmanageri.R;

import java.util.Calendar;
import java.util.List;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EventModActivity extends AppCompatActivity {

    // UI reference
    private EditText mTitle_editTxt, mMemo_editTxt, mVideo_editTxt, mLocation_editTxt;
    private TextView mDate_viewTxt;
    private ImageView mPhoto_imgView;
    private Spinner mType_sp, mShare_sp;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Button mBtnUpPhoto, mBtnUpdate, mBtnDelete, mBtnCancel;

    // Data reference
    private String key, userid, email, title, date, memo, type, photo, photoUrl, video, location, share, uDisplayName;

    // Photo reference
    private Uri mPhotoUri;
    private static final int PICK_Photo_Request = 1;

    // firebase reference
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;
    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_mod);

        // Define UI reference
        mTitle_editTxt = (EditText) findViewById(R.id.title_editTxt);
        mDate_viewTxt = (TextView) findViewById(R.id.date_viewTxt);
        mMemo_editTxt = (EditText) findViewById(R.id.memo_editTxt);
        mPhoto_imgView = (ImageView) findViewById(R.id.imageView3);
        mVideo_editTxt = (EditText) findViewById(R.id.video_editTxt);
        mLocation_editTxt = (EditText) findViewById(R.id.location_editTxt);
        mType_sp = (Spinner) findViewById(R.id.type_sp);
        mShare_sp = (Spinner) findViewById(R.id.share_sp);
        mBtnUpPhoto = (Button) findViewById(R.id.btnUpPhoto);
        mBtnUpdate = (Button) findViewById(R.id.btnUpdate);
        mBtnDelete = (Button) findViewById(R.id.btnDelete);
        mBtnCancel = (Button) findViewById(R.id.btnCancel);

        // Define firebase reference
        mDatabase = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = mDatabase.getReference("events");
        mStorageRef = mStorage.getReference("photo");
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        uDisplayName = mUser.getDisplayName();

        // <------------------------ Data ------------------------>
        // Get data from EventListActivity
        key = getIntent().getStringExtra("key");
        userid = getIntent().getStringExtra("userid");
        email = getIntent().getStringExtra("email");
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
        mVideo_editTxt.setText(video);
        mLocation_editTxt.setText(location);
        mType_sp.setSelection(getIndex_SpinnerItem(mType_sp, type));
        mShare_sp.setSelection(getIndex_SpinnerItem(mShare_sp, share));
        if (photo != "NONE") {
            // If there is photo, get image from url
            Picasso.get().load(photo).into(mPhoto_imgView);
        }

        // <------------------------- Date ------------------------->
        // "Date" TextView : Choose date
        mDate_viewTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        EventModActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = month + "/" + day + "/" + year;
                mDate_viewTxt.setText(date);
            }
        };
        // <------------------------ Button ------------------------>
        // "PHOTO" Button : Choose photo to upload
        mBtnUpPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        // "UPDATE" Button : Update the event
        mBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEvent();
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
    // Choose photo
    private void choosePhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_Photo_Request);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_Photo_Request && resultCode == RESULT_OK) {
            // The user has picked an photo successfully
            // Save its reference to a Uri variable
            mPhotoUri = data.getData();
            mPhoto_imgView.setImageURI(mPhotoUri);
        }
    }

    // Update the event
    private void updateEvent() {
        // Upload a file to storage with random name
        if (mPhotoUri == null) {
            // If the user does not pick photo, set "NONE" to "photo" on firebase
            Event event = new Event();
            event.setEventId(key);
            event.setUserId(userid);
            event.setEmail(uDisplayName);
            event.setTitle(mTitle_editTxt.getText().toString());
            event.setDate(mDate_viewTxt.getText().toString());
            event.setMemo(mMemo_editTxt.getText().toString());
            event.setPhoto(photo);
            event.setVideo(mVideo_editTxt.getText().toString());
            event.setLocation(mLocation_editTxt.getText().toString());
            event.setType(mType_sp.getSelectedItem().toString());
            event.setShare(mShare_sp.getSelectedItem().toString());

            new Database().updateEvent(key, event, new Database.DataStatus() {
                @Override
                public void DataIsLoaded(List<Event> events) {

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

        } else {
            // If the user picks photo, set url to "photo" on firebase
            final StorageReference PhotoRef = mStorageRef.child(System.currentTimeMillis() + "." +
                    getFileExtension(mPhotoUri));
            PhotoRef.putFile(mPhotoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Photo uploaded successfully
                    // Get download url
                    PhotoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            photoUrl = uri.toString();

                            Event event = new Event();
                            event.setEventId(key);
                            event.setUserId(userid);
                            event.setEmail(uDisplayName);
                            event.setTitle(mTitle_editTxt.getText().toString());
                            event.setDate(mDate_viewTxt.getText().toString());
                            event.setMemo(mMemo_editTxt.getText().toString());
                            event.setPhoto(photoUrl);
                            event.setVideo(mVideo_editTxt.getText().toString());
                            event.setLocation(mLocation_editTxt.getText().toString());
                            event.setType(mType_sp.getSelectedItem().toString());
                            event.setShare(mShare_sp.getSelectedItem().toString());

                            new Database().updateEvent(key, event, new Database.DataStatus() {
                                @Override
                                public void DataIsLoaded(List<Event> events) {

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
                }
            });
        }
    }
    private String getFileExtension(Uri uri) {
        ContentResolver Cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(Cr.getType(uri));
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

    // Go to HomeEventActivity
    private void goToList() {
        Intent goToList = new Intent(getApplicationContext(), HomeEventActivity.class);
        startActivity(goToList);
    }
}
