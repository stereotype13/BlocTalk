package com.test.bloctalk.app;

import android.content.ContentValues;

/**
 * Created by stereotype13 on 8/3/14.
 */
public class Participant extends Model {

    private long mUserID;
    private long mConversationID;

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
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
        mConversationID = conversation.getConversationID();
    }
}
