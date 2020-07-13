package com.nature.thenature;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;

public class SignUpPhoneActivity extends AppCompatActivity {
    private EditText phone_edit_txt;
    private CountryCodePicker ccp;
    private Button nextBtn;
    private ProgressBar progressBar;
    private ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_phone);

        initviews();
        countrycodepicker();
        nextBtnClick();
    }
    private void countrycodepicker() {
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                Toast.makeText(getApplicationContext(), ccp.getSelectedCountryName() + "selected", Toast.LENGTH_SHORT).show();
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
                getSupportFragmentManager().beginTransaction().replace(R.id.layoutSignup,fragment).commit();

                layout.removeView(nextBtn);

            }
        });
    }
    private void initviews() {
        phone_edit_txt = findViewById(R.id.edit_text_phone);
        ccp = findViewById(R.id.ccp);
        nextBtn = findViewById(R.id.btn_next);
        progressBar = findViewById(R.id.loading);
        layout = findViewById(R.id.layoutSignup);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
           // getSupportFragmentManager().beginTransaction().replace(R.id.layoutSignup,new MainFragment()).commit();

            layout.removeView(nextBtn);
            Intent intent = new Intent(this, DashBoardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }
}