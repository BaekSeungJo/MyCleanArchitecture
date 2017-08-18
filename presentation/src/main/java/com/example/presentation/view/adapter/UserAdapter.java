package com.example.presentation.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.presentation.R;
import com.example.presentation.model.UserModel;

import java.util.Collection;
import java.util.List;

/**
 * Created by plnc on 2017-06-27.
 */

public class UserAdapter extends BaseAdapter {

    private List<UserModel> usersCollection;
    private final LayoutInflater layoutInflater;

    public UserAdapter(Context context, Collection<UserModel> usersCollection) {
        validateUsersCollection(usersCollection);
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.usersCollection =(List<UserModel>) usersCollection;
    }

    @Override
    public int getCount() {
        int count = 0;
        if(usersCollection != null && !usersCollection.isEmpty()) {
            count = usersCollection.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        return usersCollection.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserViewHolder userViewHolder;

        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_user, parent, false);

            userViewHolder = new UserViewHolder();
            userViewHolder.textViewTitle = (TextView) convertView.findViewById(R.id.title);

            convertView.setTag(userViewHolder);
        } else {
            userViewHolder = (UserViewHolder) convertView.getTag();
        }

        UserModel userModel = usersCollection.get(position);
        userViewHolder.textViewTitle.setText(userModel.getFullName());

        return convertView;
    }

    public void setUsersCollection(Collection<UserModel> usersCollection) {
        this.validateUsersCollection(usersCollection);
        this.usersCollection = (List<UserModel>) usersCollection;
        this.notifyDataSetChanged();
    }

    private void validateUsersCollection(Collection<UserModel> usersCollection) {
        if(usersCollection == null) {
            throw new IllegalArgumentException("The track list cannot be null");
        }
    }

    static class UserViewHolder {
        TextView textViewTitle;
    }
}
