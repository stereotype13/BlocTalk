package com.test.bloctalk.app;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

/**
 * Created by stereotype13 on 8/3/14.
 */
public class Participant extends Model {

    private long mUserID;
    private long mConversationID;
    private String mNumber;
    private long mTimeStamp;

    public Conversation conversation;
    public User user;

    public Participant() {
        super();
        setContract();
    }

    @Override
    public void setContract() {
        mTableName = BlocTalkDBContract.Participant.TABLE_NAME;
        mNullableColumnName = BlocTalkDBContract.Participant.COLUMN_NAME_NULLABLE;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(BlocTalkDBContract.Participant.USER_ID, mUserID);
        contentValues.put(BlocTalkDBContract.Participant.CONVERSATION_ID, mConversationID);
        contentValues.put(BlocTalkDBContract.Participant.NUMBER, mNumber);
        contentValues.put(BlocTalkDBContract.Participant.TIME_STAMP, System.currentTimeMillis()/1000);

        return contentValues;
    }

    public long getParticipantID() {
        return mID;
    }

    public void setUserID(long userID) {
        mUserID = userID;
    }

    public long getUserID() {
        return mUserID;
    }

    public void setConversationID(long conversationID) {
        mConversationID = conversationID;
    }

    public long getConversationID() {
        return mConversationID;
    }

    public void setUser(User user) {
        this.user = user;
        mUserID = user.getUserID();
        mNumber = user.getMobileNumber();
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
        mConversationID = conversation.getID();
    }

    public void setNumber(String number) {
        this.mNumber = number;
    }

    public String getNumber() {
        return mNumber;
    }

    public long getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        mTimeStamp = timeStamp;
    }

    public static Participant getParticipantByNumber(String number) {
        //Must take the NORMALIZED number
        Participant participant = new Participant();
        SQLiteDatabase sqLiteDatabase = BlocTalk.getBlocTalkDB();
        String[] projection = {"_ID", BlocTalkDBContract.Participant.USER_ID, BlocTalkDBContract.Participant.CONVERSATION_ID, BlocTalkDBContract.Participant.NUMBER, BlocTalkDBContract.Participant.TIME_STAMP};
        Cursor participantCursor = sqLiteDatabase.query(BlocTalkDBContract.Participant.TABLE_NAME, projection, "NUMBER = '" + number + "'", null, null, null, "_ID DESC", "1");

        if(participantCursor.getCount() > 0) {
            participantCursor.moveToFirst();

            participant.setID(participantCursor.getLong(participantCursor.getColumnIndex("_ID")));
            participant.setConversationID(participantCursor.getLong(participantCursor.getColumnIndex(BlocTalkDBContract.Participant.CONVERSATION_ID)));
            participant.setNumber(participantCursor.getString(participantCursor.getColumnIndex(BlocTalkDBContract.Participant.NUMBER)));
            participant.setTimeStamp(participantCursor.getLong(participantCursor.getColumnIndex(BlocTalkDBContract.Participant.TIME_STAMP)));

            if(Long.valueOf(participant.getConversationID()) != null) {
                participant.conversation = Conversation.getConversationByID(participant.getConversationID());
            }

            ///////////////////
            String id = String.valueOf(participantCursor.getLong(participantCursor.getColumnIndex("_ID")));
            number = participantCursor.getString(participantCursor.getColumnIndex(BlocTalkDBContract.Participant.NUMBER));
            String timeStamp = String.valueOf(participantCursor.getLong(participantCursor.getColumnIndex(BlocTalkDBContract.Participant.TIME_STAMP)));
            String conversationID = String.valueOf(participantCursor.getLong(participantCursor.getColumnIndex(BlocTalkDBContract.Participant.CONVERSATION_ID)));
            Toast.makeText(BlocTalk.getContext(), "_ID: " + id + ", NUMBER: " + number + ", CONVERSATION_ID: " + conversationID + ", participant.conversation: " + String.valueOf(participant.conversation.getID()), 3000).show();

        }
        else {
            participant = null;
        }

        return participant;

    }
}
