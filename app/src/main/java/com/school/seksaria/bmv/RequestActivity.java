package com.school.seksaria.bmv;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestActivity extends AppCompatActivity {

    ArrayList<User> users = new ArrayList<>();
    FirebaseUser mFirebaseUser;
    DatabaseReference mRequestDatabaseReference, mUserDatabaseReference;
    User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final RequestAdapter requestAdapter = new RequestAdapter(this, R.layout.item_request, users);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(requestAdapter);

        mUserDatabaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(mFirebaseUser.getUid());

        mUserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.getValue(User.class);
                if (mUser.getWhat().equals("principal")) {
                    mRequestDatabaseReference = FirebaseDatabase.getInstance()
                            .getReference()
                            .child("responses")
                            .child("principal");
                } else {
                    mRequestDatabaseReference = FirebaseDatabase.getInstance()
                            .getReference()
                            .child("responses")
                            .child("teacher");
                }

                mRequestDatabaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        User currentUser = dataSnapshot.getValue(User.class);
                        requestAdapter.add(currentUser);
                    }

                    @Override public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
                    @Override public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
                    @Override public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
                    @Override public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
            }

            @Override public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

    }
}
