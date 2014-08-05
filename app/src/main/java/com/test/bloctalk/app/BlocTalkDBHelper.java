package com.test.bloctalk.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by stereotype13 on 8/3/14.
 */
public class BlocTalkDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BlocTalk.db";
    private static final int DATABASE_VERSION = 1;

    public BlocTalkDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(BlocTalkDBContract.Conversation.SQL_CREATE_CONVERSATIONS);
        sqLiteDatabase.execSQL(BlocTalkDBContract.Participant.SQL_CREATE_PARTICIPANTS);
        sqLiteDatabase.execSQL(BlocTalkDBContract.Message.SQL_CREATE_MESSAGES);
        sqLiteDatabase.execSQL(BlocTalkDBContract.User.SQL_CREATE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL(BlocTalkDBContract.Conversation.SQL_DELETE_CONVERSATIONS);
        sqLiteDatabase.execSQL(BlocTalkDBContract.Participant.SQL_DELETE_PARTICIPANTS);
        sqLiteDatabase.execSQL(BlocTalkDBContract.Message.SQL_DELETE_MESSAGES);
        sqLiteDatabase.execSQL(BlocTalkDBContract.User.SQL_DELETE_USERS);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
