package com.test.bloctalk.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stereotype13 on 8/3/14.
 */
public class NewConversationFragment extends Fragment {

    private static final String POTENTIAL_PARTICIPANTS = "POTENTIAL_PARTICIPANTS";
    private static final String CONVERSATION = "CONVERSATION";

    private ArrayList<User> mPotentialParticipants;
    private Conversation mConversation;

    public NewConversationFragment(Conversation conversation, ArrayList<User> potentialParticipants) {
        mConversation = conversation;
        mPotentialParticipants = potentialParticipants;
    }

    public NewConversationFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            mConversation = savedInstanceState.getParcelable(CONVERSATION);
            mPotentialParticipants = savedInstanceState.getParcelableArrayList(POTENTIAL_PARTICIPANTS);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.new_conversation_fragment, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.lvNewConversationFragment);

        listView.setAdapter(new UsersAdapter(getActivity(), mPotentialParticipants));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //CheckBox checkBox = (CheckBox)view.findViewById(R.id.cbIsSelected);

                CheckBox checkBox = (CheckBox) adapterView.findViewById(R.id.cbIsSelected);

                if(checkBox.isSelected()) {
                    checkBox.setSelected(false);
                }
                else {
                    checkBox.setSelected(true);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(POTENTIAL_PARTICIPANTS, mPotentialParticipants);
        outState.putParcelable(CONVERSATION, mConversation);
    }


}
