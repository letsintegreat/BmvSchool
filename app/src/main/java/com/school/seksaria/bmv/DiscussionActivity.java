package com.school.seksaria.bmv;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DiscussionActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private User mUser;
    private DatabaseReference mUserDatabaseReference;
    private DatabaseReference mChatDatabaseReference;
    private int numberOfClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        Bundle bundle = getIntent().getExtras();
        numberOfClass = bundle.getInt("class");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.list_view).setVisibility(View.GONE);
        findViewById(R.id.message_editor).setVisibility(View.GONE);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();


        ListView listView = (ListView) findViewById(R.id.list_view);
        List<UserMessage> messages = new ArrayList<>();
        final MessageAdapter messageAdapter = new MessageAdapter(this, R.layout.item_message, messages);
        listView.setAdapter(messageAdapter);

        mUserDatabaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(mFirebaseUser.getUid());

        mUserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.getValue(User.class);
                mChatDatabaseReference = FirebaseDatabase.getInstance()
                        .getReference()
                        .child("discussion")
                        .child("" + numberOfClass);

                getSupportActionBar().setTitle("Class " + numberOfClass + " discussion");

                findViewById(R.id.list_view).setVisibility(View.VISIBLE);
                findViewById(R.id.message_editor).setVisibility(View.VISIBLE);
                findViewById(R.id.progressBar).setVisibility(View.GONE);

                mChatDatabaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        UserMessage message = dataSnapshot.getValue(UserMessage.class);
                        messageAdapter.add(message);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {}

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        final EditText messageEditText = (EditText) findViewById(R.id.message_edit_text);

        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    findViewById(R.id.sendButton).setEnabled(true);
                } else {
                    findViewById(R.id.sendButton).setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserMessage newMessage = new UserMessage(mUser.getFullName(), messageEditText.getText().toString());
                mChatDatabaseReference.push().setValue(newMessage);
                messageEditText.setText("");
            }
        });

    }
}
