package com.msa.timetracker;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference myRef;
    private FirebaseDatabase database;


    private View v;
    String timeTxt = "";
    private TextView name, email, time;
    private ImageView img;
    private long totalTime = 0;

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_profile, container, false);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();


        //elements
        img = v.findViewById(R.id.profile_img);
        name = v.findViewById(R.id.profile_name);
        email = v.findViewById(R.id.profile_mail);
        time = v.findViewById(R.id.profile_number);

        if (currentUser.getPhotoUrl() != null) {
            String url = currentUser.getPhotoUrl().toString();
            if (url.contains("s96-c")) {
                url = url.replace("s96-c", "s384-c");
            } else {
                url = url.concat("?type=large");
            }
            System.out.println("AVATAR URL: " + url);
            Picasso.get().load(Uri.parse(url)).into(img);
        }
        name.setText(currentUser.getDisplayName());
        email.setText(currentUser.getEmail());


        final Handler handler = new Handler();
        handler.postDelayed(() -> getTime(), 800);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn_profile:
//                Toast.makeText(getActivity(), "clicky", Toast.LENGTH_SHORT).show();
//                break;
        }
    }


    private void getTime() {
        // Read from the database
        DatabaseReference userRef = myRef.child(currentUser.getUid()); // get user's tasks
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot day : dataSnapshot.getChildren())
                    for (DataSnapshot task : day.getChildren()) {
                        long temp = Long.parseLong(task.getValue().toString());

                        System.out.println("totalTime+=" + task.getKey() + " " + temp);
                        totalTime += temp;
                    }

                int hours = (int) ((totalTime / (1000 * 60 * 60)) % 24);
                totalTime -= hours * (1000 * 60 * 60);
                int minutes = (int) ((totalTime / (1000 * 60)) % 60);
                totalTime -= minutes * (1000 * 60);
                int seconds = (int) (totalTime / 1000) % 60;

                timeTxt = String.format("%02d:%02d:%02d", hours, minutes, seconds);
//                Toast.makeText(getActivity(), "Time: " + timeTxt, Toast.LENGTH_SHORT).show();
                time.setText(timeTxt);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(getActivity(), "Error reading from DB", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
