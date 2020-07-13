package com.nature.thenature;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class InfoFragment extends Fragment {

    private TextView mDeveloper, mTitle, mPrivacyPolicy;
    private AdView mAdView;
    private FirebaseAuth mAuth;
    private Button logoutBtn, deleteAccountBtn;
    private ProgressBar progressBar;
    private static final String USERS = "Users";
    private DatabaseReference current_user_db_reference;
    private FirebaseAuth.AuthStateListener mAthListener;
    private ConstraintLayout layout;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        initVaribales(view);
        //Initialize adds
        InitAdds(view);
        mAuth = FirebaseAuth.getInstance();
        //get current user
        final FirebaseUser user = mAuth.getCurrentUser();
        final String uid = user.getUid();
        Log.e("uid", uid);
        mDeveloperClick();
        mPrivacyPolicyClick();
        logoutBtnClick();
        deleteAccountBtnClick(user);
        changeAuthState();

        return view;
    }

    private void changeAuthState() {
        // this listener will be called when there is change in firebase user session
        mAthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(getActivity(),SignUpPhoneActivity.class));

                    layout.removeAllViewsInLayout();

                }
            }
        };
    }

    private void deleteAccountBtnClick(final FirebaseUser user) {
        deleteAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setMessage("Are you sure want to delete your account permanently!");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressBar.setVisibility(View.VISIBLE);
                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Your profile is deleted:( Create a account now!", Toast.LENGTH_SHORT).show();

                                    mAuth.signOut();
                                    //to delete the database current user
//                                    current_user_db_reference= FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
//                                    current_user_db_reference.removeValue();
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    Toast.makeText(getContext(), "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    private void logoutBtnClick() {
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                mAuth.signOut();

            }
        });
    }

    private void InitAdds(View view) {
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void mPrivacyPolicyClick() {
        mPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PrivacyPolicyActivity.class));

            }
        });
    }

    private void mDeveloperClick() {
        mDeveloper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.instagram.com/___nature__lover/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage("com.instagram.android");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.instagram.com/___nature__lover/")));
                }
            }
        });
    }


    private void initVaribales(View view) {
        mDeveloper = view.findViewById(R.id.txtv_developer);
        mPrivacyPolicy = view.findViewById(R.id.txtv_Privacy_policy);
        logoutBtn = view.findViewById(R.id.logoutBtn);
        deleteAccountBtn = view.findViewById(R.id.deleteAccBtn);
        progressBar = view.findViewById(R.id.loading);
        layout = view.findViewById(R.id.FragInfo);


    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAthListener != null) {
            mAuth.removeAuthStateListener(mAthListener);
        }
    }
}