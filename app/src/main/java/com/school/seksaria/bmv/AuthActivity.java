package com.school.seksaria.bmv;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class AuthActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUserDatabaseReference;
    private DatabaseReference mResponseDatabaseReference;

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mFullNameEditText;
    private EditText mClassEditText;
    boolean pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        findViewById(R.id.email_edit_text).setVisibility(View.GONE);
        findViewById(R.id.password_edit_text).setVisibility(View.GONE);
        findViewById(R.id.class_edit_text).setVisibility(View.GONE);
        findViewById(R.id.name_edit_text).setVisibility(View.GONE);
        findViewById(R.id.radio_button).setVisibility(View.GONE);
        findViewById(R.id.next_sign_in_button).setVisibility(View.GONE);
        findViewById(R.id.email_edit_text).animate().translationY(findViewById(R.id.email_edit_text).getHeight()).alpha(0.0f);
        findViewById(R.id.password_edit_text).animate().translationY(findViewById(R.id.password_edit_text).getHeight()).alpha(0.0f);
        findViewById(R.id.class_edit_text).animate().translationY(findViewById(R.id.class_edit_text).getHeight()).alpha(0.0f);
        findViewById(R.id.name_edit_text).animate().translationY(findViewById(R.id.name_edit_text).getHeight()).alpha(0.0f);
        findViewById(R.id.radio_button).animate().translationY(findViewById(R.id.radio_button).getHeight()).alpha(0.0f);
        findViewById(R.id.next_sign_in_button).animate().translationY(findViewById(R.id.next_sign_in_button).getHeight()).alpha(0.0f);


        Button signUpButton = (Button) findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getSupportActionBar().setTitle("Sign-Up");

                findViewById(R.id.auth_root_view).setBackgroundColor(getResources().getColor(R.color.colorBackground));
                findViewById(R.id.heading_text_view).animate().translationY(findViewById(R.id.heading_text_view).getHeight()).alpha(0.0f);
                findViewById(R.id.log_in_button).animate().translationY(findViewById(R.id.log_in_button).getHeight()).alpha(0.0f);
                findViewById(R.id.sign_up_button).animate().translationY(findViewById(R.id.sign_up_button).getHeight()).alpha(0.0f);
                findViewById(R.id.textView).animate().translationY(findViewById(R.id.textView).getHeight()).alpha(0.0f);
                findViewById(R.id.email_edit_text).setVisibility(View.VISIBLE);
                findViewById(R.id.password_edit_text).setVisibility(View.VISIBLE);
                findViewById(R.id.class_edit_text).setVisibility(View.VISIBLE);
                findViewById(R.id.name_edit_text).setVisibility(View.VISIBLE);
                findViewById(R.id.next_sign_in_button).setVisibility(View.VISIBLE);
                findViewById(R.id.email_edit_text).animate().alpha(1.0f);
                findViewById(R.id.email_edit_text).animate().alpha(1.0f);
                findViewById(R.id.password_edit_text).animate().alpha(1.0f);
                findViewById(R.id.class_edit_text).animate().alpha(1.0f);
                findViewById(R.id.name_edit_text).animate().alpha(1.0f);
                findViewById(R.id.radio_button).animate().alpha(1.0f);
                findViewById(R.id.next_sign_in_button).animate().alpha(1.0f);

                findViewById(R.id.email_edit_text).animate().rotation(360).setDuration(1000);
                findViewById(R.id.email_edit_text).animate().rotation(360).setDuration(1000);
                findViewById(R.id.password_edit_text).animate().rotation(360).setDuration(1000);
                findViewById(R.id.class_edit_text).animate().rotation(360).setDuration(1000);
                findViewById(R.id.name_edit_text).animate().rotation(360).setDuration(1000);
                findViewById(R.id.next_sign_in_button).animate().rotation(360).setDuration(1000);

                mEmailEditText = (EditText) findViewById(R.id.email_edit_text);
                mPasswordEditText = (EditText) findViewById(R.id.password_edit_text);
                mFullNameEditText = (EditText) findViewById(R.id.name_edit_text);
                mClassEditText = (EditText) findViewById(R.id.class_edit_text);

                pressed = false;
                findViewById(R.id.next_sign_in_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!pressed) {

                            boolean errorOccurred = false;

                            if (!mEmailEditText.getText().toString().contains("@bmv.com")) {
                                mEmailEditText.setError("Must contain \"@bmv.com\"");
                                errorOccurred = true;
                            }

                            if (mPasswordEditText.getText().toString().equals("")) {
                                mPasswordEditText.setError("Invalid Password");
                                errorOccurred = true;
                            }

                            if (mFullNameEditText.getText().toString().equals("")) {
                                mFullNameEditText.setError("Invalid Name");
                                errorOccurred = true;
                            }

                            if (mClassEditText.getText().toString().equals("")) {
                                mClassEditText.setError("Invalid Class");
                                errorOccurred = true;
                            }

                            if (!errorOccurred) {

                                pressed = true;
                                findViewById(R.id.radio_button).setVisibility(View.VISIBLE);
                                float translate = mEmailEditText.getHeight();

                                mEmailEditText.animate().translationY(translate).alpha(0.0f);
                                mPasswordEditText.animate().translationY(translate).alpha(0.0f);
                                mFullNameEditText.animate().translationY(translate).alpha(0.0f);
                                mClassEditText.animate().translationY(translate).alpha(0.0f);
                                findViewById(R.id.radio_button).animate().rotation(360).setDuration(1000);

                            }

                        } else {

                                mAuth.createUserWithEmailAndPassword(
                                        mEmailEditText.getText().toString(),
                                        mPasswordEditText.getText().toString()
                                ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            mUser = mAuth.getCurrentUser();
                                            RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_button);
                                            if (radioGroup.getCheckedRadioButtonId() == R.id.student_radio_button) {
                                                mUserDatabaseReference = mDatabase.getReference()
                                                        .child("users")
                                                        .child(mUser.getUid());
                                                mUserDatabaseReference.setValue(new User(
                                                        "student",
                                                        false,
                                                        mFullNameEditText.getText().toString(),
                                                        Integer.parseInt(mClassEditText.getText().toString()),
                                                        FirebaseInstanceId.getInstance().getToken()
                                                ));

                                                // Asking Class Teacher for validating!
                                                mResponseDatabaseReference = mDatabase.getReference()
                                                        .child("responses")
                                                        .child("teachers");

                                                mResponseDatabaseReference
                                                        .push()
                                                        .setValue(new User(
                                                                "student",
                                                                false,
                                                                mFullNameEditText.getText().toString(),
                                                                Integer.parseInt(mClassEditText.getText().toString()),
                                                                FirebaseInstanceId.getInstance().getToken()
                                                        ));

                                                mResponseDatabaseReference = mDatabase.getReference()
                                                        .child("responses")
                                                        .child("principal");

                                                mResponseDatabaseReference
                                                        .push()
                                                        .setValue(new User(
                                                                "student",
                                                                false,
                                                                mFullNameEditText.getText().toString(),
                                                                Integer.parseInt(mClassEditText.getText().toString()),
                                                                FirebaseInstanceId.getInstance().getToken()
                                                        ));

                                            } else if (radioGroup.getCheckedRadioButtonId() == R.id.teacher_radio_button) {
                                                mUserDatabaseReference = mDatabase.getReference()
                                                        .child("users")
                                                        .child(mUser.getUid());
                                                mUserDatabaseReference.setValue(new User(
                                                        "teacher",
                                                        false,
                                                        mFullNameEditText.getText().toString(),
                                                        Integer.parseInt(mClassEditText.getText().toString()),
                                                        FirebaseInstanceId.getInstance().getToken()
                                                ));

                                                // Asking Class Teacher for validating!
                                                mResponseDatabaseReference = mDatabase.getReference()
                                                        .child("responses")
                                                        .child("principal");

                                                mResponseDatabaseReference
                                                        .push()
                                                        .setValue(new User(
                                                                "teacher",
                                                                false,
                                                                mFullNameEditText.getText().toString(),
                                                                Integer.parseInt(mClassEditText.getText().toString()),
                                                                FirebaseInstanceId.getInstance().getToken()
                                                        ));

                                            } else if (radioGroup.getCheckedRadioButtonId() == R.id.principal_radio_button) {
                                                mUserDatabaseReference = mDatabase.getReference()
                                                        .child("users")
                                                        .child(mUser.getUid());
                                                mUserDatabaseReference.setValue(new User(
                                                        "principal",
                                                        false,
                                                        mFullNameEditText.getText().toString(),
                                                        Integer.parseInt(mClassEditText.getText().toString()),
                                                        FirebaseInstanceId.getInstance().getToken()
                                                ));
                                            } else {
                                                mUserDatabaseReference = mDatabase.getReference()
                                                        .child("users")
                                                        .child(mUser.getUid());
                                                mUserDatabaseReference.setValue(new User(
                                                        "monitor",
                                                        false,
                                                        mFullNameEditText.getText().toString(),
                                                        Integer.parseInt(mClassEditText.getText().toString()),
                                                        FirebaseInstanceId.getInstance().getToken()
                                                ));

                                                // Asking Class Teacher for validating!
                                                mResponseDatabaseReference = mDatabase.getReference()
                                                        .child("responses")
                                                        .child("teachers");

                                                mResponseDatabaseReference
                                                        .push()
                                                        .setValue(new User(
                                                                "monitor",
                                                                false,
                                                                mFullNameEditText.getText().toString(),
                                                                Integer.parseInt(mClassEditText.getText().toString()),
                                                                FirebaseInstanceId.getInstance().getToken()
                                                        ));

                                                mResponseDatabaseReference = mDatabase.getReference()
                                                        .child("responses")
                                                        .child("principal");

                                                mResponseDatabaseReference
                                                        .push()
                                                        .setValue(new User(
                                                                "monitor",
                                                                false,
                                                                mFullNameEditText.getText().toString(),
                                                                Integer.parseInt(mClassEditText.getText().toString()),
                                                                FirebaseInstanceId.getInstance().getToken()
                                                        ));

                                            }
                                            updateUI();
                                        } else {
                                            Toast.makeText(
                                                    AuthActivity.this,
                                                    "Error Occurred",
                                                    Toast.LENGTH_SHORT
                                            ).show();
                                        }
                                    }
                                });
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

                findViewById(R.id.auth_root_view).setBackgroundColor(getResources().getColor(R.color.colorBackground));
                findViewById(R.id.heading_text_view).setVisibility(View.GONE);
                findViewById(R.id.log_in_button).setVisibility(View.GONE);
                findViewById(R.id.textView).setVisibility(View.GONE);
                findViewById(R.id.sign_up_button).setVisibility(View.GONE);
                findViewById(R.id.email_edit_text).setVisibility(View.VISIBLE);
                findViewById(R.id.password_edit_text).setVisibility(View.VISIBLE);
                findViewById(R.id.next_sign_in_button).setVisibility(View.VISIBLE);

                Button nextLogInButton = (Button) findViewById(R.id.next_sign_in_button);
                nextLogInButton.setText("Log-in");

                final EditText emailEditText = (EditText) findViewById(R.id.email_edit_text);
                final EditText passwordEditText = (EditText) findViewById(R.id.password_edit_text);

                nextLogInButton.setOnClickListener(new View.OnClickListener() {
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

}
