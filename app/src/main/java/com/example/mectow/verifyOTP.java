package com.example.mectow;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.jar.Attributes;

public class verifyOTP extends AppCompatActivity {

    private EditText inputotpcode1, inputotpcode2, inputotpcode3, inputotpcode4, inputotpcode5, inputotpcode6;
    private String verificationId, mobilenumber, _phonenumber, userid;
    ProgressBar progressBar;
    DatabaseReference reff, reference;
    FirebaseAuth auth;
    Button verifybutton;
    public String type,user_id;
    String name, email, phonenumber, password, confirm_password,uid;
    user User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_o_t_p);
        reff = FirebaseDatabase.getInstance().getReference("Customers");
        reference = FirebaseDatabase.getInstance().getReference("User");
        auth = FirebaseAuth.getInstance();
        mobilenumber = getIntent().getExtras().getString("mobile");
        if (getIntent().getExtras().getString("name") != null) {
            name = getIntent().getExtras().getString("name");
        }
        if (getIntent().getExtras().getString("email") != null) {
            email = getIntent().getExtras().getString("email");
        }
        if (getIntent().getExtras().getString("phonenumber") != null) {
            phonenumber = getIntent().getExtras().getString("phonenumber");

        }
        if (getIntent().getExtras().getString("type")!=null){
            type=getIntent().getExtras().getString("type");
        }
        if (getIntent().getExtras().getString("uid")!=null){
            user_id=getIntent().getExtras().getString("uid");
        }
        if(getIntent().getExtras().getString("uid")!=null){
            uid = getIntent().getExtras().getString("uid");
        }
        if(getIntent().getExtras().getString("password")!=null){
            password = getIntent().getExtras().getString("password");
        }

        _phonenumber = "+92" + mobilenumber;
        User = new user();
        inputotpcode1 = findViewById(R.id.inputotpcode1);
        inputotpcode2 = findViewById(R.id.inputotpcode2);
        inputotpcode3 = findViewById(R.id.inputotpcode3);
        inputotpcode4 = findViewById(R.id.inputotpcode4);
        inputotpcode5 = findViewById(R.id.inputotpcode5);
        inputotpcode6 = findViewById(R.id.inputotpcode6);
        setupOTPInput();
        progressBar = findViewById(R.id.progressbar1);
        verifybutton = findViewById(R.id.verifybutton);

        verificationId = getIntent().getStringExtra("verificationId");
        verifybutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (inputotpcode1.getText().toString().trim().isEmpty()
                        || inputotpcode2.getText().toString().trim().isEmpty()
                        || inputotpcode3.getText().toString().trim().isEmpty()
                        || inputotpcode4.getText().toString().trim().isEmpty()
                        || inputotpcode5.getText().toString().trim().isEmpty()
                        || inputotpcode6.getText().toString().trim().isEmpty()
                ) {
                    Toast.makeText(verifyOTP.this, "Please enter valid code", Toast.LENGTH_SHORT).show();
                    return;
                }
                String code = inputotpcode1.getText().toString() +
                        inputotpcode2.getText().toString() +
                        inputotpcode3.getText().toString() +
                        inputotpcode4.getText().toString() +
                        inputotpcode5.getText().toString() +
                        inputotpcode6.getText().toString();
                if (verificationId != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    verifybutton.setVisibility(View.INVISIBLE);
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                            verificationId,
                            code
                    );
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    verifybutton.setVisibility(View.VISIBLE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(verifyOTP.this, "successfull", Toast.LENGTH_SHORT).show();
                                        if (type.equals("normal")) {
                                            Intent intent = new Intent(verifyOTP.this, Email_verify.class);
                                            intent.putExtra("name", name);
                                            intent.putExtra("email", email);
                                            intent.putExtra("phonenumber", phonenumber);
                                            intent.putExtra("password",password);
                                            intent.putExtra("uid",uid);
                                            intent.putExtra("rating","5.0");
                                            startActivity(intent);
                                        }
                                        else {
                                            HashMap<String, String> user = new HashMap<>();
                                            user.put("name", name);
                                            user.put("email", email);
                                            user.put("phonenumber", phonenumber);
                                            user.put("rating","5.0");
//                                            reff.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                    Map<String, Object> lastObj = (Map<String, Object>) snapshot.getValue();
//                                                    int id = 0;
//                                                    if (lastObj.values().size() > 0) {
//                                                        Map<String, Object> last = (Map<String, Object>) lastObj.values().iterator().next();
//                                                        if (last.get("id") != null) {
//                                                            id = Integer.valueOf(last.get("ID").toString());
//                                                        }
//                                                    }
//
//                                                    int lastId = id + 1;
//                                                    user.put("ID", String.valueOf(lastId));
//                                                    Log.i("Insert User", user.toString());
                                                    reff.child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(getApplicationContext(), "Account created Successfully", Toast.LENGTH_LONG).show();
                                                                Intent intent=new Intent(verifyOTP.this,login.class);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    });
//                                                }
//
//                                                @Override
//                                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                                }
//                                            });

                                        }
                                    }
                                    else {
                                        Toast.makeText(verifyOTP.this, "The verification code was entered is invalid", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    findViewById(R.id.resenderotp).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                    "+92" + getIntent().getStringExtra("phonenumber"),
                                    60,
                                    TimeUnit.SECONDS,
                                    verifyOTP.this,
                                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                        @Override
                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                            Toast.makeText(verifyOTP.this, "successful", Toast.LENGTH_SHORT).show();
                                            //signup();
//                                            Intent intent = new Intent(verifyOTP.this,Email_verify.class);
//                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onVerificationFailed(@NonNull FirebaseException e) {

                                            Toast.makeText(verifyOTP.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onCodeSent(@NonNull String newverificationID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                            verificationId = newverificationID;
                                            Toast.makeText(verifyOTP.this, "OTP sent", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                            );
                        }
                    });
                }

            }
        });

    }

    private void setupOTPInput() {
        inputotpcode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputotpcode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        inputotpcode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputotpcode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        inputotpcode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputotpcode4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        inputotpcode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputotpcode5.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        inputotpcode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputotpcode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        inputotpcode6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    verifybutton.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    @Override
    public void onBackPressed() {

    }

}