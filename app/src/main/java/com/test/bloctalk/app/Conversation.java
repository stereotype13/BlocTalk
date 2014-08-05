package com.test.bloctalk.app;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stereotype13 on 8/3/14.
 */
public class Conversation extends Model {

    public ArrayList<Message> messages;
    public ArrayList<Participant> participants;

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
        return contentValues;
    }

    public long getConversationID() {
        return mID;
    }


}
