package com.test.bloctalk.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stereotype13 on 8/3/14.
 */
public class NewConversationFragment extends Fragment {

    private static final String POTENTIAL_PARTICIPANTS = "POTENTIAL_PARTICIPANTS";
    private static final String SELECTED_PARTICIPANTS = "SELECTED_PARTICIPANTS";
    private static final String CONVERSATION = "CONVERSATION";

    private ArrayList<User> mPotentialParticipants;
    private ArrayList<User> mSelectedParticipants;
    private Conversation mConversation;
    private ArrayList<UsersAdapter.UserWrapper> mUserWrappers;

    private UsersAdapter mUsersAdapter;

    private INewConversationFragmentListener mListener;

    public NewConversationFragment(Conversation conversation, ArrayList<User> potentialParticipants) {
        mConversation = conversation;
        mPotentialParticipants = potentialParticipants;
    }

    public NewConversationFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListener = (INewConversationFragmentListener) getActivity();


        if(savedInstanceState != null) {
            mConversation = savedInstanceState.getParcelable(CONVERSATION);
            mPotentialParticipants = savedInstanceState.getParcelableArrayList(POTENTIAL_PARTICIPANTS);
            mSelectedParticipants = savedInstanceState.getParcelableArrayList(SELECTED_PARTICIPANTS);
        }
        else {
            mSelectedParticipants = new ArrayList<User>();
        }

        mUsersAdapter = new UsersAdapter(getActivity(), mPotentialParticipants);
        mUserWrappers = mUsersAdapter.getUserWrappers();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.new_conversation_fragment, container, false);

        final GridView gridView = (GridView) rootView.findViewById(R.id.gvNewConversation);

        gridView.setAdapter(mUsersAdapter);

        Button btnCreateConversation = (Button) rootView.findViewById(R.id.btnCreateConversation);
        btnCreateConversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<UsersAdapter.UserWrapper> userWrappers = mUsersAdapter.getUserWrappers();

                for(UsersAdapter.UserWrapper userWrapper : userWrappers) {
                    if(userWrapper.isSelected()) {

                        mSelectedParticipants.add(userWrapper.getUser());
                    }
                }

                mListener.onSaveConversation(mConversation, mSelectedParticipants);

            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ArrayList<UsersAdapter.UserWrapper> userWrappers = mUsersAdapter.getUserWrappers();

                UsersAdapter.UserWrapper userWrapper = mUsersAdapter.getUserWrapper(i);

                CheckBox checkBox = (CheckBox) view.findViewById(R.id.cbIsSelected);

                if(checkBox.isChecked()) {
                    checkBox.setChecked(false);
                    userWrapper.setSelected(false);
                    userWrappers.set(i, userWrapper);
                }
                else {
                    checkBox.setChecked(true);
                    userWrapper.setSelected(true);
                    userWrappers.set(i, userWrapper);
                }

                mUsersAdapter.setUserWrappers(userWrappers);

            }
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(POTENTIAL_PARTICIPANTS, mPotentialParticipants);
        outState.putParcelableArrayList(SELECTED_PARTICIPANTS, mSelectedParticipants);
        outState.putParcelable(CONVERSATION, mConversation);
    }

    public interface INewConversationFragmentListener {
        public void onSaveConversation(Conversation conversation, ArrayList<User> selectedParticipants);
    }


}
