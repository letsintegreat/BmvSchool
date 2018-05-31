package com.school.seksaria.bmv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

public class HolidayAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> tags = new ArrayList<>();
    private ArrayList<String> dates = new ArrayList<>();
    private String mWhat;

    public HolidayAdapter(Context c, ArrayList<String> tTags, ArrayList<String> tDates , String what) {
        mContext = c;
        tags = tTags;
        dates = tDates;
        mWhat = what;
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
    public int getCount() {
        return tags.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cardView;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            cardView = new View(mContext);
            cardView = inflater.inflate(R.layout.holiday_card, null);

            TextView tagTextView = (TextView) cardView.findViewById(R.id.tag_text_view);
            tagTextView.setText(tags.get(position));

            TextView dateTextView = (TextView) cardView.findViewById(R.id.date_text_view);
            dateTextView.setText(dates.get(position));

            ImageView imageView = (ImageView) cardView.findViewById(R.id.image_view);
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

        } else {
            cardView = (View) convertView;
        }

        return cardView;

    }
}
