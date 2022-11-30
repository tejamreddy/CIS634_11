package com.android.messenger.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.android.messenger.Helpers.CurrentUser;
import com.android.messenger.Model.User;
import com.android.messenger.R;
import com.android.messenger.Helpers.DatabaseRef;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.Calendar;
import java.util.TimeZone;


public class Splash extends AppCompatActivity {

    Context context;
    long timestamp;
    Calendar calendar;
    int userTimeToDeleteMsg;
    String uid="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;

        // this will run after 2 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                // check if the current user is loged in or not
                if(DatabaseRef.firebaseAuth.getCurrentUser() == null){

                    // if the user is not loged in then open the register activity
                    Intent intent_register = new Intent(context,Register.class);
                    startActivity(intent_register);
                    finish();
                }

                // if the user is logged in then check if the username available in the databse or not
                if(DatabaseRef.firebaseAuth.getCurrentUser() != null){

                    uid = DatabaseRef.firebaseAuth.getCurrentUser().getUid();




                    DatabaseRef.dbref_users.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){

                                // if the username exist then  open the dashboard
                                User user = dataSnapshot.getValue(User.class);
                                CurrentUser.setUser(user);

                                Intent intent_dashboard = new Intent(context,Dashboard.class);
                                startActivity(intent_dashboard);
                                finish();

                            }else{

                                // if the username not exist then open the username activity
                                Intent intent_username = new Intent(context,Username.class);
                                startActivity(intent_username);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });





                }

            }
        }, 2000);
    }
}
