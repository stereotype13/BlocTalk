package com.test.bloctalk.app;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

/**
 * Created by stereotype13 on 8/3/14.
 */
public class BlocTalk extends Application {

    private static BlocTalkDBHelper mBlocTalkDBHelper;
    private static SQLiteDatabase mBlocTalkDB;

    @Override
    public void onCreate() {

        //Instantiate the database when we start out app.
        mBlocTalkDBHelper = new BlocTalkDBHelper(getApplicationContext());

        //Get the database in a different thread, since it may take a while.
        GetWritableDatabaseTask writableDatabaseTask = new GetWritableDatabaseTask();
        writableDatabaseTask.execute();
    }

    public static BlocTalkDBHelper getBlocTalkDBHelper() {
        return mBlocTalkDBHelper;
    }

    public static SQLiteDatabase getBlocTalkDB() {
        return mBlocTalkDB;
    }

    //This gets called when the thread fetching the database finishes.
    private void onDBTaskCompleted(SQLiteDatabase db) {
        //Set the model's database
        mBlocTalkDB = db;
    }

    private class GetWritableDatabaseTask extends AsyncTask<Void, Integer, SQLiteDatabase> {
        @Override
        protected SQLiteDatabase doInBackground(Void... voids) {
            return BlocTalk.getBlocTalkDBHelper().getWritableDatabase();
        }

        @Override
        protected void onPostExecute(SQLiteDatabase sqLiteDatabase) {
            super.onPostExecute(sqLiteDatabase);
            onDBTaskCompleted(sqLiteDatabase);
        }
    }
}