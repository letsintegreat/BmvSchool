package com.school.seksaria.bmv;

import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AuthActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private boolean accountCreated = false;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUserDatabaseReference, mResponseDatabaseReference, mUserResponseDatabaseReference;
    private DatabaseReference mCheckRequestDatabaseReference;
    private ValueEventListener valueEventListener;
    private Button button;

    ConstraintLayout formPage, createAccountPage, startPage, splashPage, logPage, waitingPage;
    Handler handler;
    String unique;
    boolean errorOccurred = false;
    private EditText mEmailEditText, mClassEditText, mFullNameEditText, mPasswordEditText,
        mpasswordEditText2;
    User userState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        mEmailEditText = (EditText) findViewById(R.id.email_edit_text);
        mClassEditText = (EditText) findViewById(R.id.class_edit_text);
        mFullNameEditText = (EditText) findViewById(R.id.name_edit_text);
        mPasswordEditText = (EditText) findViewById(R.id.password_edit_text);
        mpasswordEditText2 = (EditText) findViewById(R.id.cpassword_edit_text);
        formPage = (ConstraintLayout) findViewById(R.id.form_page);
        startPage = (ConstraintLayout) findViewById(R.id.start_page);
        splashPage = (ConstraintLayout) findViewById(R.id.splash_screen);
        logPage = (ConstraintLayout) findViewById(R.id.log_in_page);
        waitingPage = (ConstraintLayout) findViewById(R.id.waiting_page);
        createAccountPage = (ConstraintLayout) findViewById(R.id.create_account_page);

        handler = new Handler();

        unique = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        mUserResponseDatabaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child("request");

        mUserResponseDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        formPage.animate().translationX((float) 1.0 * formPage.getWidth());
                        createAccountPage.animate().translationX((float) 1.0 * createAccountPage.getWidth());
                        startPage.animate().translationX((float) -1.0 * startPage.getWidth());
                        logPage.animate().translationX((float) -1.0 * logPage.getWidth());
                        waitingPage.animate().translationX((float) -1.0 * waitingPage.getWidth());
                    }
                }, 500);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        splashPage.animate().translationY(splashPage.getHeight()).setDuration(150);
                    }
                }, 1000);

                if (dataSnapshot.hasChild(unique)) {
                    accountCreated = true;
                }

                if (accountCreated) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getSupportActionBar().show();
                            waitingPage.animate().translationX(0);
                        }
                    }, 2000);
                    initializeRequest();
                } else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getSupportActionBar().show();
                            startPage.animate().translationX(0);
                        }
                    }, 2000);
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        Button signUpButton = (Button) findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportActionBar().setTitle("Sign-Up");

                startPage.animate().translationX((float) -1.0 * startPage.getWidth());
                formPage.animate().translationX((float) 0);

                findViewById(R.id.final_sign_in_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        boolean errorOccurred = false;

                        if (mFullNameEditText.getText().toString().equals("")) {
                            mFullNameEditText.setError("Invalid Name");
                            errorOccurred = true;
                        }

                        if (mClassEditText.getText().toString().equals("")) {
                            mClassEditText.setError("Invalid Class");
                            errorOccurred = true;
                        }

                        if (!errorOccurred) {
                            formPage.animate().translationX((float) -1.0 * formPage.getWidth());
                            mResponseDatabaseReference = FirebaseDatabase.getInstance()
                                    .getReference()
                                    .child("request")
                                    .child(unique);

                            String isWhat;
                            RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_button);
                            if (radioGroup.getCheckedRadioButtonId() == R.id.student_radio_button)
                                isWhat = "student";
                            else if (radioGroup.getCheckedRadioButtonId() == R.id.monitor_radio_button)
                                isWhat = "monitor";
                            else if (radioGroup.getCheckedRadioButtonId() == R.id.teacher_radio_button)
                                isWhat = "teacher";
                            else
                                isWhat = "principal";

                            mResponseDatabaseReference.setValue(new User(
                                    isWhat,
                                    mFullNameEditText.getText().toString(),
                                    Integer.parseInt(mClassEditText.getText().toString())
                            ));

                            waitingPage.animate().translationX(0);
                            initializeRequest();
                        }
                    }
                });
            }
        });

        Button logInButton = (Button) findViewById(R.id.log_in_button);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportActionBar().setTitle("Log-In");

                startPage.animate().translationX((float) 1.0 * startPage.getWidth());
                logPage.animate().translationX(0);

                final EditText emailEditText = (EditText) findViewById(R.id.log_email_edit_text);
                final EditText passwordEditText = (EditText) findViewById(R.id.log_password_edit_text);

                findViewById(R.id.log_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAuth.signInWithEmailAndPassword(
                                emailEditText.getText().toString(),
                                passwordEditText.getText().toString()
                        )
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {

                                            Log.w("Authentication:failed", task.getException());
                                            Toast.makeText(
                                                    AuthActivity.this,
                                                    "Authentication Failed",
                                                    Toast.LENGTH_SHORT
                                            ).show();
                                        } else {
                                            updateUI();
                                        }
                                    }
                                });
                    }
                });
            }
        });

    }

    private void updateUI() {
        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void initializeRequest() {

        button = (Button) findViewById(R.id.rejected_button);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userState = dataSnapshot.getValue(User.class);
                if (userState.getValidity() == -1) {
                    TextView textView = (TextView) findViewById(R.id.wait_head);
                    textView.setText(userState.getRejectedMessage());
                    textView = (TextView) findViewById(R.id.rejected_by);
                    textView.setVisibility(View.VISIBLE);
                    String text = "Your Account Request is rejected by: " + userState.getValidatedBy();
                    textView.setText(text);
                    button.setVisibility(View.VISIBLE);
                } else if (userState.getValidity() == 1) {
                    waitingPage.animate().translationX((float) -1.0 * waitingPage.getWidth());
                    createAccountPage.animate().translationX(0);

                    TextView textView = (TextView) findViewById(R.id.approved_by);
                    String text = "Your Account request is accepted by " + userState.getValidatedBy();
                    textView.setText(text);

                }
            }
            @Override public void onCancelled(@NonNull DatabaseError databaseError) {}
        };

        mCheckRequestDatabaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child("request")
                .child(unique);

        mCheckRequestDatabaseReference.addValueEventListener(valueEventListener);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AuthActivity.this, MainActivity.class));
                mCheckRequestDatabaseReference.removeEventListener(valueEventListener);
                mCheckRequestDatabaseReference.removeValue();
                button.setVisibility(View.GONE);
            }
        });

        findViewById(R.id.create_sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                String cpassword = mpasswordEditText2.getText().toString();
                errorOccurred = false;
                if (!email.contains("@bmv.com")) {
                    mEmailEditText.setError("Must contain \"@bmv.com\"");
                    errorOccurred = true;
                }

                if (password.equals("")) {
                    mPasswordEditText.setError("Invalid Password");
                    errorOccurred = true;
                }

                if (!password.equals(cpassword)) {
                    mpasswordEditText2.setError("Both the passwords should match");
                    errorOccurred = true;
                }

                if (!errorOccurred) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        mUser = FirebaseAuth.getInstance().getCurrentUser();
                                        mUserDatabaseReference = FirebaseDatabase
                                                .getInstance()
                                                .getReference()
                                                .child("users")
                                                .child(mUser.getUid());
                                        mUserDatabaseReference.setValue(userState);
                                        updateUI();
                                        mCheckRequestDatabaseReference.removeEventListener(valueEventListener);
                                    } else {
                                        Log.w("Authentication:failed", task.getException());
                                        Toast.makeText(AuthActivity.this,
                                                "Authentication Failed",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });

    }

}
