package com.android.messenger.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.android.messenger.Activites.NewGroup;
import com.android.messenger.Model.User;
import com.android.messenger.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupUserAdapter extends RecyclerView.Adapter<GroupUserAdapter.UserViewHolder> {

public Context context;
        List<User> userList;

public GroupUserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
        }

@NonNull
@Override
public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_group_user, viewGroup, false);
        return new UserViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull final UserViewHolder userViewHolder, int i) {
final User user = userList.get(i);

        userViewHolder.tv_username.setText(user.username);
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
    TextView tv_username;
    Button btn_removeuser;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        profileimageview = (CircleImageView) itemView.findViewById(R.id.profileimageview);
        tv_username = (TextView) itemView.findViewById(R.id.tv_username);
        btn_removeuser = (Button) itemView.findViewById(R.id.btn_removeuser);


        btn_removeuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user = userList.get(getAdapterPosition());
                userList.remove(getAdapterPosition());
                NewGroup.selectedUserList.remove(user);
                notifyDataSetChanged();


                if(userList.size() < 1) {
                    if (NewGroup.recyclerview_groupuserlist.getVisibility() == View.VISIBLE) {
                        NewGroup.recyclerview_groupuserlist.setVisibility(View.GONE);
                    }
                }



                    if (NewGroup.layout_noResult.getVisibility() == View.VISIBLE) {
                        NewGroup.layout_noResult.setVisibility(View.GONE);
                    }



                NewGroup.filteredUserList.add(user);
                NewGroup.recyclerview_alluserlist.setAdapter( NewGroup.groupAllUserAdapter);
                NewGroup.groupAllUserAdapter.notifyDataSetChanged();


            }
        });

    }

}


}


