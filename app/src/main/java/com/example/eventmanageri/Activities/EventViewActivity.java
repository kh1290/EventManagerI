package com.example.eventmanageri.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventmanageri.Adapters.CommentAdapter;
import com.example.eventmanageri.Models.Comment;
import com.example.eventmanageri.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Locale;


public class EventViewActivity extends AppCompatActivity {

    private TextView textViewRate, mTitle_viewTxt, mUser_viewTxt, mDate_viewTxt, mMemo_viewTxt, mPhoto_viewTxt,
            mVideo_viewTxt, mLocation_viewTxt, mType_viewTxt;
    // private ImageView mPhoto_imgView;

    private String message, key, eventid, userid, title, date, memo, photo,
            video, location, share, type, uId, uDisplayName;
    private EditText mComment;
    private Button mBtnUpdate, mBtnAddComment, mBtnRate;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private RatingBar ratingRatingBar;
    RecyclerView RvComment;
    List<Comment> listComment;
    CommentAdapter commentAdapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);

        RvComment = findViewById(R.id.rv_comment);
        mTitle_viewTxt = (TextView) findViewById(R.id.title_textView);
        mUser_viewTxt = (TextView) findViewById(R.id.user_viewTxt);
        mDate_viewTxt = (TextView) findViewById(R.id.date_viewTxt);
        mMemo_viewTxt = (TextView) findViewById(R.id.memo_txtView);
        mType_viewTxt = (TextView) findViewById(R.id.type_txtView);
        mPhoto_viewTxt = (TextView) findViewById(R.id.Photo_txtView);
        mVideo_viewTxt = (TextView) findViewById(R.id.Video_txtView);
        mLocation_viewTxt = (TextView) findViewById(R.id.Location_txtView);
        mComment = (EditText) findViewById(R.id.comment_editView);
        mBtnUpdate = (Button) findViewById(R.id.btnUpdate);
        mBtnAddComment = (Button) findViewById(R.id.btnAddComment);
        ratingRatingBar = (RatingBar) findViewById(R.id.ratingBar4);
        mBtnRate = (Button)findViewById(R.id.button2);
        final TextView ratingDisplayTextView = (TextView)findViewById(R.id.textViewRate);


        mDatabase = FirebaseDatabase.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        uId = mUser.getUid();
        uDisplayName = mUser.getDisplayName();


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
        mUser_viewTxt.setText(userid);
        mDate_viewTxt.setText(date);
        mMemo_viewTxt.setText(memo);
        mType_viewTxt.setText(type);
        mPhoto_viewTxt.setText(photo);
        mVideo_viewTxt.setText(video);
        mLocation_viewTxt.setText(location);

        // Edit Event : "Rate" Button
        mBtnRate.setOnClickListener(new View.OnClickListener() {
               @Override
                public void onClick(View v) {

                    ratingDisplayTextView.setText("Your rating is:"+ratingRatingBar.getRating());

                }
            });

        // Edit Event : "Comment" Button
        mBtnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBtnAddComment.setVisibility(View.INVISIBLE);
                DatabaseReference commentRef = mDatabase.getReference("comment").child(eventid).push();
                String comment_content = mComment.getText().toString();
                String uid = mUser.getUid();
                String uname = mUser.getDisplayName();
                Comment comment = new Comment(comment_content, uid, uname);

                commentRef.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EventViewActivity.this,"Comment added",
                                Toast.LENGTH_SHORT).show();
                        mComment.setText("");
                        mBtnAddComment.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EventViewActivity.this,"Unsuccessful",
                                Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

        iniRvComment();

    }

    private void iniRvComment() {
        RvComment.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference commentRefence = mDatabase.getReference("comment").child(eventid);
        //ValueEventListener valueEventListener =
        commentRefence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listComment = new ArrayList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {

                    Comment comment = snap.getValue(Comment.class);
                    listComment.add(comment);
                }

                commentAdapter = new CommentAdapter(getApplicationContext(),listComment);
                RvComment.setAdapter(commentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Menu: "Update"
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_view_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.update_event:
                if (uDisplayName.equals(userid)) {
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
                    intent.putExtra("share",share);
                    startActivity(intent);

                } else {
                    Toast.makeText(EventViewActivity.this,"You do not have permission",Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String timestampToString(long time) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String commentDate = DateFormat.getDateInstance().toString();
        return commentDate;
    }

    //

}