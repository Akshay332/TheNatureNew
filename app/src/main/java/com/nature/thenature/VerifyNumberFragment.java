package com.nature.thenature;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class VerifyNumberFragment extends Fragment {

    private PinView pinView;
    private Button letsGoBtn;
    private ProgressBar progressBar;
    private String verificationId;
    private FirebaseAuth mAuth;
    private ConstraintLayout layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verify_number, container, false);
        initviews(view);

        mAuth = FirebaseAuth.getInstance();
        String phoneNumber = getArguments().getString("phone_Number");
        sendVerifiactionCode(phoneNumber);
        letsGoBtnClick();
        return view;
    }



    private void sendVerifiactionCode(String phoneNumber) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack

        );

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            progressBar.setVisibility(View.GONE);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                pinView.setText(code);
                progressBar.setVisibility(View.GONE);
//                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                   startActivity(new Intent(getActivity(),DashBoardActivity.class));
                  // getFragmentManager().beginTransaction().replace(R.id.fragVerify,new MainFragment()).commit();
                   layout.removeAllViews();

                } else {
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void letsGoBtnClick() {
            letsGoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String code = pinView.getText().toString().trim();
                    if (code.isEmpty() || code.length() < 6) {
                        pinView.setError("Enter code...");
                        pinView.requestFocus();
                        return;
                    }
                    verifyCode(code);
                }
            });
    }

    private void initviews(View view) {
        pinView = view.findViewById(R.id.pin_view);
        letsGoBtn = view.findViewById(R.id.btn_letsGo);
        progressBar = view.findViewById(R.id.loading);
        layout = view.findViewById(R.id.fragVerify);
    }


}