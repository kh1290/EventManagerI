package com.example.eventmanageri.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmanageri.Activities.HomeEventActivity;
import com.example.eventmanageri.Activities.MainActivity;
import com.example.eventmanageri.Fragments.ProfileFragment;
import com.example.eventmanageri.Models.User;
import com.example.eventmanageri.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserItemView> {
    // Data reference
    private Context mContext;
    private List<User> mUserList;
    private boolean isFragment;
    private String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    // DB reference
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;

    public UserAdapter(List<User> mUserList, Context mContext, boolean isFragment) {
        this.mUserList = mUserList;
        this.mContext = mContext;
        this.isFragment = isFragment;
    }

    class UserItemView extends RecyclerView.ViewHolder{
        private TextView mUname, mEmail;
        private String mUserId, mBio, mRole;
        private Button btnFollow;


        public UserItemView(final ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false));
            mUname = (TextView) itemView.findViewById(R.id.uname_textView);
            mEmail = (TextView) itemView.findViewById(R.id.email_txtView);
            btnFollow = (Button) itemView.findViewById(R.id.btnFollow);

            // View User Detail
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Put Data to ProfileFragment
                    Intent intent = new Intent(parent.getContext(), ProfileFragment.class);
                    intent.putExtra("userid",mUserId);
                    intent.putExtra("title",mUname.getText().toString());
                    intent.putExtra("type",mEmail.getText().toString());
                    parent.getContext().startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public UserAdapter.UserItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserAdapter.UserItemView(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserAdapter.UserItemView holder, int position) {
        final User user = mUserList.get(position);

        // Define user items (for ProfileFragment)
        holder.btnFollow.setVisibility(View.VISIBLE);
        holder.mUname.setText(user.getUname());
        holder.mEmail.setText(user.getEmail());
        holder.mBio = user.getBio();
        holder.mRole = user.getRole();

        // Button : check if "follow" or "following"
        isFollowing(user.getKey(), holder.btnFollow);

        // If UID = currentUser, button is gone
        if (user.getKey().equals(currentUser)) {
            holder.btnFollow.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFragment) {
                    // If it is called from ProfileFragment
                    // Show user's profile
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",
                            Context.MODE_PRIVATE).edit();
                    editor.putString("profileid", user.getKey());
                    editor.apply();

                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, new ProfileFragment()).commit();
                } else {
                    // if it is called from FollowerActivity
                    // Do nothing
                }
            }
        });

        holder.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase = FirebaseDatabase.getInstance();
                mDatabaseRef = mDatabase.getReference("follow");

                if (holder.btnFollow.getText().toString().equals("follow")) {
                    mDatabaseRef.child(currentUser)
                            .child("following").child(user.getKey()).setValue(true);
                    mDatabaseRef.child(user.getKey())
                            .child("followers").child(currentUser).setValue(true);
                } else {
                    mDatabaseRef.child(currentUser)
                            .child("following").child(user.getKey()).removeValue();
                    mDatabaseRef.child(user.getKey())
                            .child("followers").child(currentUser).removeValue();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    // Button : check if "follow" or "following"
    private void isFollowing(final String userid, final Button button) {
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("follow")
                .child(mUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userid).exists()) {
                    button.setText("following");
                } else {
                    button.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}