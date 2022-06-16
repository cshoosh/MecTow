package com.example.mectow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.auth.viewmodel.RequestCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.Reference;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

public class Mechanic_Questions extends AppCompatActivity {

    Spinner spinner, spinner1, spinner2;
    public static String service_charges;
    public static String imageurl,spinner1val,spinner2val,spinnerval,problemdescription,type;
    private Button  findmechanic;
    EditText probtext;
    private Uri imageuri;
    private ProgressBar progressBar;
    private final int PICK_IMAGE_REQUEST = 71;
    //ImageView imagedisplay;
    LottieAnimationView imagedisplay;
    FirebaseAuth auth;
    FirebaseStorage storage;
    TextView charges;
    private StorageReference storageReference=FirebaseStorage.getInstance().getReference();
    private DatabaseReference root=FirebaseDatabase.getInstance().getReference("Image");
    List<String> list = new ArrayList<String>();
    List<String> list2 = new ArrayList<String>();
    List<String> list3 = new ArrayList<String>();

    Boolean check = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic__questions);
        ProgressDialog mDialog = new ProgressDialog(getApplicationContext());
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(false);
        auth = FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressbarimg);
        progressBar.setVisibility(View.VISIBLE);
        probtext=(EditText)findViewById(R.id.probtext);
        charges=findViewById(R.id.prices);
        findmechanic = (Button) findViewById(R.id.findmechanic);
        findmechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                problemdescription=probtext.getText().toString();
                if (imageuri != null){
                    uploadbtntofirebase(imageuri);
                }
                else{
                    Toast.makeText(Mechanic_Questions.this, "Please select image", Toast.LENGTH_SHORT).show();
                }
            }
        });
        imagedisplay = (LottieAnimationView) findViewById(R.id.probimg);
        imagedisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryintent=new Intent();
                galleryintent.setAction(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent,2);
            }
        });

        spinner = (Spinner) findViewById(R.id.question_spinner);
        spinner1 = (Spinner) findViewById(R.id.question_spinner1);
        spinner2 = (Spinner) findViewById(R.id.question_spinner2);
        list.add("Choose your Vehicle");
        fetch_service();
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
                String ctg = adapterView.getItemAtPosition(i).toString();
                list2.clear();
                list2.add("Choose category");
                fetch_category(ctg);
                spinnerval=ctg;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void submitComplaint(String spinnerval, String spinner1val, String spinner2val, String problemdescription, String imageurl) {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Complaint").push();
        HashMap<String,String> data=new HashMap<>();
        data.put("service",spinnerval);
        data.put("Category",spinner1val);
        data.put("Subcategory",spinner2val);
        data.put("Description",problemdescription);
        data.put("image",imageurl);
        data.put("uid",auth.getUid());
        reference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Toast.makeText(Mechanic_Questions.this, "Complaint uploaded successful", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    private void uploadbtntofirebase(Uri uri) {
        StorageReference fileref= storageReference.child(System.currentTimeMillis()+"."+getFileExtension(uri));
        fileref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        imageurl=uri.toString();
                        progressBar.setVisibility(View.GONE);
                        type="mechanic";
                        Cartow_Question.type = "";
                        //submitComplaint(spinnerval,spinner1val,spinner2val,problemdescription,imageurl);
                        Intent intent = new Intent(Mechanic_Questions.this, user_MapsActivity.class);
                        intent.putExtra("type","mechanic");
                        startActivity(intent);
                        finishAffinity();
                    }
                });
            }
        });

    }
    private String getFileExtension(Uri mUri){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageuri = data.getData();
            imagedisplay.setImageURI(imageuri);
        }
    }


    public void fillSpinner() {
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list2);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(arrayAdapter2);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String ctg=parent.getItemAtPosition(position).toString();
                list3.clear();
                list3.add("Choose sub category");
                fetch_sub_category(ctg);
                spinner1val=ctg;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    public void fillspinner2(){
        ArrayAdapter<String>arrayAdapter3=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list3);
        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(arrayAdapter3);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String ctg=parent.getItemAtPosition(position).toString();
                spinner2val=ctg;
                fetch_charges(ctg,spinner1val);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();

    public void fetch_service(){
        FirebaseDatabase.getInstance().getReference("ProblemFix").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    check=true;
                    if (dataSnapshot.child("service").exists()){
                        if (!list.contains(dataSnapshot.child("service").getValue().toString())){

                            list.add(dataSnapshot.child("service").getValue().toString());
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void fetch_category(String category ){
        FirebaseDatabase.getInstance().getReference("ProblemFix").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if (dataSnapshot.child("service").exists()){
                        if (dataSnapshot.child("service").getValue().toString().equals(category)){
                            if (dataSnapshot.child("category").exists()){
                                if (!list2.contains(dataSnapshot.child("category").getValue().toString())){
                                    list2.add(dataSnapshot.child("category").getValue().toString());
                                }
                            }
                        }
                    }
                }
                fillSpinner();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void fetch_sub_category(String sub_category){
        FirebaseDatabase.getInstance().getReference("ProblemFix").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if (dataSnapshot.child("category").exists()){
                        if (dataSnapshot.child("category").getValue().toString().equals(sub_category)){
                            if (dataSnapshot.child("subcategory").exists()){
                                list3.add(dataSnapshot.child("subcategory").getValue().toString());
                            }
                        }
                    }
                }
                fillspinner2();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void fetch_charges(String service,String sub_category){
        FirebaseDatabase.getInstance().getReference("ProblemFix").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if (dataSnapshot.child("category").exists()){
                        if (dataSnapshot.child("category").getValue().toString().equals(sub_category)){
                            if (dataSnapshot.child("subcategory").exists()){
                                if (dataSnapshot.child("subcategory").getValue().toString().equals(service)){
                                    service_charges=dataSnapshot.child("charges").getValue().toString();
                                    charges.setText(service_charges);
                                }
                            }
                        }
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}