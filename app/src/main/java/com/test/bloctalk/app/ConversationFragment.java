package com.test.bloctalk.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by rhodel on 8/5/2014.
 */
public class ConversationFragment extends Fragment {

    private ListView mListView;
    private EditText mEditText;
    private ImageButton mSendButton;

    private static final String CONVERSATION = "CONVERSATION";
    private Conversation mConversation;

    private IConversationFragmentListener mListener;
    private MessagesAdapter mMessagesAdapter;

    private static final String MESSAGES = "MESSAGES";
    private ArrayList<Message> mMessages;

    public ConversationFragment(){

    }

    public ConversationFragment(Conversation conversation) {
        mConversation = conversation;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            mConversation = savedInstanceState.getParcelable(CONVERSATION);
            mMessages = savedInstanceState.getParcelableArrayList(MESSAGES);
        }
        else {
            mMessages = new ArrayList<Message>();
            mMessages = mConversation.messages;
        }

        mMessagesAdapter = new MessagesAdapter(getActivity(), mMessages);

        mListener = (IConversationFragmentListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = (View)inflater.inflate(R.layout.conversation_fragment, container, false);

        mListView = (ListView) rootView.findViewById(R.id.lwConversation);

        mListView.setAdapter(mMessagesAdapter);

        mEditText = (EditText) rootView.findViewById(R.id.etMessage);

        mSendButton = (ImageButton) rootView.findViewById(R.id.btnSend);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message();
                message.create();
                message.setMessage(mEditText.getText().toString());
                message.setConversation(mConversation);
                mListener.onSendMessage(mConversation, message);
                mEditText.setText("");
            }
        });

        return rootView;
    }

    public interface IConversationFragmentListener {
        public void onSendMessage(Conversation conversation, Message message);
    }

    public void refresh(Conversation conversation){
        mConversation = Conversation.getConversationByID(conversation.getID());
        mMessages = Conversation.getMessages(mConversation);
        mMessagesAdapter.clear();
        mMessagesAdapter.addAll(mMessages);
        mMessagesAdapter.notifyDataSetChanged();

    }

    public void refresh(Message message){
       // mMessagesAdapter.add(message);
        //mMessagesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CONVERSATION, mConversation);
        outState.putParcelableArrayList(MESSAGES, mMessages);
    }
}
