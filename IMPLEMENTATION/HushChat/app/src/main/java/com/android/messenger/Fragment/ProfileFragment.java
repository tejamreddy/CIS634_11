package com.android.messenger.Fragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.android.messenger.Adapter.DialogEditInfoAdapter;
import com.android.messenger.Adapter.DialogImageUploadAdapter;
import com.android.messenger.Helpers.CurrentUser;
import com.android.messenger.Helpers.DatabaseRef;
import com.android.messenger.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import adil.dev.lib.materialnumberpicker.dialog.NumberPickerDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {


       CircleImageView profileimageview;
    ImageView btnchoosephoto;
    private static TextView tv_name_details, tv_status_details, tv_username, tv_number_details;
    Button btn_edit_name, btn_edit_status,btnMsgDeleteTime;
    Context context;
    private final int PICK_IMAGE_REQUEST = 71;
    StorageReference ref;
    Button btnSignout;
    FirebaseAuth firebaseAuth;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        init(view);

        btnMsgDeleteTime.setText("Messages delete time in minutes . "+CurrentUser.userMsgDeleteTime);

        SetUserUpdatedInfo();

        if (!CurrentUser.userInfo.profileImageUrl.equals("default")) {
            Glide.with(context).load(CurrentUser.userInfo.profileImageUrl).placeholder(R.drawable.default_user_img).into(profileimageview);
        }


        btn_edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = DatabaseRef.dbref_users.child(CurrentUser.userInfo.uid).child("name");
                DialogEditInfoAdapter dialogEditInfoAdapter = new DialogEditInfoAdapter(context, "Update Name", CurrentUser.userInfo.name, databaseReference);
                dialogEditInfoAdapter.setCancelable(false);
                dialogEditInfoAdapter.setCanceledOnTouchOutside(false);
                dialogEditInfoAdapter.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialogEditInfoAdapter.show();
            }
        });


        btn_edit_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = DatabaseRef.dbref_users.child(CurrentUser.userInfo.uid).child("status");
                DialogEditInfoAdapter dialogEditInfoAdapter = new DialogEditInfoAdapter(context, "Update status", CurrentUser.userInfo.status, databaseReference);
                dialogEditInfoAdapter.setCancelable(false);
                dialogEditInfoAdapter.setCanceledOnTouchOutside(false);
                dialogEditInfoAdapter.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialogEditInfoAdapter.show();
            }
        });


        btnchoosephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int checkVal = context.checkCallingOrSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                if (checkVal == PackageManager.PERMISSION_GRANTED) {
                    chooseImage();
                } else {

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            123);
                }

            }
        });

        btnMsgDeleteTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                try {
                    NumberPickerDialog numberPickerDialog = new NumberPickerDialog(context, 1, 60, new NumberPickerDialog.NumberPickerCallBack() {
                        @Override
                        public void onSelectingValue(int value) {

                            DatabaseRef.dbref_MessagesDeleteTime.child(CurrentUser.userInfo.uid).setValue(value);
                            CurrentUser.userMsgDeleteTime = value;
                            btnMsgDeleteTime.setText("Messages delete time in minutes . "+CurrentUser.userMsgDeleteTime);
                            Toast.makeText(context, "Your all messages will be deleted after "+value+" minutes", Toast.LENGTH_LONG).show();


                        }
                    });
                    numberPickerDialog.show();
                }catch (Exception e){
                    Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });

        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Toast toast = Toast.makeText(context,"Signout successfully",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                getActivity().finishAffinity();
            }
        });

        return view;
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }


    public static void SetUserUpdatedInfo() {

        tv_username.setText(CurrentUser.userInfo.username);
        tv_name_details.setText(CurrentUser.userInfo.name);
        tv_status_details.setText(CurrentUser.userInfo.status);
        tv_number_details.setText(CurrentUser.userInfo.phone);


    }

    private void init(View view) {

        firebaseAuth = FirebaseAuth.getInstance();

        profileimageview = (CircleImageView) view.findViewById(R.id.profileimageview);
        btnchoosephoto = (ImageView) view.findViewById(R.id.btnchoosephoto);
        tv_name_details = (TextView) view.findViewById(R.id.tv_name_details);
        tv_username = (TextView) view.findViewById(R.id.tv_username);
        tv_name_details = (TextView) view.findViewById(R.id.tv_name_details);
        tv_status_details = (TextView) view.findViewById(R.id.tv_status_details);
        tv_number_details = (TextView) view.findViewById(R.id.tv_number_details);
        btn_edit_name = (Button) view.findViewById(R.id.btn_edit_name);
        btn_edit_status = (Button) view.findViewById(R.id.btn_edit_status);
        btnMsgDeleteTime = (Button)view.findViewById(R.id.btnMsgDeleteTime);
        btnSignout = (Button)view.findViewById(R.id.signout);

        context = getActivity();

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if (requestCode == 123) {

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                chooseImage();

            } else {

                Toast.makeText(context, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
            }

        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            uploadImage(filePath);

        }


    }


    private void uploadImage(Uri filePath) {

        if (filePath != null) {

            final DialogImageUploadAdapter dialogImageUploadAdapter = new DialogImageUploadAdapter(context);
            dialogImageUploadAdapter.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogImageUploadAdapter.setCancelable(false);
            dialogImageUploadAdapter.setCanceledOnTouchOutside(false);
            dialogImageUploadAdapter.show();

            ref = DatabaseRef.storageReference.child("images/" + CurrentUser.userInfo.uid);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;
                                    CurrentUser.userInfo.profileImageUrl = downloadUrl.toString();

                                    Glide.with(context)
                                            .load(CurrentUser.userInfo.profileImageUrl)
                                            .placeholder(R.drawable.default_user_img)
                                            .into(profileimageview);

                                    DatabaseRef.dbref_users.child(CurrentUser.userInfo.uid).child("profileImageUrl")
                                            .setValue(CurrentUser.userInfo.profileImageUrl);
                                }
                            });

                            dialogImageUploadAdapter.dismiss();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialogImageUploadAdapter.dismiss();
                            Toast.makeText(context, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            dialogImageUploadAdapter.txtProgress.setText((int) progress + "%");
                        }
                    });
        }
    }


}
