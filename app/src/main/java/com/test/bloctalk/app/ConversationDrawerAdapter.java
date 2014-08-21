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
 * Created by stereotype13 on 8/6/14.
 */
public class ConversationDrawerAdapter extends ArrayAdapter<Conversation> {

    //private ArrayList<Conversation> mConversations;

    public ConversationDrawerAdapter(Context context, ArrayList<Conversation> conversations) {
        super(context, R.layout.conversation_drawer_item, conversations);

        //mConversations = conversations;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View cView = convertView;
        if (cView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            cView = inflater.inflate(R.layout.conversation_drawer_item, parent, false);
        }

        Conversation conversation = getItem(position);


        // Setup Text
        TextView textView = (TextView) cView.findViewById(R.id.tvConversationDrawerItem);
        textView.setText(String.valueOf(conversation.getID()) + " " + conversation.getTitle());


        // All done
        return cView;
    }


}
