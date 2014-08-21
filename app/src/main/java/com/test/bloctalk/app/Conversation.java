package com.test.bloctalk.app;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stereotype13 on 8/3/14.
 */
public class Conversation extends Model {

    public ArrayList<Message> messages;
    public ArrayList<Participant> participants;

    private long mTimeStamp;
    private String mTitle;

    public Conversation() {
        super();
        setContract();

        messages = new ArrayList<Message>();
        participants = new ArrayList<Participant>();
    }

    @Override
    public void setContract() {
        mTableName = BlocTalkDBContract.Conversation.TABLE_NAME;
        mNullableColumnName = BlocTalkDBContract.Conversation.COLUMN_NAME_NULLABLE;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BlocTalkDBContract.Conversation.TIME_STAMP, System.currentTimeMillis()/1000);
        contentValues.put(BlocTalkDBContract.Conversation.TITLE, mTitle);
        return contentValues;
    }

    public long getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        mTimeStamp = timeStamp;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public static ArrayList<Conversation> getConversations(SQLiteDatabase sqLiteDatabase) {
        Cursor cursor = sqLiteDatabase.query(BlocTalkDBContract.Conversation.TABLE_NAME, null, null, null, null, null, null);
        ArrayList<Conversation> conversations = new ArrayList<Conversation>();

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                Conversation conversation = new Conversation();
                conversation.setID(cursor.getLong(cursor.getColumnIndex("_ID")));
                conversation.setTitle(cursor.getString(cursor.getColumnIndex(BlocTalkDBContract.Conversation.TITLE)));
                conversations.add(conversation);
            } while(cursor.moveToNext());

        }

        return conversations;

    }

    public static ArrayList<Message> getMessages(Conversation conversation) {
        ArrayList<Message> messages = new ArrayList<Message>();
        String whereClause = BlocTalkDBContract.Message.CONVERSATION_ID + " = " + String.valueOf(conversation.getID());
        //Cursor cursor = sqLiteDatabase.query(BlocTalkDBContract.Message.TABLE_NAME, null, "CONVERSATION_ID = ?", new String[]{String.valueOf(conversation.getID())}, null, null, null );
        SQLiteDatabase sqLiteDatabase = BlocTalk.getBlocTalkDB();
        Cursor cursor = sqLiteDatabase.query(BlocTalkDBContract.Message.TABLE_NAME, null, whereClause, null, null, null, null );

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Message message = new Message();
                message.setConversation(conversation);
                message.setID(cursor.getLong(cursor.getColumnIndex("_ID")));
                Long userID = cursor.getLong(cursor.getColumnIndex(BlocTalkDBContract.Message.USER_ID));
                if(userID > 0) {
                    message.setUserID(userID);
                }

                message.setMessage(cursor.getString(cursor.getColumnIndex(BlocTalkDBContract.Message.MESSAGE)));

                messages.add(message);
            } while(cursor.moveToNext());
        }

        return messages;
    }

    public static ArrayList<Participant> getParticipants(Conversation conversation) {
        ArrayList<Participant> participantArrayList = new ArrayList<Participant>();
        String whereClause = BlocTalkDBContract.Participant.CONVERSATION_ID + " = " + String.valueOf(conversation.getID());
        SQLiteDatabase sqLiteDatabase = BlocTalk.getBlocTalkDB();
        Cursor cursor = sqLiteDatabase.query(BlocTalkDBContract.Participant.TABLE_NAME, new String[]{"_ID", BlocTalkDBContract.Participant.NUMBER, BlocTalkDBContract.Participant.CONVERSATION_ID, BlocTalkDBContract.Participant.USER_ID}, whereClause, null, null, null, null);

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Participant participant = new Participant();
                participant.setID(cursor.getLong(cursor.getColumnIndex("_ID")));
                participant.setNumber(cursor.getString(cursor.getColumnIndex(BlocTalkDBContract.Participant.NUMBER)));
                participant.setUserID(cursor.getLong(cursor.getColumnIndex(BlocTalkDBContract.Participant.USER_ID)));
                participant.setConversationID(cursor.getLong(cursor.getColumnIndex(BlocTalkDBContract.Participant.CONVERSATION_ID)));
                participantArrayList.add(participant);

            } while(cursor.moveToNext());
        }

        return participantArrayList;
    }

    public static Conversation getConversationByID(long ID) {
        SQLiteDatabase sqLiteDatabase = BlocTalk.getBlocTalkDB();
        Conversation conversation = new Conversation();
        String tableName = BlocTalkDBContract.Conversation.TABLE_NAME;
        String id = "_ID";
        String TITLE = BlocTalkDBContract.Conversation.TITLE;
        String[] projection = {id, TITLE};
        Cursor conversationCursor = sqLiteDatabase.query(tableName, projection, id + " = " + String.valueOf(ID), null, null, null, null, "1");

        if(conversationCursor.getCount() > 0) {
            conversationCursor.moveToFirst();
            conversation.setID(conversationCursor.getLong(conversationCursor.getColumnIndex("_ID")));
            conversation.setTitle(conversationCursor.getString(conversationCursor.getColumnIndex(TITLE)));
        }
        else {
            conversation = null;
        }

        return conversation;
    }
}
