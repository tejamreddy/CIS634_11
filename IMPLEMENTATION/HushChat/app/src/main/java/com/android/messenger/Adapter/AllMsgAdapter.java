package com.android.messenger.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.messenger.Helpers.CurrentUser;
import com.android.messenger.Model.Message;
import com.android.messenger.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AllMsgAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private static final int VIEW_TYPE_MESSAGE_IMAGE_SENT = 3;
    private static final int VIEW_TYPE_MESSAGE_IMAGE_RECEIVED = 4;
    private static final int VIEW_TYPE_MESSAGE_VIDEO_SENT = 5;
    private static final int VIEW_TYPE_MESSAGE_VIDEO_RECEIVED = 6;
    private static final int VIEW_TYPE_GROUP_EVENT = 7;
    private static final int VIEW_TYPE_MESSAGE_GROUP_SENT = 8;
    private static final int VIEW_TYPE_MESSAGE_GROUP_RECEIVED = 9;
    private static final int VIEW_TYPE_MESSAGE_IMAGE_GROUP_SENT = 10;
    private static final int VIEW_TYPE_MESSAGE_IMAGE_GROUP_RECEIVED = 11;
    private static final int VIEW_TYPE_MESSAGE_VIDEO_GROUP_SENT = 12;
    private static final int VIEW_TYPE_MESSAGE_VIDEO_GROUP_RECEIVED = 13;


    private Context mContext;
    private List<Message> mMessageList;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    public AllMsgAdapter() {

    }

    public AllMsgAdapter(Context context, List<Message> messageList) {
        mContext = context;
        mMessageList = messageList;
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        Message message = (Message) mMessageList.get(position);
            if (message.type.equals("text") && message.senderUid.equals(CurrentUser.userInfo.uid)) {

                return VIEW_TYPE_MESSAGE_SENT;
            } else if (message.type.equals("text") && !(message.senderUid.equals(CurrentUser.userInfo.uid))) {

                return VIEW_TYPE_MESSAGE_RECEIVED;
            } else if (message.type.equals("image") && message.senderUid.equals(CurrentUser.userInfo.uid)) {

                return VIEW_TYPE_MESSAGE_IMAGE_SENT;
            } else if (message.type.equals("image") && !(message.senderUid.equals(CurrentUser.userInfo.uid))) {

                return VIEW_TYPE_MESSAGE_IMAGE_RECEIVED;
            } else if (message.type.equals("video") && message.senderUid.equals(CurrentUser.userInfo.uid)) {

                return VIEW_TYPE_MESSAGE_VIDEO_SENT;
            } else if (message.type.equals("video") && !(message.senderUid.equals(CurrentUser.userInfo.uid))) {

                return VIEW_TYPE_MESSAGE_VIDEO_RECEIVED;
            } else if (message.type.equals("groupevent")) {
                return VIEW_TYPE_GROUP_EVENT;
            }
            if (message.type.equals("grouptext") && message.senderUid.equals(CurrentUser.userInfo.uid)) {

                return VIEW_TYPE_MESSAGE_GROUP_SENT;
            } else if (message.type.equals("grouptext") && !(message.senderUid.equals(CurrentUser.userInfo.uid))) {

                return VIEW_TYPE_MESSAGE_GROUP_RECEIVED;
            } else if (message.type.equals("groupimage") && message.senderUid.equals(CurrentUser.userInfo.uid)) {

                return VIEW_TYPE_MESSAGE_IMAGE_GROUP_SENT;
            } else if (message.type.equals("groupimage") && !(message.senderUid.equals(CurrentUser.userInfo.uid))) {

                return VIEW_TYPE_MESSAGE_IMAGE_GROUP_RECEIVED;
            } else if (message.type.equals("groupvideo") && message.senderUid.equals(CurrentUser.userInfo.uid)) {

                return VIEW_TYPE_MESSAGE_VIDEO_GROUP_SENT;
            } else if (message.type.equals("groupvideo") && !(message.senderUid.equals(CurrentUser.userInfo.uid))) {

                return VIEW_TYPE_MESSAGE_VIDEO_GROUP_RECEIVED;
            }
        return 0;

    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

            if (viewType == VIEW_TYPE_MESSAGE_SENT) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_sent_message_text, parent, false);
                return new SentMessageHolder(view);
            } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_receive_message_text, parent, false);
                return new ReceivedMessageHolder(view);
            } else if (viewType == VIEW_TYPE_GROUP_EVENT) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_group_event, parent, false);
                return new GroupEventMessageHolder(view);
            }  else if (viewType == VIEW_TYPE_MESSAGE_IMAGE_SENT) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_image_sent, parent, false);
                return new ImageSentMessageHolder(view);
            } else if (viewType == VIEW_TYPE_MESSAGE_IMAGE_RECEIVED) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_image_received, parent, false);
                return new ImageReceivedMessageHolder(view);
            }
            if (viewType == VIEW_TYPE_MESSAGE_GROUP_SENT) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_text_group_sent, parent, false);
                return new GroupSentMessageHolder(view);
            } else if (viewType == VIEW_TYPE_MESSAGE_GROUP_RECEIVED) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_text_group_received, parent, false);
                return new GroupReceivedMessageHolder(view);
            }
        if (viewType == VIEW_TYPE_MESSAGE_IMAGE_GROUP_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_image_sent_group, parent, false);
            return new GroupImageSentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_IMAGE_GROUP_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_image_received_group, parent, false);
            return new GroupImageReceivedMessageHolder(view);
        }

//        else if (viewType == VIEW_TYPE_MESSAGE_VIDEO_SENT) {
//            view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.imagemsgscard, parent, false);
//            return new ImageSentMessageHolder(view);
//        } else if (viewType == VIEW_TYPE_MESSAGE_VIDEO_RECEIVED) {
//            view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.imagemsgcardreceived, parent, false);
//            return new ImageReceivedMessageHolder(view);
//        }


        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = (Message) mMessageList.get(position);

            switch (holder.getItemViewType()) {
                case VIEW_TYPE_MESSAGE_SENT:
                    ((SentMessageHolder) holder).bind(message);
                    break;
                case VIEW_TYPE_MESSAGE_RECEIVED:
                    ((ReceivedMessageHolder) holder).bind(message);
                    break;
                case VIEW_TYPE_MESSAGE_IMAGE_SENT:
                    ((ImageSentMessageHolder) holder).bind(message);
                    break;
                case VIEW_TYPE_MESSAGE_IMAGE_RECEIVED:
                    ((ImageReceivedMessageHolder) holder).bind(message);
                    break;
                case VIEW_TYPE_GROUP_EVENT:
                    ((GroupEventMessageHolder) holder).bind(message);
                    break;
                case VIEW_TYPE_MESSAGE_GROUP_SENT:
                    ((GroupSentMessageHolder) holder).bind(message);
                    break;
                case VIEW_TYPE_MESSAGE_GROUP_RECEIVED:
                    ((GroupReceivedMessageHolder) holder).bind(message);
                    break;
                case VIEW_TYPE_MESSAGE_IMAGE_GROUP_SENT:
                    ((GroupImageSentMessageHolder) holder).bind(message);
                    break;
                case VIEW_TYPE_MESSAGE_IMAGE_GROUP_RECEIVED:
                    ((GroupImageReceivedMessageHolder) holder).bind(message);
                    break;

            }

    }


    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView tv_text, tv_time;

        SentMessageHolder(View itemView) {
            super(itemView);

            tv_text = (TextView) itemView.findViewById(R.id.tv_text);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);

        }


        void bind(final Message message) {


            tv_text.setText(message.msg);
            String dateTime = sdf.format(new Date(message.timestamp));
            tv_time.setText(dateTime);

        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView tv_text, tv_time;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            tv_text = (TextView) itemView.findViewById(R.id.tv_text);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);


        }

        void bind(final Message message) {

            tv_text.setText(message.msg);
            String dateTime = sdf.format(new Date(message.timestamp));
            tv_time.setText(dateTime);


        }
    }

    private class ImageSentMessageHolder extends RecyclerView.ViewHolder {

        TextView tv_time;

        ImageSentMessageHolder(View itemView) {
            super(itemView);
            tv_time = (TextView)itemView.findViewById(R.id.tv_time);


        }

        void bind(final Message message) {

            String dateTime = sdf.format(new Date(message.timestamp));
            tv_time.setText(dateTime);

           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                 DialogShowImage dialogShowImage = new DialogShowImage(mContext, message.msg);
                    dialogShowImage.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogShowImage.show();
               }
           });

        }
    }

    private class ImageReceivedMessageHolder extends RecyclerView.ViewHolder {

        TextView tv_time;

        ImageReceivedMessageHolder(View itemView) {
            super(itemView);
            tv_time = (TextView)itemView.findViewById(R.id.tv_time);

        }

        void bind(final Message message) {


            String dateTime = sdf.format(new Date(message.timestamp));
            tv_time.setText(dateTime);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   DialogShowImage dialogShowImage = new DialogShowImage(mContext, message.msg);
                    dialogShowImage.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogShowImage.show();
                }
            });

        }
    }

    private class GroupEventMessageHolder extends RecyclerView.ViewHolder {
        TextView tv_groupevent;

        GroupEventMessageHolder(View itemView) {
            super(itemView);
            tv_groupevent = (TextView) itemView.findViewById(R.id.tv_groupevent);
        }

        void bind(final Message message) {

            tv_groupevent.setText(message.msg);
        }

    }

    private class GroupSentMessageHolder extends RecyclerView.ViewHolder {
        TextView tv_text, tv_time;

        GroupSentMessageHolder(View itemView) {
            super(itemView);

            tv_text = (TextView) itemView.findViewById(R.id.tv_text);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);

        }


        void bind(final Message message) {


            tv_text.setText(message.msg);
            String dateTime = sdf.format(new Date(message.timestamp));
            tv_time.setText(dateTime);

        }
    }

    private class GroupReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView tv_text, tv_time,tv_username;

        GroupReceivedMessageHolder(View itemView) {
            super(itemView);

            tv_text = (TextView) itemView.findViewById(R.id.tv_text);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_username = (TextView) itemView.findViewById(R.id.tv_username);



        }

        void bind(final Message message) {
            tv_username.setText(message.senderUsername);
            tv_text.setText(message.msg);
            String dateTime = sdf.format(new Date(message.timestamp));
            tv_time.setText(dateTime);


        }
    }

    private class GroupImageSentMessageHolder extends RecyclerView.ViewHolder {

        TextView tv_time;

        GroupImageSentMessageHolder(View itemView) {
            super(itemView);

            tv_time = (TextView)itemView.findViewById(R.id.tv_time);

        }

        void bind(final Message message) {
            String dateTime = sdf.format(new Date(message.timestamp));
            tv_time.setText(dateTime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogShowImage dialogShowImage = new DialogShowImage(mContext, message.msg);
                    dialogShowImage.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogShowImage.show();
                }
            });

        }
    }

    private class GroupImageReceivedMessageHolder extends RecyclerView.ViewHolder {

        TextView tv_username,tv_time;

        GroupImageReceivedMessageHolder(View itemView) {
            super(itemView);

            tv_username = (TextView)itemView.findViewById(R.id.tv_username);
            tv_time = (TextView)itemView.findViewById(R.id.tv_time);

        }

        void bind(final Message message) {

            String dateTime = sdf.format(new Date(message.timestamp));
            tv_time.setText(dateTime);

            tv_username.setText(message.senderUsername);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogShowImage dialogShowImage = new DialogShowImage(mContext, message.msg);
                    dialogShowImage.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogShowImage.show();
                }
            });

        }
    }


}



