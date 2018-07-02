package com.school.seksaria.bmv;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class HomeworkActivity extends AppCompatActivity implements DatePickerFragment.GetClass,
        HolidayAdapter.Populate {

    private FirebaseUser mFirebaseUser;
    private DatabaseReference mUserDatabaseReference;
    private User mUser;
    private DatabaseReference mHomeworkDatabaseReference;
    private ArrayList<String> tags = new ArrayList<>();
    private ArrayList<String> dates = new ArrayList<>();
    private int numberOfClass;
    private Handler handler = new Handler();
    private ConstraintLayout mainStuff, newStuff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainStuff = (ConstraintLayout) findViewById(R.id.main_stuff);
        newStuff = (ConstraintLayout) findViewById(R.id.new_stuff);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                newStuff.animate().translationX((float) 1.0 * newStuff.getWidth());
            }
        }, 500);

        handler.postDelayed((new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.new_subject_edit_text).setVisibility(View.VISIBLE);
                findViewById(R.id.new_homework_edit_text).setVisibility(View.VISIBLE);
                findViewById(R.id.new_homework_button).setVisibility(View.VISIBLE);
            }
        }), 1000);

        Bundle bundle = getIntent().getExtras();
        numberOfClass = bundle.getInt("class");

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserDatabaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(mFirebaseUser.getUid());
        mUserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.getValue(User.class);
                Date date = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());

                String formattedDate = dateFormat.format(date);

                Log.e("FormattedDate", formattedDate);

                mHomeworkDatabaseReference = FirebaseDatabase.getInstance()
                        .getReference()
                        .child("homework")
                        .child(""+numberOfClass)
                        .child(formattedDate);

                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("homework")
                        .child(""+numberOfClass)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
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

                            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                if (!(snapshot.getKey().equals(dateOne) ||
                                        snapshot.getKey().equals(dateTwo) ||
                                        snapshot.getKey().equals(dateThree) ||
                                        snapshot.getKey().equals(dateFour) ||
                                        snapshot.getKey().equals(dateFive))) {

                                    FirebaseDatabase.getInstance()
                                            .getReference()
                                            .child("homework")
                                            .child(""+numberOfClass)
                                            .child(snapshot.getKey()).removeValue();

                                }
                            }

                        } catch (Exception e) {
                            Log.v("Homework Activity", "No Homework found to delete");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });

                mHomeworkDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        collectHomeworks((Map<String, Object>) dataSnapshot.getValue());
                        ListView gridView = (ListView) findViewById(R.id.grid_view);

                        gridView.setAdapter(new HolidayAdapter(HomeworkActivity.this,
                                R.layout.holiday_card,
                                tags));

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                findViewById(R.id.progressBar).setVisibility(View.GONE);
                            }
                        }, 1000);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.homework_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_homework_button) {
            if (mUser.getWhat().equals("student")) {
                Toast.makeText(HomeworkActivity.this, "Access Denied", Toast.LENGTH_SHORT)
                        .show();
            } else {
                showDatePickerDialog();
            }
        }
        return true;
    }

    private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private void collectHomeworks(Map<String, Object> homeworks) {
        try {
            for (Map.Entry<String, Object> entry: homeworks.entrySet()) {
                Map singleHomework = (Map) entry.getValue();
                tags.add((String) singleHomework.get("tag"));
                dates.add((String) singleHomework.get("holidayDate"));
            }
        } catch (Exception e) {
            Toast.makeText(HomeworkActivity.this, "No Homework given yet.", Toast.LENGTH_SHORT).show();
        }
    }

    public int getNumberOfClass() {
        Bundle bundle = getIntent().getExtras();
        return bundle.getInt("class");
    }

    public String getAdapterWhat() {
        return mUser.getWhat();
    }

    public ArrayList<String> getAdapterTags() {
        return tags;
    }

    public ArrayList<String> getAdapterDates() {
        return dates;
    }
}
