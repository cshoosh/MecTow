package com.example.mectow;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mectow.ui.home.HomeFragment;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;

public class signup extends AppCompatActivity {
    String name, email, phonenumber, password, confirm_password,userid;
    EditText nametxt, emailtxt, phonetxt, passtxt, confirmpasstxt;
    Boolean istrue = true;
    Boolean checkuser=false;
    Boolean state=false;
    int maxid=0;
    public static FirebaseAuth auth;
    Button loginherebtn, registrationbtn;
    DatabaseReference reff,reference;
    user User;
    SignInButton signUpButton;
    GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    // [START declare_auth]
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signUpButton = findViewById(R.id.sign_up_button);
        mAuth = FirebaseAuth.getInstance();
        google_sigin();
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        nametxt = (EditText) findViewById(R.id.name);
        emailtxt = (EditText) findViewById(R.id.email);
        passtxt = (EditText) findViewById(R.id.password);
        confirmpasstxt = (EditText) findViewById(R.id.confirm_password);
        phonetxt = (EditText) findViewById(R.id.phonenumber);
        registrationbtn = (Button) findViewById(R.id.registration);
        User = new user();
        auth=FirebaseAuth.getInstance();
        reff = FirebaseDatabase.getInstance().getReference("User");
        reference= FirebaseDatabase.getInstance().getReference("User");
        initialization();
        loginherebtn = findViewById(R.id.loginhere);
        loginherebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup.this,login.class);
                startActivity(intent);
            }
        });
        registrationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent=new Intent(signup.this,sendOTP.class);
                //startActivity(intent);
               validateddata();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null) {
            //  open_main();
        }
    }
    private void open_main() {
        Toast.makeText(signup.this, "signup successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), sendOTP.class);
        intent.putExtra("name", name);
        intent.putExtra("email", email);
        intent.putExtra("phonenumber", phonenumber);
        intent.putExtra("password",password);
        intent.putExtra("type","normal");
        startActivity(intent);
        finish();
    }
    private void validateddata() {
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+[.]+[a-z]+";
        String phonePattern = "[0-9]{11}";
        String namepattern="[a-zA-Z]+";
        name = nametxt.getText().toString();
        email = emailtxt.getText().toString();
        phonenumber = phonetxt.getText().toString();
        password = passtxt.getText().toString();
        confirm_password = confirmpasstxt.getText().toString();
        if (!email.matches(checkEmail)) {
            emailtxt.setError("Invalid Email!");
            emailtxt.requestFocus();
        }
        else if (!password.equals(confirm_password)) {
            confirmpasstxt.setError("password not matched with confirm password");
            confirmpasstxt.requestFocus();
        }
        else if (email.isEmpty()) {
            emailtxt.setError("Field cannot be empty");
            emailtxt.requestFocus();
        }
        else if (name.isEmpty()) {
            nametxt.setError("Field cannot be empty");
            nametxt.requestFocus();
        }
        else if (name.length() >= 15) {
            nametxt.setError("Username too long");
            nametxt.requestFocus();
        }
        else if (password.isEmpty()) {
            passtxt.setError("Field cannot be empty");
            passtxt.requestFocus();
        }
        else if (!name.matches(namepattern)) {
            nametxt.setError("Please provide correct pattern(Aa to Zz)");
            nametxt.requestFocus();
        }
        else if (confirm_password.isEmpty()) {
            confirmpasstxt.setError("Field cannot be empty");
            confirmpasstxt.requestFocus();
        }
        else if (phonenumber.isEmpty()) {
            phonetxt.setError("Field cannot be empty");
            phonetxt.requestFocus();
        }
        else if (!phonenumber.matches(phonePattern)){
            phonetxt.setError("Pattern is incorrect");
            phonetxt.requestFocus();
        }
        else{
            //create_user();
            open_main();
        }
   }
    private void create_user() {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    upload_data(task.getResult().getUser().getUid());
                } else {
                    Toast.makeText(signup.this, "Error:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(signup.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void upload_data(String uid) {

        HashMap<String, String> user = new HashMap<>();
        user.put("key", uid);
        user.put("name", name);
        user.put("email", email);
        user.put("phone_number", phonenumber);
        user.put("password", password);
        user.put("confirm_password", confirm_password);

        reff.child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Toast.makeText(signup_mechanic.this, "user created", Toast.LENGTH_SHORT).show();
                    open_main();
                } else {
                    Toast.makeText(signup.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(signup.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void initialization() {
        nametxt = findViewById(R.id.name);
        emailtxt = findViewById(R.id.email);
        phonetxt = findViewById(R.id.phonenumber);
        passtxt = findViewById(R.id.password);
        confirmpasstxt = findViewById(R.id.confirm_password);
        registrationbtn = findViewById(R.id.registration);
        auth = FirebaseAuth.getInstance();
    }

    public void GoBackToLogin(View view) {
        Intent intent = new Intent(signup.this,login.class);
        startActivity(intent);
    }
    private void google_sigin() {
        // Configure Google Sign I
    }

    private void signUp() {
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
            String uid=user.getUid();
            String _email=user.getEmail();
            String _name=user.getDisplayName();
            Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), googleOtp.class);
            intent.putExtra("name", _name);
            intent.putExtra("email", _email);
            intent.putExtra("type","google");
            intent.putExtra("uid",uid);
            startActivity(intent);
            finish();

        }
        else {
            Toast.makeText(this, "Something Error", Toast.LENGTH_SHORT).show();
        }

    }

    public void ShowHidePass(View view) {
        if (passtxt.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
            ((ImageView) (view)).setImageResource(R.drawable.eye);

            //Show Password
            passtxt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            ((ImageView) (view)).setImageResource(R.drawable.invisible);

            //Hide Password
            passtxt.setTransformationMethod(PasswordTransformationMethod.getInstance());

        }
    }

    public void ShowHideCPass(View view) {
        if (confirmpasstxt.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
            ((ImageView) (view)).setImageResource(R.drawable.eye);
            //Show Password
            confirmpasstxt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            ((ImageView) (view)).setImageResource(R.drawable.invisible);
            //Hide Password
            confirmpasstxt.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }
}