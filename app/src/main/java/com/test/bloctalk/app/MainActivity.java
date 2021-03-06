package com.test.bloctalk.app;

import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements NewConversationFragment.INewConversationFragmentListener,
    ConversationFragment.IConversationFragmentListener, SMSReceiver.ISMSReceiverListener
{

    private static final String NEW_CONVERSATION_FRAGMENT = "NEW_CONVERSATION_FRAGMENT";
    private NewConversationFragment mNewConversationFragment;

    private static final String CONVERSATION_FRAGMENT = "CONVERSATION_FRAGMENT";
    private ConversationFragment mConversationFragment;

    private DrawerLayout mConversationDrawer;
    private ListView mConversationDrawerListView;

    private static final String CONVERSATIONS = "CONVERSATIONS";
    private ArrayList<Conversation> mConversations;

    private static final String TITLE = "TITLE";
    private CharSequence mTitle;

    private ConversationDrawerAdapter mConversationDrawerAdapter;
    private SMSReceiver mSMSReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        if(savedInstanceState != null) {
            mNewConversationFragment = (NewConversationFragment)getFragmentManager().findFragmentByTag(NEW_CONVERSATION_FRAGMENT);
            mConversationFragment = (ConversationFragment) getFragmentManager().findFragmentByTag(CONVERSATION_FRAGMENT);
            mConversations = savedInstanceState.getParcelableArrayList(CONVERSATIONS);
        }
        else {
            mNewConversationFragment = new NewConversationFragment();
            mConversationFragment = new ConversationFragment();
            mConversations = Conversation.getConversations(BlocTalk.getBlocTalkDB());
            mTitle = getString(R.string.app_name);
        }

        mConversationDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mConversationDrawerListView = (ListView) findViewById(R.id.left_drawer);
        mConversationDrawerAdapter = new ConversationDrawerAdapter(this, mConversations);
        mConversationDrawerListView.setAdapter(mConversationDrawerAdapter);
        mConversationDrawerListView.setOnItemClickListener(new DrawerItemClickListener());

        mSMSReceiver = new SMSReceiver();
        mSMSReceiver.setListener(this);

        IntentFilter smsIntentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(mSMSReceiver, smsIntentFilter);
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
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER;
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

            do {
                 user = new User();

                contactID = contactCursor.getString(contactCursor.getColumnIndex(_ID));
                phoneCursor = contentResolver.query(PHONE_CONTENT_URI, new String[]{NUMBER}, CONTACT_ID + " = ? AND " + TYPE + " = ?", new String[]{contactID, String.valueOf(TYPE_MOBILE)}, null);

                if(phoneCursor.getCount() > 0) {
                    phoneCursor.moveToFirst();

                    user.setName(contactCursor.getString(contactCursor.getColumnIndex(DISPLAY_NAME_PRIMARY)));
                    user.setMobileNumber(phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER)));

                    potentialParticipants.add(user);
                }

            } while(contactCursor.moveToNext());

            contactCursor.close();

        }

        mNewConversationFragment = new NewConversationFragment(conversation, potentialParticipants);

        getFragmentManager().beginTransaction().replace(R.id.content_frame, mNewConversationFragment, NEW_CONVERSATION_FRAGMENT).addToBackStack(NEW_CONVERSATION_FRAGMENT).commit();


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
        long conversationID = conversation.getID();

        String title = "";
        for(User user : selectedParticipants) {
            Participant participant = new Participant();
            participant.create();
            participant.setConversationID(conversationID);
            participant.setUser(user);
            participant.setDisplayName(user.getName());
            participant.setNumber(user.getMobileNumber());
            conversation.participants.add(participant);
            title = title + participant.getDisplayName() + " ";
            participant.save();
        }

        conversation.setTitle(title);

        conversation.save();

        //Refresh the adapter of our navigation drawer
        mConversations = Conversation.getConversations(BlocTalk.getBlocTalkDBHelper().getReadableDatabase());
        mConversationDrawerAdapter.clear();
        mConversationDrawerAdapter.addAll(mConversations);
        mConversationDrawerAdapter.notifyDataSetChanged();

        mConversationFragment = new ConversationFragment(conversation);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack();
        getFragmentManager().beginTransaction().replace(R.id.content_frame, mConversationFragment, CONVERSATION_FRAGMENT).addToBackStack(CONVERSATION_FRAGMENT).commit();

    }

    @Override
    public void onSendMessage(Conversation conversation, Message message) {

        message.save();

        //Toast.makeText(this, "Saving message with CONVERSATION_ID: " + String.valueOf(message.getConversationID()) + ", MESSAGE: " + message.getMessage() + "MESSAGE_ID: " + String.valueOf(message.getID()), 1000).show();
        conversation.messages.add(message);

        //Send the message to each participant
        conversation.participants = Conversation.getParticipants(conversation);

        for(Participant participant : conversation.participants) {
            message.send(participant);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(CONVERSATIONS, mConversations);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    //Gets called when user clicks a conversation in the navigation drawer
    private void selectItem(int position) {

        Conversation conversation = new Conversation();
        conversation = mConversations.get(position);
        conversation.messages = Conversation.getMessages(conversation);
        conversation.participants = Conversation.getParticipants(conversation);

        FragmentManager fragmentManager = getFragmentManager();
        if(fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }

        ConversationFragment conversationFragment = new ConversationFragment(conversation);

        fragmentManager.beginTransaction().replace(R.id.content_frame, conversationFragment, CONVERSATION_FRAGMENT).addToBackStack(CONVERSATION_FRAGMENT).commit();

        mConversationDrawerListView.setItemChecked(position, true);

        setTitle(String.valueOf(((Conversation)mConversationDrawerListView.getItemAtPosition(position)).getID()));
        mConversationDrawer.closeDrawer(mConversationDrawerListView);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    public void onNoUsersChosen() {

        String alertMessage = getString(R.string.choose_at_least_one_contact);
        AlertDialog alertDialog = new AlertDialog(alertMessage);
        alertDialog.show(getSupportFragmentManager(), "ALERT_DIALOG");

    }

    @Override
    public void onMessageReceived(Conversation conversation, Message message) {
        ConversationFragment conversationFragment = (ConversationFragment)getFragmentManager().findFragmentByTag(CONVERSATION_FRAGMENT);

        if(conversationFragment != null) {
            conversationFragment.refresh(conversation);
        }
    }

    public void onTest(View view) {
        SQLiteDatabase sqLiteDatabase = BlocTalk.getBlocTalkDB();
        String participantTable = BlocTalkDBContract.Participant.TABLE_NAME;
        String _ID = BlocTalkDBContract.Participant._ID;
        String NUMBER = BlocTalkDBContract.Participant.NUMBER;
        String TIME_STAMP = BlocTalkDBContract.Participant.TIME_STAMP;
        String CONVERSATION_ID = BlocTalkDBContract.Participant.CONVERSATION_ID;
        String[] projection = {_ID, NUMBER, TIME_STAMP, CONVERSATION_ID};
        Cursor cursor = sqLiteDatabase.query(participantTable, projection, NUMBER + " = '+19513337993'", null, null, null, "_ID DESC", "1");
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String id = String.valueOf(cursor.getLong(cursor.getColumnIndex("_ID")));
                String number = cursor.getString(cursor.getColumnIndex(NUMBER));
                String timeStamp = String.valueOf(cursor.getLong(cursor.getColumnIndex(TIME_STAMP)));
                String conversationID = String.valueOf(cursor.getLong(cursor.getColumnIndex(CONVERSATION_ID)));
                Toast.makeText(this, "_ID: " + id + ", NUMBER: " + number + ", CONVERSATION_ID: " + conversationID, 1000).show();
            } while (cursor.moveToNext());
        }

    }
}
