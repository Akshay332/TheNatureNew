package com.nature.thenature;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;


public class SignUpFragment extends Fragment {

    private EditText phone_edit_txt;
    private CountryCodePicker ccp;
    private Button nextBtn;
    private ProgressBar progressBar;
    private ConstraintLayout layout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        initviews(view);
        countrycodepicker();
        nextBtnClick();
        return view;
    }


    private void countrycodepicker() {
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                Toast.makeText(getContext(), ccp.getSelectedCountryName() + "selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void nextBtnClick() {
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = ccp.getSelectedCountryCode();
                Log.e("ccp",code);
                String number = phone_edit_txt.getText().toString().trim();
                if (number.isEmpty() || number.length() < 10) {
                    phone_edit_txt.setError("Valid number is required");
                    phone_edit_txt.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                String phoneNumber = "+" + code + number;
                Log.e("phone_number", phoneNumber);
                VerifyNumberFragment fragment = new VerifyNumberFragment();
                Bundle data = new Bundle();
                data.putString("phone_Number",phoneNumber);
                fragment.setArguments(data);
//                Intent intent = new Intent(getActivity(),VerifyNumberActivity.class);
//                intent.putExtra("phone_Number",phoneNumber);
//                startActivity(intent);
                getFragmentManager().beginTransaction().replace(R.id.fragSignup,fragment).commit();
                layout.removeAllViews();

            }
        });
    }

    private void initviews(View view) {
        phone_edit_txt = view.findViewById(R.id.edit_text_phone);
        ccp = view.findViewById(R.id.ccp);
        nextBtn = view.findViewById(R.id.btn_next);
        progressBar = view.findViewById(R.id.loading);
        layout = view.findViewById(R.id.fragSignup);




    }

    @Override
    public void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            getFragmentManager().beginTransaction().replace(R.id.fragSignup,new MainFragment()).commit();
            layout.removeAllViews();
//            Intent intent = new Intent(getActivity(), MainFragment.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
        }
    }
}