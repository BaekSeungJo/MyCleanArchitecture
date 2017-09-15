package com.example.presentation.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.presentation.R;
import com.example.presentation.internal.di.PerActivity;
import com.example.presentation.model.UserModel;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by plnc on 2017-06-27.
 */
@PerActivity
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onUserItemClicked(UserModel userModel);
    }

    private List<UserModel> usersCollection;
    private final LayoutInflater layoutInflater;

    @Inject
    public UsersAdapter(Context context) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.usersCollection = Collections.emptyList();
    }

    @Override
    public int getItemCount() {
        return (this.usersCollection != null) ? this.usersCollection.size() : 0;
    }

    @Override
    public UsersAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.layoutInflater.inflate(R.layout.row_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UsersAdapter.UserViewHolder holder, int position) {
        final UserModel userModel = this.usersCollection.get(position);
        holder.textViewTitle.setText(userModel.getFullName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UsersAdapter.this.onItemClickListener != null) {
                    UsersAdapter.this.onItemClickListener.onUserItemClicked(userModel);
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setUsersCollection(Collection<UserModel> usersCollection) {
        this.validateUsersCollection(usersCollection);
        this.usersCollection = (List<UserModel>) usersCollection;
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void validateUsersCollection(Collection<UserModel> usersCollection) {
        if(usersCollection == null) {
            throw new IllegalArgumentException("The list cannot be null");
        }
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title) TextView textViewTitle;

        public UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

//public class UsersAdapter extends BaseAdapter {
//
//    private List<UserModel> usersCollection;
//    private final LayoutInflater layoutInflater;
//
//    public UsersAdapter(Context context, Collection<UserModel> usersCollection) {
//        validateUsersCollection(usersCollection);
//        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        this.usersCollection =(List<UserModel>) usersCollection;
//    }
//
//    @Override
//    public int getCount() {
//        int count = 0;
//        if(usersCollection != null && !usersCollection.isEmpty()) {
//            count = usersCollection.size();
//        }
//        return count;
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return usersCollection.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        UserViewHolder userViewHolder;
//
//        if(convertView == null) {
//            convertView = layoutInflater.inflate(R.layout.row_user, parent, false);
//
//            userViewHolder = new UserViewHolder();
//            userViewHolder.textViewTitle = (TextView) convertView.findViewById(R.id.title);
//
//            convertView.setTag(userViewHolder);
//        } else {
//            userViewHolder = (UserViewHolder) convertView.getTag();
//        }
//
//        UserModel userModel = usersCollection.get(position);
//        userViewHolder.textViewTitle.setText(userModel.getFullName());
//
//        return convertView;
//    }
//
//    public void setUsersCollection(Collection<UserModel> usersCollection) {
//        this.validateUsersCollection(usersCollection);
//        this.usersCollection = (List<UserModel>) usersCollection;
//        this.notifyDataSetChanged();
//    }
//
//    private void validateUsersCollection(Collection<UserModel> usersCollection) {
//        if(usersCollection == null) {
//            throw new IllegalArgumentException("The track list cannot be null");
//        }
//    }
//
//    static class UserViewHolder {
//        TextView textViewTitle;
//    }
//}
