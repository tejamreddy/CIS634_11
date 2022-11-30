package com.android.messenger.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.messenger.Fragment.ChatFragment;
import com.android.messenger.Fragment.InviteFragment;
import com.android.messenger.Fragment.ProfileFragment;
import com.android.messenger.R;

public class Dashboard  extends AppCompatActivity implements  ChatFragment.OnFragmentInteractionListener, InviteFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener {

    // declre all the controls to cast
    ImageView  imgchat, imginvite, imgprofile;
    TextView  txtchat, txtinvite, txtprofile;
    LinearLayout  lytchat, lytinvite, lytprofile;
    Context context;
    LinearLayout dynamicLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // cast all the controls
        init();




        //  by deafult the chat fragment will be selected
        androidx.fragment.app.FragmentManager fm = getSupportFragmentManager();
        ChatFragment fragment = new ChatFragment();
        fm.beginTransaction().add(R.id.dynamiclayout, fragment).commit();


        // set the chat fragment in clicking the chat button
        lytchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // set the active drawable for the chat and all the other drawables will be inactive
                Drawable top2 = getResources().getDrawable(R.drawable.messagesactive);
                imgchat.setImageDrawable(top2);
                txtchat.setTextColor(Color.parseColor("#006399"));


                Drawable top3 = getResources().getDrawable(R.drawable.invitenormal);
                imginvite.setImageDrawable(top3);
                txtinvite.setTextColor(Color.parseColor("#c2c2c2"));


                Drawable top4 = getResources().getDrawable(R.drawable.profilenormal);
                imgprofile.setImageDrawable(top4);
                txtprofile.setTextColor(Color.parseColor("#c2c2c2"));


                androidx.fragment.app.FragmentManager fm = getSupportFragmentManager();
                ChatFragment fragment = new ChatFragment();
                fm.beginTransaction().replace(R.id.dynamiclayout, fragment).commit();

            }
        });


        // set the invite fragement fby clicking the invite button
        lytinvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Drawable top2 = getResources().getDrawable(R.drawable.messagesnormal);
                imgchat.setImageDrawable(top2);
                txtchat.setTextColor(Color.parseColor("#c2c2c2"));


                Drawable top3 = getResources().getDrawable(R.drawable.inviteactive);
                imginvite.setImageDrawable(top3);
                txtinvite.setTextColor(Color.parseColor("#006399"));


                Drawable top4 = getResources().getDrawable(R.drawable.profilenormal);
                imgprofile.setImageDrawable(top4);
                txtprofile.setTextColor(Color.parseColor("#c2c2c2"));

                androidx.fragment.app.FragmentManager fm = getSupportFragmentManager();
                InviteFragment fragment = new InviteFragment();
                fm.beginTransaction().replace(R.id.dynamiclayout, fragment).commit();


            }
        });


        // set the profile fragment on clicking the profile button
        lytprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Drawable top2 = getResources().getDrawable(R.drawable.messagesnormal);
                imgchat.setImageDrawable(top2);
                txtchat.setTextColor(Color.parseColor("#c2c2c2"));


                Drawable top3 = getResources().getDrawable(R.drawable.invitenormal);
                imginvite.setImageDrawable(top3);
                txtinvite.setTextColor(Color.parseColor("#c2c2c2"));


                Drawable top4 = getResources().getDrawable(R.drawable.profileactive);
                imgprofile.setImageDrawable(top4);
                txtprofile.setTextColor(Color.parseColor("#006399"));

                androidx.fragment.app.FragmentManager fm = getSupportFragmentManager();
                ProfileFragment fragment = new ProfileFragment();
                fm.beginTransaction().replace(R.id.dynamiclayout, fragment).commit();

            }
        });








    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }



    // cast all the controls
    public void init(){


        context = this;


        imgchat = (ImageView) findViewById(R.id.imgchat);
        imginvite = (ImageView) findViewById(R.id.imginvite);
        imgprofile = (ImageView) findViewById(R.id.imgprofile);


        txtchat = (TextView) findViewById(R.id.txtchat);
        txtinvite = (TextView) findViewById(R.id.txtinvite);
        txtprofile = (TextView) findViewById(R.id.txtprofile);


        lytchat = (LinearLayout) findViewById(R.id.lytchat);
        lytinvite = (LinearLayout) findViewById(R.id.lytinvite);
        lytprofile = (LinearLayout) findViewById(R.id.lytprofile);


        dynamicLayout = (LinearLayout) findViewById(R.id.dynamiclayout);


    }
}
