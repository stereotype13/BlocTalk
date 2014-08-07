package com.test.bloctalk.app;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by stereotype13 on 8/3/14.
 */
public abstract class Model implements Parcelable {

    private SQLiteDatabase mSQLiteDatabase;

    protected long mID = -1;
    protected String mTableName = "";
    protected String mNullableColumnName = null;



    //CRUD: create, read (subclasses will use getByID, etc), update, delete
    public Model() {
        mSQLiteDatabase = BlocTalk.getBlocTalkDB();

    }

    //Creates a blank record
    public long create() {
        mID = mSQLiteDatabase.insert(mTableName, mNullableColumnName, getContentValues());

        return mID;
    }

    //We only call cancel if we've created a new record, but then decide to cancel it before saving it.
    public long cancel() {
        if(mID < 0) {
            mSQLiteDatabase.delete(mTableName, String.valueOf(mID) + " = ?", new String[] {String.valueOf(mID)});
            return mID;
        }
        else {

            return -1;
        }

    }

    public long save() {
        ContentValues contentValues = getContentValues();

        if(contentValues.size() <= 0) {
            return mID;
        }

        mSQLiteDatabase.update(mTableName, getContentValues(), "_id = ?", new String[] {String.valueOf(mID)});

        return mID;
    }

    public long delete() {
        mSQLiteDatabase.delete(mTableName, String.valueOf(mID) + " = ?", new String[] {String.valueOf(mID)});
        return mID;
    }

    public abstract void setContract();
    public abstract ContentValues getContentValues();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    public void setID(long id) {
        mID = id;
    }

    public long getID() {
        return mID;
    }
}
