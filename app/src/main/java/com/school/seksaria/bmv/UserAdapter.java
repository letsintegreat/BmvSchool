package com.school.seksaria.bmv;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class UserAdapter extends ArrayAdapter<Map> {

    public UserAdapter(Context c, int resource, List<Map> objects) {
        super(c, resource, objects);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.grid_view_item, parent, false);
        }

        Map user = getItem(position);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.image_view);
        if (position % 4 == 0) {
            imageView.setBackgroundResource(R.drawable.circle_background_green);
        } else if (position % 4 == 1) {
            imageView.setBackgroundResource(R.drawable.circle_background_purple);
        } else if (position % 4== 2) {
            imageView.setBackgroundResource(R.drawable.circle_background_pink);
        } else {
            imageView.setBackgroundResource(R.drawable.circle_background_yellow);
        }

        TextView nameTextView = (TextView) convertView.findViewById(R.id.name_text_view);
        nameTextView.setText((String) user.get("fullName"));

        TextView classTextView = (TextView) convertView.findViewById(R.id.class_text_view);
        classTextView.setText(user.get("classNumber").toString());

        TextView whatTextView = (TextView) convertView.findViewById(R.id.what_text_view);
        whatTextView.setText(user.get("what").toString().substring(0,1).toUpperCase() +
            user.get("what").toString().substring(1));

        return convertView;

    }


}
