package com.test.bloctalk.app;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by stereotype13 on 8/3/14.
 */
public class User extends Model {

    private String mName;
    private String mMobileNumber;

    public User() {
        super();
        setContract();
    }

    @Override
    public void setContract() {
        mTableName = BlocTalkDBContract.User.TABLE_NAME;
        mNullableColumnName = BlocTalkDBContract.User.COLUMN_NAME_NULLABLE;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(BlocTalkDBContract.User.NAME, mName);
        contentValues.put(BlocTalkDBContract.User.MOBILE_NUMBER, mMobileNumber);

        return contentValues;
    }

    public long getUserID() {
        return mID;
    }

    public void setName(String firstName) {
        mName = firstName;
    }

    public String getName() {
        return mName;
    }

    public void setMobileNumber(String mobileNumber) {
        mMobileNumber = mobileNumber;
    }

    public String getMobileNumber() {
        return mMobileNumber;
    }

}
