package com.test.bloctalk.app;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements NewConversationFragment.INewConversationFragmentListener,
    ConversationFragment.IConversationFragmentListener
{

    private static final String NEW_CONVERSATION_FRAGMENT = "NEW_CONVERSATION_FRAGMENT";
    private NewConversationFragment mNewConversationFragment;

    private static final String CONVERSATION_FRAGMENT = "CONVERSATION_FRAGMENT";
    private ConversationFragment mConversationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null) {
            mNewConversationFragment = (NewConversationFragment)getFragmentManager().findFragmentByTag(NEW_CONVERSATION_FRAGMENT);
            mConversationFragment = (ConversationFragment) getFragmentManager().findFragmentByTag(CONVERSATION_FRAGMENT);
        }
        else {
            mNewConversationFragment = new NewConversationFragment();
            mConversationFragment = new ConversationFragment();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Gets called when the 'start a new conversation' button is clicked.
    public void onStartANewConversation(View view) {

        Conversation conversation = new Conversation();

        conversation.create();

        Uri CONTACT_CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME_PRIMARY = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PHONE_CONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String CONTACT_ID =  ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        String TYPE = ContactsContract.CommonDataKinds.Phone.TYPE;
        int TYPE_MOBILE = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;

        ContentResolver contentResolver = getContentResolver();

        Cursor contactCursor = contentResolver.query(CONTACT_CONTENT_URI, new String[] {_ID, DISPLAY_NAME_PRIMARY}, HAS_PHONE_NUMBER + " = 1", null, null);
        contactCursor.moveToFirst();

        ArrayList<User> potentialParticipants = new ArrayList<User>();

        if(contactCursor.getCount() > 0) {

            Cursor phoneCursor;
            String contactID;
            User user;

            while(contactCursor.moveToNext()) {
                 user = new User();

                contactID = contactCursor.getString(contactCursor.getColumnIndex(_ID));
                phoneCursor = contentResolver.query(PHONE_CONTENT_URI, new String[]{NUMBER}, CONTACT_ID + " = ? AND " + TYPE + " = ?", new String[]{contactID, String.valueOf(TYPE_MOBILE)}, null);
                phoneCursor.moveToFirst();


                user.setName(contactCursor.getString(contactCursor.getColumnIndex(DISPLAY_NAME_PRIMARY)));
                user.setMobileNumber(phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER)));

                potentialParticipants.add(user);
            }

            contactCursor.close();

        }

        mNewConversationFragment = new NewConversationFragment(conversation, potentialParticipants);

        getFragmentManager().beginTransaction().replace(android.R.id.content, mNewConversationFragment, NEW_CONVERSATION_FRAGMENT).addToBackStack(NEW_CONVERSATION_FRAGMENT).commit();


    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        }
        else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onSaveConversation(Conversation conversation, ArrayList<User> selectedParticipants) {
        long conversationID = conversation.getConversationID();

        for(User user : selectedParticipants) {
            Participant participant = new Participant();
            participant.create();
            participant.setConversationID(conversationID);
            participant.setUser(user);
            conversation.participants.add(participant);
            participant.save();
        }

        conversation.save();

        mConversationFragment = new ConversationFragment(conversation);

        getFragmentManager().beginTransaction().replace(android.R.id.content, mConversationFragment, CONVERSATION_FRAGMENT).addToBackStack(CONVERSATION_FRAGMENT).commit();

    }

    @Override
    public void onSendMessage(Conversation conversation, Message message) {
        message.setConversationID(conversation.getConversationID());
        message.save();
        conversation.messages.add(message);

        //Send the message to each participant

        for(Participant participant : conversation.participants) {
            message.send(participant);
        }
        //Refresh the list view of the conversation fragment once we've sent the message
        mConversationFragment.refresh(message);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }
}
