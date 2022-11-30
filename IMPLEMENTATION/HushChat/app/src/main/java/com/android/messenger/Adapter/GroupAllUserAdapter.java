package com.android.messenger.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.android.messenger.Activites.NewGroup;
import com.android.messenger.Model.User;
import com.android.messenger.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupAllUserAdapter extends RecyclerView.Adapter<GroupAllUserAdapter.UserViewHolder> {

    public Context context;
    List<User> userList;

    public GroupAllUserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public GroupAllUserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, viewGroup, false);
        return new GroupAllUserAdapter.UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GroupAllUserAdapter.UserViewHolder userViewHolder, int i) {
        final User user = userList.get(i);

        userViewHolder.tv_username.setText(user.username);
        userViewHolder.tv_status.setText(user.status);
        if(!user.profileImageUrl.equals("default")) {
            Glide.with(context).load(user.profileImageUrl).placeholder(R.drawable.default_user_img).into(userViewHolder.profileimageview);
        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profileimageview;
        TextView tv_username,tv_status;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            profileimageview = (CircleImageView) itemView.findViewById(R.id.profileimageview);
            tv_username = (TextView) itemView.findViewById(R.id.tv_username);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    User user = userList.get(getAdapterPosition());
                    userList.remove(getAdapterPosition());
                    NewGroup.filteredUserList.remove(user);
                    notifyDataSetChanged();

                        if (NewGroup.recyclerview_groupuserlist.getVisibility() == View.GONE) {
                            NewGroup.recyclerview_groupuserlist.setVisibility(View.VISIBLE);
                        }

                    NewGroup.selectedUserList.add(user);
                    NewGroup.groupUserAdapter.notifyDataSetChanged();


                }
            });

        }

    }


}



