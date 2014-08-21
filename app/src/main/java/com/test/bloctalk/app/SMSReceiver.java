package com.test.bloctalk.app;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by stereotype13 on 8/10/14.
 */
public class SMSReceiver extends BroadcastReceiver {

    private ISMSReceiverListener mListener;
    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public void setListener(Context context) {
        mListener = (ISMSReceiverListener) context;
    }

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                Uri PHONE_CONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String CONTACT_ID =  ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
                String TYPE = ContactsContract.CommonDataKinds.Phone.TYPE;
                String DISPLAY_NAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
                String NUMBER = ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER;
                int TYPE_MOBILE = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;


                Cursor phoneCursor;
                ContentResolver contentResolver = context.getContentResolver();

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();



                    Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                    Participant participant = Participant.getParticipantByNumber(senderNum);

                    if(participant != null) {
                        message = participant.getDisplayName() + ": " + message;
                        participant.conversation = Conversation.getConversationByID(participant.getConversationID());
                        Message smsMessage = new Message();
                        smsMessage.create();

                        smsMessage.setConversation(participant.conversation);
                        smsMessage.setMessage(message);
                        smsMessage.setUserID(participant.getID());
                        smsMessage.save();

                        if(mListener != null) {
                            mListener.onMessageReceived(smsMessage.conversation, smsMessage);
                        }


                    }
                    else {
                        phoneCursor = contentResolver.query(PHONE_CONTENT_URI, new String[]{CONTACT_ID, DISPLAY_NAME}, NUMBER + " = ?", new String[]{senderNum}, null);
                        phoneCursor.moveToFirst();
                        String userName = phoneCursor.getString(phoneCursor.getColumnIndex(DISPLAY_NAME));
                    }

                    // Show Alert
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context,
                            "senderNum: "+ senderNum +  ", message: " + message, duration);
                    toast.show();

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }


    }



    public interface ISMSReceiverListener {
        public void onMessageReceived(Conversation conversation, Message message);
    }
}
