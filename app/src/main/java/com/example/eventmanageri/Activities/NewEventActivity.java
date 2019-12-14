        package com.example.eventmanageri.Activities;

        import android.content.ContentResolver;
        import android.content.Intent;
        import android.net.Uri;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.webkit.MimeTypeMap;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.MediaController;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;
        import android.widget.VideoView;

        import androidx.appcompat.app.AppCompatActivity;

        import com.example.eventmanageri.Activities.CalendarActivity.ActivityConstants;
        import com.example.eventmanageri.Database;
        import com.example.eventmanageri.Models.Event;
        import com.example.eventmanageri.R;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.StorageReference;
        import com.google.firebase.storage.UploadTask;

        import java.util.List;


        public class NewEventActivity extends AppCompatActivity {

        // UI reference
        private EditText mTitle_editTxt, mMemo_editTxt, mVideo_editTxt, mLocation_editTxt;
        private TextView mDate_viewTxt;
        private ImageView mPhoto_imgView;
        private VideoView mVideo_videoView;
        private MediaController mediaC;
        private Spinner mType_sp, mShare_sp;
        private Button mBtnDate, mBtnUpPhoto, mBtnUpVideo, mBtnAdd, mBtnCancel;

        // Data reference
        private String title, date, type, memo, photo, photoUrl, video, location, share;

        // Photo reference
        private Uri mPhotoUri;
        private static final int PICK_Photo_Request = 1;


        // firebase reference
        private FirebaseDatabase mDatabase;
        private DatabaseReference mDatabaseRef;
        private FirebaseStorage mStorage;
        private StorageReference mStoragePhotoRef, mStorageVideoRef;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        // Define UI reference
        mTitle_editTxt = (EditText) findViewById(R.id.title_editTxt);
        mDate_viewTxt = (TextView) findViewById(R.id.date_viewTxt);
        mMemo_editTxt = (EditText) findViewById(R.id.memo_editTxt);
        mPhoto_imgView = (ImageView) findViewById(R.id.photo_imgView);
        mVideo_editTxt = (EditText) findViewById(R.id.video_editTxt);
        mVideo_videoView = (VideoView) findViewById(R.id.video_videoView);
        mLocation_editTxt = (EditText) findViewById(R.id.location_editTxt);
        mType_sp = (Spinner) findViewById(R.id.type_sp);
        mShare_sp = (Spinner) findViewById(R.id.share_sp);
        mBtnDate = (Button) findViewById(R.id.btnDate);
        mBtnUpPhoto = (Button) findViewById(R.id.btnUpPhoto);
        mBtnUpVideo = (Button) findViewById(R.id.btnUpVideo);
        mBtnAdd = (Button) findViewById(R.id.btnAdd);
        mBtnCancel = (Button) findViewById(R.id.btnCancel);
        mediaC = new MediaController(this);



        // Define firebase reference
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference("events");
        mStorage = FirebaseStorage.getInstance();
        mStoragePhotoRef = mStorage.getReference("photo");
        mStorageVideoRef = mStorage.getReference("video");


        // <------------------------ Data ------------------------>
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
        newEvent();
        goToList();
        }
        });
        }

        //Video

        public void playVideo(View v) {
//            String path = "android.resource://com.example.eventmanageri.Activities/"+R.raw.cloud;
        String path = "android.resource://" + getPackageName() +"/" + R.raw.cloud;
        Uri u = Uri.parse(path);

        mVideo_videoView.setVideoURI(u);
        mVideo_videoView.setMediaController(mediaC);
        mediaC.setAnchorView(mVideo_videoView);
        mVideo_videoView.start();
        Log.d("NewEventActivity", "111111: "+mVideo_videoView);
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
        // The user has picked a photo successfully
        // Save its reference to a Uri variable
        mPhotoUri = data.getData();
        mPhoto_imgView.setImageURI(mPhotoUri);

        }
        }


        // Create new event
        private void newEvent() {
        // Upload a file to storage with random name
        if (mPhotoUri == null) {
        // If the user does not pick photo, set "null" to "photo" on firebase
        Event event = new Event();
        event.setTitle(mTitle_editTxt.getText().toString());
        event.setDate(mDate_viewTxt.getText().toString());
        event.setMemo(mMemo_editTxt.getText().toString());
        event.setPhoto("NONE");
        event.setVideo(mVideo_editTxt.getText().toString());
        event.setLocation(mLocation_editTxt.getText().toString());
        event.setType(mType_sp.getSelectedItem().toString());
        event.setShare(mShare_sp.getSelectedItem().toString());

        new Database().addEvent(event, new Database.DataStatus() {
        @Override
        public void DataIsLoaded(List<Event> events) { //, List<String> keys

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

        } else {
        // If the user picks photo, set url to "photo" on firebase
        final StorageReference PhotoRef = mStoragePhotoRef.child(System.currentTimeMillis() + "." +
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
        event.setTitle(mTitle_editTxt.getText().toString());
        event.setDate(mDate_viewTxt.getText().toString());
        event.setMemo(mMemo_editTxt.getText().toString());
        event.setPhoto(photoUrl);
        event.setVideo(mVideo_editTxt.getText().toString());
        event.setLocation(mLocation_editTxt.getText().toString());
        event.setType(mType_sp.getSelectedItem().toString());
        event.setShare(mShare_sp.getSelectedItem().toString());

        new Database().addEvent(event, new Database.DataStatus() {
        @Override
        public void DataIsLoaded(List<Event> events) { //, List<String> keys

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

        // Choose Date
        private void chooseDate() {
        // Put data to CalendarActivity
        Intent intent = new Intent(NewEventActivity.this, CalendarActivity.class);
        intent.putExtra("calling-activity",ActivityConstants.ACTIVITY_1);
        intent.putExtra("title",mTitle_editTxt.getText().toString());
        intent.putExtra("memo",mMemo_editTxt.getText().toString());
        intent.putExtra("photo",photo);
        intent.putExtra("video",mVideo_editTxt.getText().toString());
        intent.putExtra("location",mLocation_editTxt.getText().toString());
        intent.putExtra("type",mType_sp.getSelectedItem().toString());
        intent.putExtra("share",mShare_sp.getSelectedItem().toString());
        startActivityForResult(intent,1001);
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
