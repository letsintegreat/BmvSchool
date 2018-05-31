package com.school.seksaria.bmv;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference mUserDatabaseReference;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CardView discussionCardView = (CardView) findViewById(R.id.discussion_card_view);
        CardView homeworkCarView = (CardView) findViewById(R.id.homework_card_view);
        CardView holidayCardView = (CardView) findViewById(R.id.holiday_card_view);
        CardView usersCardView = (CardView) findViewById(R.id.users_card_view);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mFirebaseAuth.getCurrentUser() == null) {
                    Intent intent = new Intent(MainActivity.this, AuthActivity.class);
                    startActivity(intent);
                }
            }
        };

        if (mFirebaseAuth.getCurrentUser() != null) {
            startService(new Intent(this, NotificationService.class));
            mUserDatabaseReference = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            mUserDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mUser = dataSnapshot.getValue(User.class);
                    findViewById(R.id.progress_bar).setVisibility(View.GONE);
                    findViewById(R.id.app_bar).setVisibility(View.VISIBLE);
                    findViewById(R.id.nested_scroll_view).setVisibility(View.VISIBLE);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }

        discussionCardView.setOnClickListener(this);
        homeworkCarView.setOnClickListener(this);
        holidayCardView.setOnClickListener(this);
        usersCardView.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        super.onStart();
    }

    @Override
    protected void onPause() {
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sign_out) {
            mFirebaseAuth.signOut();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {
            case R.id.discussion_card_view:

                if (mUser.getWhat().equals("student") || mUser.getWhat().equals("monitor")) {
                    intent = new Intent(this, DiscussionActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("class", mUser.getClassNumber());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                } else {
                    intent = new Intent(this, ClassTakerActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("redirectTo", "discussion");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                }

            case R.id.homework_card_view:

                if (mUser.getWhat().equals("student") || mUser.getWhat().equals("monitor")) {
                    intent = new Intent(this, HomeworkActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("class", mUser.getClassNumber());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                } else {
                    intent = new Intent(this, ClassTakerActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("redirectTo", "homework");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                }

            case R.id.holiday_card_view:

                intent = new Intent(this, HolidayActivity.class);
                startActivity(intent);
                break;

            case R.id.users_card_view:

                intent = new Intent(this, UserActivity.class);
                startActivity(intent);
                break;

            default: break;
        }

    }


}
