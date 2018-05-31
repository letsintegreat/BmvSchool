package com.school.seksaria.bmv;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class HolidayActivity extends AppCompatActivity {

    private FirebaseUser mFirebaseUser;
    private User mUser;
    private DatabaseReference mUserDatabaseReference;
    private DatabaseReference mHolidayDatabaseReference;
    private ArrayList<String> tags = new ArrayList<>();
    private ArrayList<String> dates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mUserDatabaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(mFirebaseUser.getUid());

        mUserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.getValue(User.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        mHolidayDatabaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child("holiday");

        mHolidayDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
                Calendar calendar = Calendar.getInstance();
                Date date = calendar.getTime();

                String dateOne = simpleDateFormat.format(date);

                calendar.add(Calendar.DATE, 1);
                date = calendar.getTime();
                String dateTwo = simpleDateFormat.format(date);

                calendar.add(Calendar.DATE, 1);
                date = calendar.getTime();
                String dateThree = simpleDateFormat.format(date);

                calendar.add(Calendar.DATE, 1);
                date = calendar.getTime();
                String dateFour = simpleDateFormat.format(date);

                calendar.add(Calendar.DATE, 1);
                date = calendar.getTime();
                String dateFive = simpleDateFormat.format(date);

                Holiday holiday = dataSnapshot.getValue(Holiday.class);

                if (!(holiday.getHolidayDate().equals(dateOne) ||
                        holiday.getHolidayDate().equals(dateTwo) ||
                        holiday.getHolidayDate().equals(dateThree) ||
                        holiday.getHolidayDate().equals(dateFour) ||
                        holiday.getHolidayDate().equals(dateFive))) {

                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("holiday")
                            .child(dataSnapshot.getKey()).removeValue();

                }

            }
            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        });

        mHolidayDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                collectHoliday((Map<String, Object>) dataSnapshot.getValue());
                GridView gridView = (GridView) findViewById(R.id.grid_view);
                gridView.setAdapter(new HolidayAdapter(HolidayActivity.this, tags, dates, "holiday"));
                findViewById(R.id.progressBar).setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }

    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.holiday_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_holiday_button) {
            if (mUser.getWhat().equals("principal") || mUser.getWhat().equals("teacher")) {
                showDatePickerDialog();
            } else {
                Toast.makeText(HolidayActivity.this, "Access Denied", Toast.LENGTH_SHORT)
                        .show();
            }
        }

        return true;
    }

    private void collectHoliday(Map<String, Object> holidays) {
        try {
            for (Map.Entry<String, Object> entry: holidays.entrySet()) {
                Map singleHoliday = (Map) entry.getValue();
                tags.add((String) singleHoliday.get("tag"));
                dates.add((String) singleHoliday.get("holidayDate"));
            }
        } catch (Exception e) {
            Toast.makeText(this, "No Holidays found so far!", Toast.LENGTH_SHORT).show();
        }
    }
}
