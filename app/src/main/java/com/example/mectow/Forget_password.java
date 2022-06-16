package com.example.mectow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forget_password extends AppCompatActivity {
private EditText forget_email;
private Button reset_passbtn;
private String email;
private ProgressBar progressBar;
FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        forget_email=(EditText)findViewById(R.id.forgotemail);
        reset_passbtn=(Button)findViewById(R.id.reset);
        progressBar=(ProgressBar) findViewById(R.id.progressbar);
        auth=FirebaseAuth.getInstance();
        reset_passbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=forget_email.getText().toString();
                if (email.isEmpty()){
                    Toast.makeText(Forget_password.this, "Please provide your email!", Toast.LENGTH_SHORT).show();
                }
                else{
                    forget_password();
                }
            }

            private void forget_password() {
                FirebaseAuth auth=FirebaseAuth.getInstance();
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(Forget_password.this, "check your email!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Forget_password.this,login.class));
                                    finish();
                                }
                                else {
                                    Toast.makeText(Forget_password.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    public void GoBackToLogin(View view) {
        Intent intent = new Intent(Forget_password.this, login.class);
        startActivity(intent);
    }
}