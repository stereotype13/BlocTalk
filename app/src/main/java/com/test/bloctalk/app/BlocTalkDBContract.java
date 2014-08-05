package com.test.bloctalk.app;

import android.provider.BaseColumns;

/**
 * Created by stereotype13 on 8/3/14.
 */
public class BlocTalkDBContract {

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    BlocTalkDBContract() {}

    //Define the Tables
    public static abstract class EmptyTable implements BaseColumns {}


    public static class Conversation implements BaseColumns {
        public static final String TABLE_NAME = "Conversations";
        public static final String COLUMN_NAME_NULLABLE = "NULLABLE_COLUMN";

        public static final String SQL_CREATE_CONVERSATIONS = "CREATE TABLE " +
                TABLE_NAME + " (" +
                "_ID INTEGER PRIMARY KEY" + COMMA_SEP +
                COLUMN_NAME_NULLABLE + INTEGER_TYPE +
                ")";

        public static final String SQL_DELETE_CONVERSATIONS =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

    }

    public static abstract class Participant implements BaseColumns {
        public static final String TABLE_NAME = "Participants";
        public static final String USER_ID = "USER_ID";
        public static final String CONVERSATION_ID = "CONVERSATION_ID";
        public static final String COLUMN_NAME_NULLABLE = "NULLABLE_COLUMN";

        public static final String SQL_CREATE_PARTICIPANTS =
                "CREATE TABLE " +
                TABLE_NAME + " (" +
                "_ID INTEGER PRIMARY KEY" + COMMA_SEP +
                USER_ID + INTEGER_TYPE + COMMA_SEP +
                CONVERSATION_ID + INTEGER_TYPE  + COMMA_SEP +
                COLUMN_NAME_NULLABLE + INTEGER_TYPE +
                ")";

        public static final String SQL_DELETE_PARTICIPANTS =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class Message implements BaseColumns {
        public static final String TABLE_NAME = "Messages";
        public static final String USER_ID = "USER_ID";
        public static final String CONVERSATION_ID = "CONVERSATION_ID";
        public static final String MESSAGE = "MESSAGE";
        public static final String COLUMN_NAME_NULLABLE = "NULLABLE_COLUMN";

        public static final String SQL_CREATE_MESSAGES =
                "CREATE TABLE " +
                TABLE_NAME + " (" +
                "_ID INTEGER PRIMARY KEY" + COMMA_SEP +
                USER_ID + TEXT_TYPE + COMMA_SEP +
                CONVERSATION_ID + COMMA_SEP +
                MESSAGE + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_NULLABLE + INTEGER_TYPE +
                ")";

        public static final String SQL_DELETE_MESSAGES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

    }

    public static abstract class User implements BaseColumns {
        public static final String TABLE_NAME = "Users";
        public static final String NAME = "NAME";
        public static final String MOBILE_NUMBER = "MOBILE_NUMBER";
        public static final String COLUMN_NAME_NULLABLE = "NULLABLE_COLUMN";

        public static final String SQL_CREATE_USERS =
                "CREATE TABLE " +
                        TABLE_NAME + " (" +
                        "_ID INTEGER PRIMARY KEY" + COMMA_SEP +
                        NAME + TEXT_TYPE + COMMA_SEP +
                        MOBILE_NUMBER + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_NULLABLE + INTEGER_TYPE +
                        ")";

        public static final String SQL_DELETE_USERS =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}
