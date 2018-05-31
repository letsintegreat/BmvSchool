package com.school.seksaria.bmv;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class UserAdapter extends BaseAdapter {
    private Context mContext;
    private long numOfUsers;

    private ArrayList<String> names;
    private ArrayList<Long> classes;
    private ArrayList<String> whats;

    public UserAdapter(Context c, ArrayList<String> tNames, ArrayList<Long> tClasses, ArrayList<String> tWhats) {
        this.mContext = c;
        this.names = tNames;
        this.classes = tClasses;
        this.whats = tWhats;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View cardView;

        if (convertView == null) {

            cardView = new View(mContext);

            cardView = inflater.inflate(R.layout.grid_view_item, null);

            ImageView imageView = (ImageView) cardView.findViewById(R.id.image_view);
            if (position % 4 == 0) {
                imageView.setBackgroundResource(R.drawable.circle_background_green);
            } else if (position % 4 == 1) {
                imageView.setBackgroundResource(R.drawable.circle_background_purple);
            } else if (position % 4== 2) {
                imageView.setBackgroundResource(R.drawable.circle_background_pink);
            } else {
                imageView.setBackgroundResource(R.drawable.circle_background_yellow);
            }

            TextView nameTextView = (TextView) cardView.findViewById(R.id.name_text_view);
            nameTextView.setText(names.get(position));

            TextView classTextView = (TextView) cardView.findViewById(R.id.class_text_view);
            classTextView.setText("" + classes.get(position));

            TextView whatTextView = (TextView) cardView.findViewById(R.id.what_text_view);
            whatTextView.setText(whats.get(position).substring(0, 1).toUpperCase() + whats.get(position).substring(1));

        } else {
            cardView = (View) convertView;
        }

        return cardView;

    }


}
