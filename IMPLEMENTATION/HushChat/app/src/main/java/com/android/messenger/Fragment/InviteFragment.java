package com.android.messenger.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android.messenger.Helpers.CurrentUser;
import com.android.messenger.R;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;


public class InviteFragment extends Fragment {


    TextView txtusername;
    CircleImageView profileimageview;
    Context context;
    Button btninvitefriends,btnshare;

    public InviteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_invite, container, false);
        init(view);

        txtusername.setText(CurrentUser.userInfo.username);

        if(!CurrentUser.userInfo.profileImageUrl.equals("default")) {
            Glide.with(context).load(CurrentUser.userInfo.profileImageUrl).placeholder(R.drawable.default_user_img).into(profileimageview);
        }


        btninvitefriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey! I've been using the Hush chat and social app. It's fun and private too. Download Hush app for free and search for my username: "+CurrentUser.userInfo.username);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);


            }
        });


        btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                ApplicationInfo app = context.getApplicationInfo();
                String filePath = app.sourceDir;

                Intent intent = new Intent(Intent.ACTION_SEND);

                // MIME of .apk is "application/vnd.android.package-archive".
                // but Bluetooth does not accept this. Let's use "*/*" instead.
                intent.setType("*/*");


                // Append file and send Intent
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
                startActivity(Intent.createChooser(intent, "Share app via"));

            }
        });


        return view;
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }



    private void init(View view){
        txtusername = (TextView)view.findViewById(R.id.txtusername);
        profileimageview = (CircleImageView)view.findViewById(R.id.profileimageview);
        context = getActivity();
        btninvitefriends = (Button)view.findViewById(R.id.btninvitefriends);
        btnshare = (Button)view.findViewById(R.id.btnshare);


    }
}
