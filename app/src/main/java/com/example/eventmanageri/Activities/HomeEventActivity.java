package com.example.eventmanageri.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.eventmanageri.Fragments.EventListFragment;
import com.example.eventmanageri.Fragments.EventSharedListFragment;
import com.example.eventmanageri.Fragments.ProfileFragment;
import com.example.eventmanageri.Fragments.SearchUserFragment;
import com.example.eventmanageri.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeEventActivity extends AppCompatActivity {
    // UI reference
    BottomNavigationView mBottomNavigationView;
    Fragment mFragment = null;

    // DB reference
    private FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_event);
        // Define UI reference
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        // Define DB reference
        mUser = FirebaseAuth.getInstance().getCurrentUser();


        // Bottom Navigation
        mBottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new EventListFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    // Define bottom menu options
                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            // Go to HomeBarActivity
                            mFragment = null;
                            startActivity(new Intent(HomeEventActivity.this, HomeBarActivity.class));
                            break;
                        case R.id.nav_search:
                            // Go to SearchUserFragment
                            mFragment = new SearchUserFragment();
                            break;
                        case R.id.nav_add:
                            // Go to NewEventActivity
                            mFragment = null;
                            startActivity(new Intent(HomeEventActivity.this, NewEventActivity.class));
                            break;
                        case R.id.nav_shared:
                            // Go to EventListFragment
                            mFragment = new EventSharedListFragment();
                            break;
                        case R.id.nav_profile:
                            // Go to ProfileFragment
                            SharedPreferences.Editor editor = getSharedPreferences("PREFS",MODE_PRIVATE).edit();
                            editor.putString("profileid", mUser.getUid());
                            editor.apply();
                            mFragment = new ProfileFragment();
                            break;
                    }
                    if (mFragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, mFragment).commit();
                    }
                    return true;
                }
            };
}
