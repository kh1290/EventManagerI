package com.example.eventmanageri.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventmanageri.Adapters.UserAdapter;
import com.example.eventmanageri.Models.User;
import com.example.eventmanageri.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;


public class SearchUserFragment extends Fragment {

    // UI reference
    private RecyclerView mRecyclerView;
    MaterialSearchView materialSearchView;

    // Data reference
    private String uDisplayName;
    private List<User> users;

    // Adapter reference
    public UserAdapter mUserAdapter;

    // DB reference
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser mUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_user, container, false);

        // Define UI reference
        materialSearchView = (MaterialSearchView) view.findViewById(R.id.searchView);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_users);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Data reference
        users = new ArrayList<>();

        // Define DB reference
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference("users");
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        uDisplayName = mUser.getDisplayName();

        // Define UserAdapter reference
        mUserAdapter = new UserAdapter(users, getContext(),true);
        mRecyclerView.setAdapter(mUserAdapter);

        // List users
        loadUser();

        return view;
    }

    // <------------------------ List ------------------------>
    // List users
    public void loadUser() {
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                List<String> keys = new ArrayList<>();

                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    User user = keyNode.getValue(User.class);

                    users.add(user);
                }

                mUserAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
