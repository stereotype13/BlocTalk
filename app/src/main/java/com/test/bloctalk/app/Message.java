package com.test.bloctalk.app;

import android.content.ContentValues;

/**
 * Created by stereotype13 on 8/3/14.
 */
public class Message extends Model {

    private long mUserID = -1; //-1 = self
    private String mMessage = null;
    private long mConversationID;

    public Conversation conversation;
    public User user;


    public Message(){
        super();
        setContract();
    }

    @Override
    public void setContract() {
        mTableName = BlocTalkDBContract.Message.TABLE_NAME;
        mNullableColumnName = BlocTalkDBContract.Message.COLUMN_NAME_NULLABLE;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(BlocTalkDBContract.Message.USER_ID, mUserID);
        contentValues.put(BlocTalkDBContract.Message.MESSAGE, mMessage);
        contentValues.put(BlocTalkDBContract.Message.CONVERSATION_ID, mConversationID);

        return contentValues;
    }

    public void setUser(User user) {
        this.user = user;
        mUserID = user.getUserID();
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
        mConversationID = conversation.getConversationID();
    }

    public long getMessageID() {
        return mID;
    }

    public void setUserID(long userID) {
        mUserID = userID;
    }

    public long getUserID() {
        return mUserID;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setConversationID(long conversationID) {
        mConversationID = conversationID;
    }

    public long getConversationID() {
        return mConversationID;
    }
}
