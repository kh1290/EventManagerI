package com.example.eventmanageri.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmanageri.Adapters.CommentAdapter;
import com.example.eventmanageri.Models.Comment;
import com.example.eventmanageri.Models.Rating;
import com.example.eventmanageri.Models.User;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class EventViewActivity extends AppCompatActivity {

    // UI reference
    private TextView mTitle_viewTxt, mUser_viewTxt, mDate_viewTxt, mMemo_viewTxt,
            mVideo_viewTxt, mLocation_viewTxt, mType_viewTxt;
    private EditText mComment;
    private ImageView mPhoto_imgView;
    private VideoView mVideo_View;
    private RatingBar ratingRatingBar;
    private Button mBtnUpdate, mBtnAddComment, mBtnRate;
    RecyclerView RvComment;
    CommentAdapter commentAdapter;
    private MediaController mediaP;



    // Data reference
    private String message, key, eventid, userid, title, date, memo, photo,
            video, location, share, type, uId, uDisplayName, email, role;
    List<Comment> listComment, listRate;

    // firebase reference
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseUserRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);

        // Define UI reference
        RvComment = findViewById(R.id.rv_comment);
        mTitle_viewTxt = (TextView) findViewById(R.id.title_textView);
        mUser_viewTxt = (TextView) findViewById(R.id.user_viewTxt);
        mDate_viewTxt = (TextView) findViewById(R.id.date_viewTxt);
        mMemo_viewTxt = (TextView) findViewById(R.id.memo_txtView);
        mType_viewTxt = (TextView) findViewById(R.id.type_txtView);
        mVideo_viewTxt = (TextView) findViewById(R.id.Video_txtView);
        mLocation_viewTxt = (TextView) findViewById(R.id.Location_txtView);
        mComment = (EditText) findViewById(R.id.comment_editView);
        mPhoto_imgView = (ImageView) findViewById(R.id.photoDetail_imgView);
        mVideo_View = (VideoView) findViewById(R.id.videoDetail_view);
        mBtnUpdate = (Button) findViewById(R.id.btnUpdate);
        mBtnAddComment = (Button) findViewById(R.id.btnAddComment);
        ratingRatingBar = (RatingBar) findViewById(R.id.ratingBar4);
        mBtnRate = (Button) findViewById(R.id.button2);
        final TextView ratingDisplayTextView = (TextView) findViewById(R.id.textViewRate);

        // Define firebase reference
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        uId = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        uDisplayName = mUser.getDisplayName();
        mDatabaseUserRef = mDatabase.getReference("users").child(uId);

        mediaP = new MediaController(this);


        // <------------------------ Data ------------------------>
        // Get data from fragments (EventListFragment, EventSharedListFragment)
        key = getIntent().getStringExtra("key");
        eventid = getIntent().getStringExtra("eventid");
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
        mTitle_viewTxt.setText(title);
        mUser_viewTxt.setText(email);
        mDate_viewTxt.setText(date);
        mMemo_viewTxt.setText(memo);
        mType_viewTxt.setText(type);
        mLocation_viewTxt.setText(location);

        if ((photo != "NONE") && (video != "NONE")) {
            // If there is photo, get image from url
            Picasso.get().load(photo).into(mPhoto_imgView);
            //Picasso.get().load(video).fit().placeholder(R.mipmap.ic_launcher).into((Target) mVideo_View);



            // <------------------------ Button ------------------------>
            // "Rate" Button : Rate an event

            mBtnRate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBtnRate.setVisibility(View.VISIBLE);
                    DatabaseReference ratingRef = mDatabase.getReference("rate").child(eventid).push();
                    String rating_content = String.valueOf(ratingRatingBar.getRating());
                    String uid = mUser.getUid();
                    String uname = mUser.getDisplayName();

                    Rating rating = new Rating(rating_content, uid, uname);
                    ratingRef.setValue(rating).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(EventViewActivity.this, "Rating added",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EventViewActivity.this, "Unsuccessful",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });


                    ratingDisplayTextView.setText("Your rating is:" + ratingRatingBar.getRating());
                    String test = String.valueOf(ratingRatingBar.getRating());
                    Log.d("EventViewActivity", "rating bar value ssss : " + test);

                }
            });
        }


        // "Comment" Button : Put new comment
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
                        Toast.makeText(EventViewActivity.this, "Comment added",
                                Toast.LENGTH_SHORT).show();
                        mComment.setText("");
                        mBtnAddComment.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EventViewActivity.this, "Unsuccessful",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        iniRvComment();
        }


    // <------------------------ Function ------------------------>

        private void iniRvComment() {
            RvComment.setLayoutManager(new LinearLayoutManager(this));

            DatabaseReference commentRefence = mDatabase.getReference("comment").child(eventid);
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


        // <------------------------ Function ------------------------>

        private String timestampToString(long time) {
            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
            calendar.setTimeInMillis(time);
            String commentDate = DateFormat.getDateInstance().toString();
            return commentDate;

        }

        // <------------------------ Menu ------------------------>
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

                    mDatabaseUserRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            role = user.getRole();

                            // Check if the user has the right permission
                            if (uId.equals(userid) || role.equals("manager")) {
                                // put data to EventModActivity
                                Intent intent = new Intent(EventViewActivity.this, EventModActivity.class);
                                intent.putExtra("key",eventid);
                                intent.putExtra("userid",userid);
                                intent.putExtra("email",email);
                                intent.putExtra("title",mTitle_viewTxt.getText().toString());
                                intent.putExtra("type",mType_viewTxt.getText().toString());
                                intent.putExtra("date",mDate_viewTxt.getText().toString());
                                intent.putExtra("memo",mMemo_viewTxt.getText().toString());
                                intent.putExtra("photo",photo);
                                intent.putExtra("video",video);
                                intent.putExtra("location",mLocation_viewTxt.getText().toString());
                                intent.putExtra("share",share);
                                startActivity(intent);

                            } else {
                                Toast.makeText(EventViewActivity.this,"You do not have permission",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    return true;
            }
            return super.onOptionsItemSelected(item);
    }
}