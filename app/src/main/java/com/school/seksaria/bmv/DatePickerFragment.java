package com.school.seksaria.bmv;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.icu.util.IndianCalendar;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;

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

    public interface GetClass {
        public int getNumberOfClass();
    }

    public GetClass myCallback;
    public void onAttach(Activity activity) {
        myCallback = (GetClass) activity;
        super.onAttach(activity);
    }

    ConstraintLayout newStuff, mainStuff;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, final int year, final int month, final int day) {

        mainStuff = (ConstraintLayout) getActivity().findViewById(R.id.main_stuff);
        newStuff = (ConstraintLayout) getActivity().findViewById(R.id.new_stuff);

        if (getActivity().findViewById(R.id.new_homework_edit_text) == null) {

            mainStuff.animate().translationX((float) -1.0 * mainStuff.getWidth());
            newStuff.animate().translationX(0);

            final DatabaseReference holidayDatabaseReference = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("holiday");

            final EditText editText = getActivity().findViewById(R.id.new_holiday_edit_text);
            final Button button = getActivity().findViewById(R.id.new_holiday_button);
            final ListView gridView = getActivity().findViewById(R.id.grid_view);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String date = "" + day + "-" + (month + 1) + "-" + year;
                    String tag;
                    tag = editText.getText().toString();
                    Holiday newHoliday = new Holiday(date, tag);
                    holidayDatabaseReference.push().setValue(newHoliday);

                    mainStuff.animate().translationX(0);
                    newStuff.animate().translationX((float) 1.0 * newStuff.getWidth());
                }
            });

        } else {

            final EditText subjectEditText = (EditText) getActivity().findViewById(R.id.new_subject_edit_text);
            final EditText homeworkEditText = (EditText) getActivity().findViewById(R.id.new_homework_edit_text);
            final Button button = (Button) getActivity().findViewById(R.id.new_homework_button);

            mainStuff.animate().translationX((float) -1.0 * mainStuff.getWidth());
            newStuff.animate().translationX(0);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String date = "" + day + "-" + (month + 1) + "-" + year;
                    final String subject = subjectEditText.getText().toString();
                    final String homework = homeworkEditText.getText().toString();

                    mainStuff.animate().translationX(0);
                    newStuff.animate().translationX((float) 1.0 * newStuff.getWidth());

//                    HomeworkActivity homeworkActivity = (HomeworkActivity) getActivity();

                    DatabaseReference homeworkDatabaseReference = FirebaseDatabase.getInstance()
                            .getReference()
                            .child("homework")
                            .child("" + myCallback.getNumberOfClass())
                            .child(date);

                    Holiday homeworkObject = new Holiday(homework, subject);
                    homeworkDatabaseReference.push().setValue(homeworkObject);
                }
            });
        }
    }
}