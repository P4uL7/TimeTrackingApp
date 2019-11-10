package com.msa.timetracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    static FirebaseAuth mAuth;
    static FirebaseUser currentUser;
    static DatabaseReference myRef;
    FirebaseDatabase database;
    DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //drawer stuff
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_main,
                    new MainFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_main);
        }
        // end drawer stuff

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        System.out.println("Current user: " + currentUser);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    private void showAlertDialog(String message, String negative, String positive, String tag) {
        FragmentManager fm = getSupportFragmentManager();
        PopupDialogFragment alertDialog = new PopupDialogFragment(message, getApplicationContext(), negative, positive);
        alertDialog.show(fm, tag);
    }


    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void exitApp() {
        new AlertDialog.Builder(this)
                .setTitle("Exit App")
                .setMessage("Are you sure you wish to exit?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                    homeIntent.addCategory(Intent.CATEGORY_HOME);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homeIntent);
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_main,
                        new ProfileFragment()).commit();
                break;
            case R.id.nav_day:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_main,
                        new DayFragment()).commit();
                break;
            case R.id.nav_main:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_main,
                        new MainFragment()).commit();
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                launchLoginActivity();
                break;
            case R.id.nav_exitApp:
                exitApp();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
