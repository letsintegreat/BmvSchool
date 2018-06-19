package com.school.seksaria.bmv;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class RequestAdapter extends ArrayAdapter<User> {
    public RequestAdapter(Context c, int resource, List<User> objects) {
        super(c, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater()
                    .inflate(R.layout.item_request, parent, false);
        }

        User user = getItem(position);

        TextView textView = (TextView) convertView.findViewById(R.id.name);
        textView.setText(user.getFullName());

        textView = (TextView) convertView.findViewById(R.id.what);
        textView.setText("wants to be a " + user.getWhat().substring(0,1).toUpperCase() + user.getWhat().substring(1) + " of");

        textView = (TextView) convertView.findViewById(R.id.class_text_view);
        textView.setText("Class" + user.getClassNumber());

        return convertView;
    }

}