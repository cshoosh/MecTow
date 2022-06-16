package com.example.mectow;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {

    TextView verticalTextView;
    Button signup, loginbtn, forgetbtn;
    EditText emailtxt, passwordtxt;
    private String email, password;
    boolean state = true;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private ProgressBar progressBar;
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    // [START declare_auth]
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(login.this);
        setContentView(R.layout.activity_login);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signInButton = findViewById(R.id.sign_in_button);
        mAuth = FirebaseAuth.getInstance();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        verticalTextView = (TextView) findViewById(R.id.login_text);
        verticalTextView.setRotation(90);

        forgetbtn = (Button) findViewById(R.id.forgot_password);
        forgetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, Forget_password.class);
                startActivity(intent);
            }
        });
        loginbtn = findViewById(R.id.login);
        signup = findViewById(R.id.signuphere);
        emailtxt = findViewById(R.id.login_email);
        passwordtxt = findViewById(R.id.login_password);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, signup.class);
                startActivity(intent);
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valid_data();
            }

            private void valid_data() {
                email = emailtxt.getText().toString();
                password = passwordtxt.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(login.this, "Please provide all fields", Toast.LENGTH_SHORT).show();
                } else {
                    loginuser();
                }
            }

            private void loginuser() {
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    if (auth.getCurrentUser().isEmailVerified()) {
                                        startActivity(new Intent(login.this, Home_Navigation.class));
                                        finish();
                                    }
                                } else {
                                    Toast.makeText(login.this, "Error:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(login.this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        testFunction();
    }

    private void testFunction() {
        String TAG = "TEST_";
        Log.i(TAG, "Start");
        Map<String, Object> user = new HashMap<>();

        reference
                .child("Customers")
                .orderByChild("ID")
                .limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Object> lastObj = (Map<String, Object>) snapshot.getValue();

                int ids = 0;
                if (lastObj != null) {
                    Log.i(TAG, "Has Values");
                    Map<String, Object> last = (Map<String, Object>) lastObj.values().iterator().next();
                    Log.i(TAG, last.toString());
                    if (last.get("ID") != null) {
                        ids = Integer.valueOf(last.get("ID").toString());
                    }
                }
                int lastId = ids + 1;
                user.put("ID", String.valueOf(lastId));
                user.put("name", "Test");

                Log.i(TAG, user.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            HashMap<String , String> userdata = new HashMap<>();
                            userdata.put("name" , user.getDisplayName());
                            userdata.put("email" , user.getEmail());
                            userdata.put("phonenumber" , user.getPhoneNumber());
                            userdata.put("rating","5.0");
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user!= null)
        {
           // Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(login.this,Home_Navigation.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Something Error", Toast.LENGTH_SHORT).show();
        }

    }

    public void ShowHidePass(View view) {
        if(view.getId()==R.id.show_pass_btn) {

            if (passwordtxt.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                ((ImageView) (view)).setImageResource(R.drawable.eye);

                //Show Password
                passwordtxt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                ((ImageView) (view)).setImageResource(R.drawable.invisible);

                //Hide Password
                passwordtxt.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }
}