package com.school.seksaria.bmv;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class UserActivity extends AppCompatActivity {

    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<Long> classes = new ArrayList<>();
    private ArrayList<String> whats = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference()
                .child("users");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        collectUsers((Map<String, Object>) dataSnapshot.getValue());
                        GridView gridView = (GridView) findViewById(R.id.grid_view);
                        gridView.setAdapter(new UserAdapter(UserActivity.this, names, classes, whats));
                        findViewById(R.id.progressBar).setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                }
        );


    }

    private void collectUsers(Map<String, Object> usersMap) {
        for (Map.Entry<String, Object> entry: usersMap.entrySet()) {
            Map singleUser = (Map) entry.getValue();
            names.add((String) singleUser.get("fullName"));
            classes.add((Long) singleUser.get("classNumber"));
            whats.add((String) singleUser.get("what"));
        }
    }
}
