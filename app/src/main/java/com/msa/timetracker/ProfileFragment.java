package com.msa.timetracker;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.Objects;

@SuppressWarnings({"FieldCanBeLocal", "NullableProblems"})
public class ProfileFragment extends Fragment implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference myRef;
    private FirebaseDatabase database;


    private View v;
    private String timeTxt = "";
    private TextView name, email, time;
    private CircularImageView circularImageView;
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
        circularImageView = v.findViewById(R.id.profile_img);
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
            Picasso.get().load(Uri.parse(url)).into(circularImageView);
        }
        name.setText(currentUser.getDisplayName());
        email.setText(currentUser.getEmail());


        final Handler handler = new Handler();
        handler.postDelayed(() -> getTime(), 800);


//        BarChart chart = v.findViewById(R.id.barchart);
//        ArrayList<BarEntry> NoOfEmp = new ArrayList<>();
//
//        NoOfEmp.add(new BarEntry(945f, 0));
//        NoOfEmp.add(new BarEntry(1040f, 1));
//        NoOfEmp.add(new BarEntry(1133f, 2));
//        NoOfEmp.add(new BarEntry(1240f, 3));
//        NoOfEmp.add(new BarEntry(1369f, 4));
//        NoOfEmp.add(new BarEntry(1487f, 5));
//        NoOfEmp.add(new BarEntry(1501f, 6));
//        NoOfEmp.add(new BarEntry(1645f, 7));
//        NoOfEmp.add(new BarEntry(1578f, 8));
//        NoOfEmp.add(new BarEntry(1695f, 9));
//
//        BarDataSet bardataset = new BarDataSet(NoOfEmp, "No Of Employee");
//        chart.animateY(5000);
//        BarData data = new BarData(bardataset);
//        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
//        chart.setData(data);

        return v;
    }

    @Override
    public void onClick(View v) {
    }


    private void getTime() {
        // Read from the database
        DatabaseReference userRef = myRef.child(currentUser.getUid()); // get user's tasks
        userRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot day : dataSnapshot.getChildren())
                    for (DataSnapshot task : day.getChildren()) {
                        long temp = Long.parseLong(Objects.requireNonNull(task.getValue()).toString());

                        System.out.println("totalTime+=" + task.getKey() + " " + temp);
                        totalTime += temp;
                    }

                int hours = (int) ((totalTime / (1000 * 60 * 60)) % 24);
                totalTime -= hours * (1000 * 60 * 60);
                int minutes = (int) ((totalTime / (1000 * 60)) % 60);
                totalTime -= minutes * (1000 * 60);
                int seconds = (int) (totalTime / 1000) % 60;

                timeTxt = String.format("%02d:%02d:%02d", hours, minutes, seconds);
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
