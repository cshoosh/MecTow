package com.example.mectow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class Email_verify extends AppCompatActivity {
    TextView emailverify;
    Button verifyemail;
    public static FirebaseAuth auth;
    private String userId;
    user User;
    String name, email, phonenumber, password, confirm_password,userid;
    DatabaseReference reff;
    private ProgressBar progressBar;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verify);
        emailverify = findViewById(R.id.emailverify);

        if (getIntent().getExtras().getString("name") != null) {
            name = getIntent().getExtras().getString("name");
        }
        if (getIntent().getExtras().getString("email") != null) {
            email = getIntent().getExtras().getString("email");
            emailverify.setText(email);
        }
        if (getIntent().getExtras().getString("phonenumber") != null) {
            phonenumber = getIntent().getExtras().getString("phonenumber");
        }
        if(getIntent().getExtras().getString("password")!=null){
            password = getIntent().getExtras().getString("password");
        }

        verifyemail = findViewById(R.id.verifyemail);
        auth = FirebaseAuth.getInstance();
        reff = FirebaseDatabase.getInstance().getReference("Customers");
        userId = auth.getCurrentUser().getUid();
        FirebaseUser user = auth.getCurrentUser();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        verifyemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                signup.auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Intent intent = new Intent(Email_verify.this,receive_verify_Email.class);
//                        startActivity(intent);
//                    }
//                });


                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String _userid=task.getResult().getUser().getUid();
                            savedata(_userid);
                            auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // signup();
                                        Log.d(TAG, "Email sent.");
                                        Intent intent = new Intent(Email_verify.this, receive_verify_Email.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(Email_verify.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(Email_verify.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    public void savedata(String id){
        HashMap<String, String> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("phonenumber", phonenumber);
        user.put("rating","5.0");
        FirebaseDatabase.getInstance()
                .getReference()
                .child("Customers")
                .orderByChild("ID")
                .limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Object> lastObj = (Map<String, Object>) snapshot.getValue();
                int ids = 0;
                if (lastObj != null) {
                    Map<String, Object> last = (Map<String, Object>) lastObj.values().iterator().next();
                    if (last.get("ID") != null) {
                        ids = Integer.valueOf(last.get("ID").toString());
                    }
                    Log.i("LAST_USER", last.toString());
                }
                int lastId = ids + 1;
                user.put("ID", String.valueOf(lastId));
                Log.i("Insert User", user.toString());
                reff.child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Account created Successfully", Toast.LENGTH_LONG).show();
                }
                }
               });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onBackPressed() {

    }
    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent
                            // after email is sent just logout the user and finish this activity
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(Email_verify.this, receive_verify_Email.class));
                            finish();
                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do

                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });
    }
}
