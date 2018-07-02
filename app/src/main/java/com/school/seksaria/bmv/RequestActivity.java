package com.school.seksaria.bmv;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class RequestActivity extends AppCompatActivity {

    ArrayList<User> users = new ArrayList<>();
    FirebaseUser mFirebaseUser;
    DatabaseReference mRequestDatabaseReference, mUserDatabaseReference;
    User mUser;
    ListView listView;
    User pending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mRequestDatabaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child("request");

        mUserDatabaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(mFirebaseUser.getUid());

        mUserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.getValue(User.class);

                mRequestDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        collectRequests((Map<String, Object>) dataSnapshot.getValue());
                        RequestAdapter requestAdapter = new RequestAdapter(RequestActivity.this,
                                R.layout.item_request, users);

                        listView = (ListView) findViewById(R.id.list_view);
                        listView.setAdapter(requestAdapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                pending = users.get(position);
                                if (position % 2 == 0) {
                                    pending.setValidatedBy(mUser.getFullName());
                                    DatabaseReference reference = FirebaseDatabase.getInstance()
                                            .getReference();

                                    Query query = reference.child("request").orderByChild("token")
                                            .equalTo(pending.getToken());
                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            FirebaseDatabase.getInstance()
                                                    .getReference()
                                                    .child("request")
                                                    .child(dataSnapshot.getKey())
                                                    .setValue(pending);
                                        }
                                        @Override public void onCancelled(@NonNull DatabaseError databaseError) {}
                                    });
                                }
                            }
                        });
                    }

                    @Override public void onCancelled(@NonNull DatabaseError databaseError) {}
                });

            }

            @Override public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

    }

    private void collectRequests(Map<String, Object> requests) {
        try {
            for (Map.Entry<String, Object> entry: requests.entrySet()) {
                Map request = (Map) entry.getValue();
                if (Integer.parseInt((String) request.get("validity")) == 0) {

                    if (mUser.getWhat().equals("teacher")) {
                        if (((String)request.get("what")).equals("student")) {
                            users.add(new User(
                                    (String) request.get("what"),
                                    (String) request.get("fullName"),
                                    (int) request.get("classNumber"),
                                    (String) request.get("token")
                            ));

                            users.add(new User(
                                    (String) request.get("what"),
                                    (String) request.get("fullName"),
                                    (int) request.get("classNumber"),
                                    (String) request.get("token")
                            ));
                        }
                    } else if (mUser.getWhat().equals("principal")) {
                        if (!((String) request.get("what")).equals("principal")) {
                            users.add(new User(
                                    (String) request.get("what"),
                                    (String) request.get("fullName"),
                                    (int) request.get("classNumber"),
                                    (String) request.get("token")
                            ));

                            users.add(new User(
                                    (String) request.get("what"),
                                    (String) request.get("fullName"),
                                    (int) request.get("classNumber"),
                                    (String) request.get("token")
                            ));
                        }
                    } else {
                        users.add(new User(
                                (String) request.get("what"),
                                (String) request.get("fullName"),
                                (int) request.get("classNumber"),
                                (String) request.get("token")
                        ));

                        users.add(new User(
                                (String) request.get("what"),
                                (String) request.get("fullName"),
                                (int) request.get("classNumber"),
                                (String) request.get("token")
                        ));
                    }
                }
            }
        } catch (Exception e) {
            Log.e("RequestActivity", e.toString());
            Toast.makeText(RequestActivity.this, "No Account Request found", Toast.LENGTH_SHORT).show();
        }
    }

}
