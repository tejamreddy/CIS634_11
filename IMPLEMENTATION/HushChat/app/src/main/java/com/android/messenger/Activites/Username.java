package com.android.messenger.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.messenger.Helpers.CurrentUser;
import com.android.messenger.Model.User;
import com.android.messenger.R;
import com.android.messenger.Helpers.DatabaseRef;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class Username extends AppCompatActivity {


    // declare all the controls
    EditText edtUsername;
    Button btnusernamedone;
    String username,uid,phone;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);
        // cast all the controls
        init();

        // check if the username is available or not if available then save all the information
        // related to the user into the database
        btnusernamedone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = edtUsername.getText().toString().toLowerCase();
                if(username.length()<4){
                    edtUsername.setError("Please type atleast four characters");
                }else {

                    DatabaseRef.dbref_username.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // if the username already taken show the error
                                edtUsername.setError("This username is not available");
                            } else {

                                // else make object of the user class and save all the information into database
                                uid = DatabaseRef.firebaseAuth.getCurrentUser().getUid();
                                phone = DatabaseRef.firebaseAuth.getCurrentUser().getPhoneNumber();


                                User user = new User(username,uid,phone,username,"","default","Hey I'm using Hush !");
                                CurrentUser.setUser(user);

                                DatabaseRef.dbref_MessagesDeleteTime.child(CurrentUser.userInfo.uid).setValue(60);
                                CurrentUser.userMsgDeleteTime = 60;

                                DatabaseRef.dbref_username.child(username).setValue(uid);
                                DatabaseRef.dbref_users.child(uid).setValue(user);

                                // launch the dashboard intent
                                Intent intent_dashboard = new Intent(context,Dashboard.class);
                                startActivity(intent_dashboard);
                                finish();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
        });
    }

    // cast all the controls
    public void init(){
        edtUsername = (EditText)findViewById(R.id.edtUsername);
        btnusernamedone = (Button)findViewById(R.id.btnusernamedone);
        context = this;

    }
}
