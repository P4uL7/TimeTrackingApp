package com.msa.timetracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;


@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        //drawer stuff
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        //

        View headerView = navigationView.getHeaderView(0);
        CircularImageView navImage = headerView.findViewById(R.id.left_menu_image);
        if (currentUser.getPhotoUrl() != null) {
            String url = currentUser.getPhotoUrl().toString();
            if (url.contains("s96-c")) {
                url = url.replace("s96-c", "s384-c");
            } else {
                url = url.concat("?type=large");
            }
            Picasso.get().load(Uri.parse(url)).into(navImage);
        }
        TextView navUsername = headerView.findViewById(R.id.left_menu_username);
        navUsername.setText(currentUser.getDisplayName());
        TextView navEmail = headerView.findViewById(R.id.left_menu_email);
        navEmail.setText(currentUser.getEmail());
        //
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
    }


    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container_main);
        if (!(currentFragment instanceof MainFragment)) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            transaction.replace(R.id.fragment_container_main, new MainFragment()).commit();
        }
    }

    private void showAlertDialog(String message, String negative, String positive, String tag) {
        FragmentManager fm = getSupportFragmentManager();
        PopupDialogFragment alertDialog = new PopupDialogFragment(message, getApplicationContext(), negative, positive);
        alertDialog.show(fm, tag);
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
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container_main);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        switch (menuItem.getItemId()) {
            case R.id.nav_main:
                if (!(currentFragment instanceof MainFragment)) {
                    transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                    transaction.replace(R.id.fragment_container_main,
                            new MainFragment()).commit();
                }
                break;
            case R.id.nav_day:
                if (!(currentFragment instanceof DayFragment))
                    transaction.replace(R.id.fragment_container_main,
                            new DayFragment()).commit();
                break;
            case R.id.nav_overview:
                if (!(currentFragment instanceof OverviewFragment))
                    transaction.replace(R.id.fragment_container_main,
                            new OverviewFragment()).commit();
                break;
            case R.id.nav_profile:
                if (!(currentFragment instanceof ProfileFragment))
                    transaction.replace(R.id.fragment_container_main,
                            new ProfileFragment()).commit();
                break;
            case R.id.nav_merge:
                if (!(currentFragment instanceof MergeAccountsFragment))
                    transaction.replace(R.id.fragment_container_main,
                            new MergeAccountsFragment()).commit();
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                launchLoginActivity();
                break;
            case R.id.nav_exitApp:
                exitApp();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
