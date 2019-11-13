package com.example.eventmanageri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class EventViewActivity extends AppCompatActivity {

    private TextView mTitle_viewTxt, mDate_viewTxt, mMemo_viewTxt, mPhoto_viewTxt,
            mVideo_viewTxt, mLocation_viewTxt, mType_viewTxt, mShare_sw;
    // private ImageView mPhoto_imgView;

    private String key, eventid, userid, title, date, memo, photo, video, location, share, type;
    private Button mBtnUpdate;

    private String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);

        mTitle_viewTxt = (TextView) findViewById(R.id.title_textView);
        mDate_viewTxt = (TextView) findViewById(R.id.date_viewTxt);
        mMemo_viewTxt = (TextView) findViewById(R.id.memo_txtView);
        mType_viewTxt = (TextView) findViewById(R.id.type_txtView);
        mPhoto_viewTxt = (TextView) findViewById(R.id.Photo_txtView);
        mVideo_viewTxt = (TextView) findViewById(R.id.Video_txtView);
        mLocation_viewTxt = (TextView) findViewById(R.id.Location_txtView);
        mShare_sw = (TextView) findViewById(R.id.Share_textView);
        mBtnUpdate = (Button) findViewById(R.id.btnUpdate);

        key = getIntent().getStringExtra("key");
        eventid = getIntent().getStringExtra("eventid");
        userid = getIntent().getStringExtra("userid");
        title = getIntent().getStringExtra("title");
        date = getIntent().getStringExtra("date");
        memo = getIntent().getStringExtra("memo");
        photo = getIntent().getStringExtra("photo");
        video = getIntent().getStringExtra("video");
        location = getIntent().getStringExtra("location");
        share = getIntent().getStringExtra("share");
        type = getIntent().getStringExtra("type");

        mTitle_viewTxt.setText(title);
        mDate_viewTxt.setText(date);
        mMemo_viewTxt.setText(memo);
        mType_viewTxt.setText(type);
        mPhoto_viewTxt.setText(photo);
        mVideo_viewTxt.setText(video);
        mLocation_viewTxt.setText(location);
        mShare_sw.setText(share);


        // Edit Event : "UPDATE" Button
        mBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to EventModActivity if the current user is the owner
                if (currentUser.equals(userid)) {
                    Intent intent = new Intent(EventViewActivity.this, EventModActivity.class);
                    intent.putExtra("key",eventid);
                    intent.putExtra("userid",userid);
                    intent.putExtra("title",mTitle_viewTxt.getText().toString());
                    intent.putExtra("type",mType_viewTxt.getText().toString());
                    intent.putExtra("date",mDate_viewTxt.getText().toString());
                    intent.putExtra("memo",mMemo_viewTxt.getText().toString());
                    intent.putExtra("photo",mPhoto_viewTxt.getText().toString());
                    intent.putExtra("video",mVideo_viewTxt.getText().toString());
                    intent.putExtra("location",mLocation_viewTxt.getText().toString());
                    intent.putExtra("share",mShare_sw.getText().toString());
                    startActivity(intent);

                } else {
                    Toast.makeText(EventViewActivity.this,"You do not have permission",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
