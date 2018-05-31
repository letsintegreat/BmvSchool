package com.school.seksaria.bmv;

import android.app.Dialog;
import android.app.DialogFragment;
import android.icu.util.IndianCalendar;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private User user;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, final int year, final int month, final int day) {

        if (getActivity().findViewById(R.id.new_homework_edit_text) == null) {
            final DatabaseReference holidayDatabaseReference = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("holiday");

            final EditText editText = getActivity().findViewById(R.id.new_holiday_edit_text);
            final Button button = getActivity().findViewById(R.id.new_holiday_button);
            final GridView gridView = getActivity().findViewById(R.id.grid_view);

            editText.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String date = "" + day + "-" + (month + 1) + "-" + year;
                    String tag;
                    tag = editText.getText().toString();
                    Holiday newHoliday = new Holiday(date, tag);
                    holidayDatabaseReference.push().setValue(newHoliday);
                    editText.setVisibility(View.GONE);
                    button.setVisibility(View.GONE);
                    gridView.setVisibility(View.VISIBLE);
                }
            });

        } else {

            final EditText subjectEditText = (EditText) getActivity().findViewById(R.id.new_subject_edit_text);
            final EditText homeworkEditText = (EditText) getActivity().findViewById(R.id.new_homework_edit_text);
            final Button button = (Button) getActivity().findViewById(R.id.new_homework_button);
            final GridView gridView = (GridView) getActivity().findViewById(R.id.grid_view);

            gridView.setVisibility(View.GONE);
            subjectEditText.setVisibility(View.VISIBLE);
            homeworkEditText.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String date = "" + day + "-" + (month + 1) + "-" + year;
                    final String subject = subjectEditText.getText().toString();
                    final String homework = homeworkEditText.getText().toString();

                    subjectEditText.setVisibility(View.GONE);
                    homeworkEditText.setVisibility(View.GONE);
                    button.setVisibility(View.GONE);
                    gridView.setVisibility(View.VISIBLE);

                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


                    DatabaseReference mUserDatabaseReference = FirebaseDatabase.getInstance()
                            .getReference()
                            .child("users")
                            .child(firebaseUser.getUid());

                    mUserDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            user = dataSnapshot.getValue(User.class);
                            DatabaseReference homeworkDatabaseReference = FirebaseDatabase.getInstance()
                                    .getReference()
                                    .child("homework")
                                    .child(""+user.getClassNumber())
                                    .child(date);

                            Holiday homeworkObject = new Holiday(homework, subject);

                            homeworkDatabaseReference.push().setValue(homeworkObject);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });

                }
            });

        }

    }

}
