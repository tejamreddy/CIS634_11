package com.android.messenger.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.messenger.Helpers.CurrentUser;
import com.android.messenger.Model.User;
import com.android.messenger.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import static com.firebase.ui.auth.AuthUI.getInstance;
import com.android.messenger.Helpers.DatabaseRef;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    // declare all the controls
    Button btnsignup;
    Button btnlogin;
    int RC_SIGN_UP=1019;
    int RC_LOG_IN=3414;
    String phonenumber="";
    List<AuthUI.IdpConfig> providers;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // cast all the controls
        init();



        // make an array of the authentication method that we want to use
        // in this case we'll use only phonenumvber authentication

        providers = Arrays.asList(

                new AuthUI.IdpConfig.PhoneBuilder().build()
        );

        // initialize this app to the firebase auth
        FirebaseApp.initializeApp(this);


        // request for the user interface for the signup
        // the user interface can be change using the styles
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivityForResult(
                        getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .setTheme(R.style.LoginTheme)
                                .build(),RC_SIGN_UP );
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivityForResult(
                        getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .setTheme(R.style.LoginTheme)
                                .build(),RC_LOG_IN );
            }
        });






    }

    // cast all the controls
    private void init(){
        btnsignup=(Button)findViewById(R.id.btnsignup);
        btnlogin=(Button)findViewById(R.id.btnlogin);
        context=this;
    }




    // this will run after the messege received and no we have to check the response
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_UP) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                // get the current user auth and phone number
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                phonenumber=user.getPhoneNumber().toString();

                DatabaseRef.dbref_users.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){



                            // if the user already exist then open the dashboard
                            User user = dataSnapshot.getValue(User.class);
                            CurrentUser.setUser(user);


                            DatabaseRef.dbref_MessagesDeleteTime.child(CurrentUser.userInfo.uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        int timetoDelete = dataSnapshot.getValue(Integer.class);
                                        CurrentUser.userMsgDeleteTime = timetoDelete;
                                        Intent intent_dashboard = new Intent(context,Dashboard.class);
                                        startActivity(intent_dashboard);
                                        finish();
                                    }else{
                                        DatabaseRef.dbref_MessagesDeleteTime.child(CurrentUser.userInfo.uid).setValue(60);
                                        CurrentUser.userMsgDeleteTime = 60;
                                        Intent intent_dashboard = new Intent(context,Dashboard.class);
                                        startActivity(intent_dashboard);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }else{

                            // if the user does not exist then open the username activity and ask user to choose a username
                            Intent intent_username = new Intent(context,Username.class);
                            startActivity(intent_username);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.

            }
        }

        //  the same process for this button as for signup
        if (requestCode == RC_LOG_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                phonenumber=user.getPhoneNumber().toString();
                DatabaseRef.dbref_users.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){

                            User user = dataSnapshot.getValue(User.class);
                            CurrentUser.setUser(user);

                            Intent intent_dashboard = new Intent(context,Dashboard.class);
                            startActivity(intent_dashboard);
                            finish();
                        }else{
                            Intent intent_username = new Intent(context,Username.class);
                            startActivity(intent_username);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.

            }
        }
    }


    // exist the app if backbutton pressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
