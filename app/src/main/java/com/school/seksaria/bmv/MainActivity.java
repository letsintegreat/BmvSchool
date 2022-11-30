package com.school.seksaria.bmv;

import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    String unique;
    ConstraintLayout splashPage;
    CardView discussionCardView, homeworkCarView, holidayCardView, usersCardView, requestCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        unique = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        splashPage  = (ConstraintLayout) findViewById(R.id.splash_screen);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                discussionCardView.animate().translationX((float) -2.0 * discussionCardView.getWidth());
                homeworkCarView.animate().translationX((float) 2.0 * discussionCardView.getWidth());
                holidayCardView.animate().translationX((float) -2.0 * discussionCardView.getWidth());
                usersCardView.animate().translationX((float) 2.0 * discussionCardView.getWidth());
            }
        }, 1000);

        discussionCardView = (CardView) findViewById(R.id.discussion_card_view);
        homeworkCarView = (CardView) findViewById(R.id.homework_card_view);
        holidayCardView = (CardView) findViewById(R.id.holiday_card_view);
        usersCardView = (CardView) findViewById(R.id.users_card_view);
        requestCardView = (CardView) findViewById(R.id.request_card_view);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mFirebaseAuth.getCurrentUser() == null) {
                    Intent intent = new Intent(MainActivity.this, AuthActivity.class);
                    startActivity(intent);
                } else {
                    try {
                        FirebaseDatabase.getInstance()
                                .getReference()
                                .child("request")
                                .child(unique)
                                .removeValue();
                    } catch (Exception e) {}
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
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            splashPage.animate().alpha(0);
                        }
                    }, 1500);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            findViewById(R.id.app_bar).setVisibility(View.VISIBLE);
                            discussionCardView.animate().translationX(0);
                            homeworkCarView.animate().translationX(0);
                            holidayCardView.animate().translationX(0);
                            usersCardView.animate().translationX(0);
                            requestCardView.setVisibility(View.VISIBLE);
                        }
                    }, 2500);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }

        discussionCardView.setOnClickListener(this);
        homeworkCarView.setOnClickListener(this);
        holidayCardView.setOnClickListener(this);
        usersCardView.setOnClickListener(this);
        requestCardView.setOnClickListener(this);

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
            stopService(new Intent(MainActivity.this, NotificationService.class));
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

            case R.id.request_card_view:

                if (mUser.getWhat().equals("student") || mUser.getWhat().equals("monitor")) {
                    Toast.makeText(MainActivity.this, "Access Denied", Toast.LENGTH_SHORT).show();
                } else if (mUser.getWhat().equals("teacher")) {
                    intent = new Intent(this, RequestActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("what", "teacher");
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    intent = new Intent(this, RequestActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("what", "principal");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;

            default: break;
        }

    }


}
