package com.example.mectow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Cartow_Question extends AppCompatActivity {
    Spinner towspinner, towspinner1, towspinner2;
    public static String towspinnerval,towspinner1val,towspinner2val,probtowtext,type;
    private Button findtowmechanic;
    EditText cartowprobtext;
    FirebaseAuth auth;
    private ProgressBar progressBar;
    private StorageReference storageReference= FirebaseStorage.getInstance().getReference();
    private DatabaseReference root= FirebaseDatabase.getInstance().getReference("Image");
    List<String> towlist = new ArrayList<String>();
    List<String> towlist2 = new ArrayList<String>();
    List<String> towlist3=new ArrayList<String>();
    Boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartow__question);
        ProgressDialog mDialog = new ProgressDialog(getApplicationContext());
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(false);
        progressBar=findViewById(R.id.progressbarimg);
        progressBar.setVisibility(View.VISIBLE);
        auth = FirebaseAuth.getInstance();
        cartowprobtext=(EditText)findViewById(R.id.cartowprobtext);
        findtowmechanic = (Button) findViewById(R.id.findcartow);
        findtowmechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                type="cartow";
                Mechanic_Questions.type = "";
                Intent intent=new Intent(Cartow_Question.this,user_MapsActivity.class);
                intent.putExtra("type","cartow");
                startActivity(intent);
                finishAffinity();
            }
        });
        towspinner = (Spinner) findViewById(R.id.cartowquestion_spinner);
        towspinner1 = (Spinner) findViewById(R.id.cartowquestion_spinner1);
        towspinner2 = (Spinner) findViewById(R.id.cartowquestion_spinner2);
        towlist.add("Choose your Vehicle");
        fetch_service();
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, towlist);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        towspinner.setAdapter(arrayAdapter1);
        towspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
                String ctg = adapterView.getItemAtPosition(i).toString();
                towlist2.clear();
                towlist2.add("Choose category");
                fetch_category(ctg);
                towspinnerval=ctg;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void submit_cartowComplaint(String spinnerval, String spinner1val, String problemdescription,Double lat,Double lng) {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Complaint").push();
        HashMap<String,String> data=new HashMap<>();
        data.put("service",spinnerval);
        data.put("Category",spinner1val);
        data.put("Subcategory",towspinner2val);
        data.put("Description",problemdescription);
        data.put("uid",auth.getUid());
        reference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Toast.makeText(Cartow_Question.this, "Complaint uploaded successful", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void fillSpinner() {
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, towlist2);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        towspinner1.setAdapter(arrayAdapter2);
        towspinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String ctg=parent.getItemAtPosition(position).toString();
                towlist3.clear();
                towlist3.add("Choose sub category");
                fetch_sub_category(ctg);
                towspinner1val=ctg;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    public void fillspinner2(){
        ArrayAdapter<String>arrayAdapter3=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,towlist3);
        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        towspinner2.setAdapter(arrayAdapter3);
        towspinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String ctg=parent.getItemAtPosition(position).toString();
                towspinner2val=ctg;

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();

    public void fetch_service(){
        FirebaseDatabase.getInstance().getReference("ProblemtowFix").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    check=true;
                    if (dataSnapshot.child("service").exists()){
                        if (!towlist.contains(dataSnapshot.child("service").getValue().toString())){

                            towlist.add(dataSnapshot.child("service").getValue().toString());
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
        FirebaseDatabase.getInstance().getReference("ProblemtowFix").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if (dataSnapshot.child("service").exists()){
                        if (dataSnapshot.child("service").getValue().toString().equals(category)){
                            if (dataSnapshot.child("category").exists()){
                                if (!towlist2.contains(dataSnapshot.child("category").getValue().toString())){
                                    towlist2.add(dataSnapshot.child("category").getValue().toString());
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
        FirebaseDatabase.getInstance().getReference("ProblemtowFix").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if (dataSnapshot.child("category").exists()){
                        if (dataSnapshot.child("category").getValue().toString().equals(sub_category)){
                            if (dataSnapshot.child("subcategory").exists()){
                                towlist3.add(dataSnapshot.child("subcategory").getValue().toString());
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
    }}