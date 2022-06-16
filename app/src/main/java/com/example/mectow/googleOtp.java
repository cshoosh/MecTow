package com.example.mectow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class googleOtp extends AppCompatActivity {
    String name, email, phonenumber, password, confirm_password;
    Button getotpbtn;
    public String phno,type;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_otp);
        final TextView inputMobile=findViewById(R.id.inputmobile);
        final Button buttonGetOTP=findViewById(R.id.otpbutton);
        final ProgressBar progressBar=findViewById(R.id.progressbar);
        if(getIntent().getExtras().getString("name")!=null){
            name = getIntent().getExtras().getString("name");
        }
        if(getIntent().getExtras().getString("type")!=null){
            type = getIntent().getExtras().getString("type");
        }
        if(getIntent().getExtras().getString("email")!=null){
            email = getIntent().getExtras().getString("email");
        }
        if (getIntent().getExtras().getString("uid")!=null){
            user_id=getIntent().getExtras().getString("uid");
        }
        buttonGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputMobile.getText().toString().trim().isEmpty()) {
                    Toast.makeText(googleOtp.this, "Enter Mobile", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    String phone_no = inputMobile.getText().toString();
                    phno = phone_no.substring(1, (phone_no.length()));
                }
                progressBar.setVisibility(View.VISIBLE);
                buttonGetOTP.setVisibility(View.INVISIBLE);
                PhoneAuthProvider.getInstance().verifyPhoneNumber
                        (
                                "+92" + phno,
                                60,
                                TimeUnit.SECONDS,
                                googleOtp.this,
                                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                        progressBar.setVisibility(View.GONE);
                                        buttonGetOTP.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        progressBar.setVisibility(View.GONE);
                                        buttonGetOTP.setVisibility(View.VISIBLE);
                                        Toast.makeText(googleOtp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        progressBar.setVisibility(View.GONE);
                                        buttonGetOTP.setVisibility(View.VISIBLE);
                                        Intent intent = new Intent(getApplicationContext(), verifyOTP.class);
                                        intent.putExtra("mobile", inputMobile.getText().toString());
                                        intent.putExtra("verificationId", verificationId);
                                        intent.putExtra("name", name);
                                        intent.putExtra("email", email);
                                        intent.putExtra("type", type);
                                        intent.putExtra("phonenumber", phno);
                                        intent.putExtra("uid", user_id);
                                        startActivity(intent);

                                    }
                                }
                                );
            }
        });
    }
    @Override
    public void onBackPressed() {
    }
}
