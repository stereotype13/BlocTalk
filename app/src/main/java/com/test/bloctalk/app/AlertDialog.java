package com.test.bloctalk.app;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by stereotype13 on 8/10/14.
 */
public class AlertDialog extends DialogFragment {

    private static final String ALERT_MESSAGE = "ALERT_MESSAGE";
    private String mAlertMessage;

    public AlertDialog() {

    }

    public AlertDialog(String alertMessage) {
        mAlertMessage =  alertMessage;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            mAlertMessage = savedInstanceState.getString(ALERT_MESSAGE);
        }
      
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = (View)inflater.inflate(R.layout.alert_dialog, container);
        TextView tv = (TextView) rootView.findViewById(R.id.tvAlertMessage);
        tv.setText(mAlertMessage);

        Button button = (Button) rootView.findViewById(R.id.btnDismissAlertDialog);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ALERT_MESSAGE, mAlertMessage);
    }
}
