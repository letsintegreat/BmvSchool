package com.school.seksaria.bmv;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.support.v7.widget.Toolbar;

public class ClassTakerActivity extends AppCompatActivity implements View.OnClickListener {

    String redirectTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_taker);

        Bundle bundle = getIntent().getExtras();
        redirectTo = bundle.getString("redirectTo");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Class");

        findViewById(R.id.class_1).setOnClickListener(this);
        findViewById(R.id.class_2).setOnClickListener(this);
        findViewById(R.id.class_3).setOnClickListener(this);
        findViewById(R.id.class_4).setOnClickListener(this);
        findViewById(R.id.class_5).setOnClickListener(this);
        findViewById(R.id.class_6).setOnClickListener(this);
        findViewById(R.id.class_7).setOnClickListener(this);
        findViewById(R.id.class_8).setOnClickListener(this);
        findViewById(R.id.class_9).setOnClickListener(this);
        findViewById(R.id.class_10).setOnClickListener(this);
        findViewById(R.id.class_11).setOnClickListener(this);
        findViewById(R.id.class_12).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        int numberOfClass = 1;

        switch (view.getId()) {

            case R.id.class_1:
                numberOfClass = 1;
                break;

            case R.id.class_2:
                numberOfClass = 2;
                break;

            case R.id.class_3:
                numberOfClass = 3;
                break;

            case R.id.class_4:
                numberOfClass = 4;
                break;

            case R.id.class_5:
                numberOfClass = 5;
                break;

            case R.id.class_6:
                numberOfClass = 6;
                break;

            case R.id.class_7:
                numberOfClass = 7;
                break;

            case R.id.class_8:
                numberOfClass = 8;
                break;

            case R.id.class_9:
                numberOfClass = 9;
                break;

            case R.id.class_10:
                numberOfClass = 10;
                break;

            case R.id.class_11:
                numberOfClass = 11;
                break;

            case R.id.class_12:
                numberOfClass = 12;
                break;

        }

        if (redirectTo.equals("discussion")) {
            Intent intent = new Intent(ClassTakerActivity.this, DiscussionActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("class", numberOfClass);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (redirectTo.equals("homework")) {
            Intent intent = new Intent(ClassTakerActivity.this, HomeworkActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("class", numberOfClass);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
