package com.school.seksaria.bmv;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class HolidayAdapter extends ArrayAdapter<String> {

    public interface Populate {
        public ArrayList<String> getAdapterTags();
        public ArrayList<String> getAdapterDates();
        public String getAdapterWhat();
    }

    public Populate populateCallback;

    private ArrayList<String> tags = new ArrayList<>();
    private ArrayList<String> dates = new ArrayList<>();
    private String mWhat;

//    public HolidayAdapter(Context c, ArrayList<String> tTags, ArrayList<String> tDates , String what) {
//        mContext = c;
//        tags = tTags;
//        dates = tDates;
//        mWhat = what;
//    }

    public HolidayAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        populateCallback = (Populate) context;
        tags = populateCallback.getAdapterTags();
        dates = populateCallback.getAdapterDates();
        mWhat = populateCallback.getAdapterWhat();
    }

    @Nullable
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView  == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().
                    inflate(R.layout.holiday_card,
                            parent,
                            false);
        }

        TextView tagTextView = (TextView) convertView.findViewById(R.id.tag_text_view);
        tagTextView.setText(tags.get(position));

        TextView dateTextView = (TextView) convertView.findViewById(R.id.date_text_view);
        dateTextView.setText(dates.get(position));

        ImageView imageView = (ImageView) convertView.findViewById(R.id.image_view);
        if (position % 4 == 0) {
            imageView.setBackgroundResource(R.drawable.circle_background_green);
        } else if (position % 4 == 1) {
            imageView.setBackgroundResource(R.drawable.circle_background_purple);
        } else if (position % 4 == 2) {
            imageView.setBackgroundResource(R.drawable.circle_background_pink);
        } else {
            imageView.setBackgroundResource(R.drawable.circle_background_yellow);
        }

        if (mWhat.equals("homework")) {
            imageView.setImageResource(R.drawable.ic_edit_black_24dp);
        }

        return convertView;

    }
}
