package com.school.seksaria.bmv;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
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

        CardView cardView = (CardView) convertView.findViewById(R.id.root);

        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(user.getFullName());

        TextView what = (TextView) convertView.findViewById(R.id.what);
        what.setText("wants to be a " + user.getWhat().substring(0,1).toUpperCase() + user.getWhat().substring(1) + " of");

        TextView _class = (TextView) convertView.findViewById(R.id.class_text_view);
        _class.setText("Class" + user.getClassNumber());

        if (position % 2 == 0) {
            cardView.setCardBackgroundColor(((Activity) getContext()).getResources().getColor(
                    R.color.colorRequestAccept
            ));

            name.setTextColor(((Activity) getContext()).getResources().getColor(
                    R.color.colorRequestPrimary
            ));

            _class.setTextColor(((Activity) getContext()).getResources().getColor(
                    R.color.colorRequestPrimary
            ));
        }

        return convertView;

    }

}