package com.android.messenger.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.android.messenger.Activites.Chat;
import com.android.messenger.Model.User;
import com.android.messenger.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserSearchAdapter extends RecyclerView.Adapter<UserSearchAdapter.UserViewHolder> {

    public Context context;
    List<User> userList;

    public UserSearchAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, viewGroup, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder userViewHolder, int i) {
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
        TextView tv_username, tv_status;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            profileimageview = (CircleImageView) itemView.findViewById(R.id.profileimageview);
            tv_username = (TextView) itemView.findViewById(R.id.tv_username);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = userList.get(getAdapterPosition());

                    Intent intent_chat = new Intent(context, Chat.class);
                    intent_chat.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent_chat.putExtra("user", user);
                    context.startActivity(intent_chat);


                }
            });
        }

    }


    public void filterList(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

}


