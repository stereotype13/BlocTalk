package com.test.bloctalk.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rhodel on 8/5/2014.
 */
public class MessagesAdapter extends ArrayAdapter<Message> {

    public MessagesAdapter(Context context, ArrayList<Message> messages) {
        super(context, R.id.message_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cView = convertView;
        if (cView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            cView = inflater.inflate(R.layout.message, parent, false);
        }

        Message message = (Message) getItem(position);

        // Setup Text
        TextView tvMessageItem = (TextView) cView.findViewById(R.id.tvMessageItem);
        tvMessageItem.setText(message.getMessage());

        // All done
        return cView;
    }
}
