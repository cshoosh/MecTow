package com.example.mectow;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.concurrent.TimeUnit;
public class sendOTP extends AppCompatActivity {
    String name, email, phonenumber, password, confirm_password;
    Button getotpbtn;
    public String phno,uid;
    public String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_o_t_p);
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
        if(getIntent().getExtras().getString("password")!=null){
            password = getIntent().getExtras().getString("password");
        }
        if(getIntent().getExtras().getString("uid")!=null){
            uid = getIntent().getExtras().getString("uid");
        }
        if(getIntent().getExtras().getString("phonenumber")!=null){
            phonenumber = getIntent().getExtras().getString("phonenumber");
            phno=phonenumber.substring(1,(phonenumber.length()));
            inputMobile.setText("+92"+phno);
        }

        buttonGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputMobile.getText().toString().trim().isEmpty()) {
                    Toast.makeText(sendOTP.this, "Enter Mobile", Toast.LENGTH_SHORT).show();
                return;
            }

                progressBar.setVisibility(View.VISIBLE);
                buttonGetOTP.setVisibility(View.INVISIBLE);
                PhoneAuthProvider.getInstance().verifyPhoneNumber
                        (
                       "+92"+phno,
                        60,
                        TimeUnit.SECONDS,
                        sendOTP.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                progressBar.setVisibility(View.GONE);
                                buttonGetOTP.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                progressBar.setVisibility(View.GONE);
                                buttonGetOTP.setVisibility(View.VISIBLE);
                                Toast.makeText(sendOTP.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                progressBar.setVisibility(View.GONE);
                                buttonGetOTP.setVisibility(View.VISIBLE);
                                Intent intent=new Intent(getApplicationContext(),verifyOTP.class);
                                intent.putExtra("mobile",inputMobile.getText().toString());
                                intent.putExtra("verificationId",verificationId);
                                intent.putExtra("name" , name);
                                intent.putExtra("email" , email);
                                intent.putExtra("password" , password);
                                intent.putExtra("type",type);
                                intent.putExtra("uid",uid);
                                intent.putExtra("phonenumber" , phonenumber);
                                intent.putExtra("rating","5.0");
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

//    public void GoBackToSignup(View view) {
//        Intent intent = new Intent(sendOTP.this,signup.class);
//        startActivity(intent);
//    }
}