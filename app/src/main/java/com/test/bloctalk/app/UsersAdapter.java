package com.test.bloctalk.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class UsersAdapter extends ArrayAdapter<User> {

    private ArrayList<UserWrapper> mUserWrappers;

    private class UserWrapper {
        private User mUser;
        private boolean mSelected = false;

        public UserWrapper(User user) {
            mUser = user;
        }

        public boolean isSelected() {
            return mSelected;
        }

        public void setSelected(boolean setting) {
            mSelected = setting;
        }

        public User getUser() {
            return mUser;
        }
    }

    public UsersAdapter(Context context, ArrayList<User> users) {
        super(context, R.id.new_conversation_fragment_list_item);

        mUserWrappers = new ArrayList<UserWrapper>();

        for (User user : users) {
            UserWrapper userWrapper = new UserWrapper(user);
            mUserWrappers.add(userWrapper);
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View cView = convertView;
        if (cView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            cView = inflater.inflate(R.layout.new_conversation_fragment_list_item, parent, false);
        }

        UserWrapper userWrapper = (UserWrapper) getUserWrapper(position);


        // Setup Text
        TextView tvFirstName = (TextView) cView.findViewById(R.id.tvName);
        tvFirstName.setText(userWrapper.getUser().getName());

        TextView tvMobileNumber = (TextView) cView.findViewById(R.id.tvMobileNumber);
        tvMobileNumber.setText(userWrapper.getUser().getMobileNumber());

        CheckBox cbCheckBox = (CheckBox) cView.findViewById(R.id.cbIsSelected);
        cbCheckBox.setSelected(userWrapper.isSelected());
        // All done
        return cView;
    }

    @Override
    public int getCount() {
        return mUserWrappers.size();
    }

    @Override
    public User getItem(int i) {
        return mUserWrappers.get(i).getUser();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public UserWrapper getUserWrapper(int i) {
        return mUserWrappers.get(i);
    }
}
