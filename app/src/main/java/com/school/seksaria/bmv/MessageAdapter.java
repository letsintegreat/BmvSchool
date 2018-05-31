package com.school.seksaria.bmv;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {
    public MessageAdapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message, parent, false);
        }

        Message message = getItem(position);

        TextView messageTextView = (TextView) convertView.findViewById(R.id.message_text_view);
        TextView nameTextView = (TextView) convertView.findViewById(R.id.name_text_view);

        messageTextView.setText(message.getMessageContext());
        nameTextView.setText(message.getUserName());

        return convertView;
    }
}
