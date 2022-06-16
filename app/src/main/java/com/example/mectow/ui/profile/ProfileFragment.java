package com.example.mectow.ui.profile;

import androidx.fragment.app.Fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mectow.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {

    CircleImageView profilpic;
    String _name,_phoneno;
    EditText update_name;
    private TextView updatephonenumber, textname,updateemail;
    private Button updatebtn;
    private Uri imageuri;
    String profilepicurl;
    private String uid;
    private static final String Customers = "customer";
    FirebaseAuth auth;
    //    private final int REQ=1;
//    private Bitmap bitmap=null;
    private StorageReference storageReference;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_nav_profile, container, false);
        Intent intent = getActivity().getIntent();
        textname = (TextView) root.findViewById(R.id.textname);
        update_name = (EditText) root.findViewById(R.id.updatename);
        updateemail=(TextView)root.findViewById(R.id.updateemail);
        profilpic = root.findViewById(R.id.profilpic);
        storageReference = FirebaseStorage.getInstance().getReference();
        updatephonenumber = (TextView) root.findViewById(R.id.updatephonenumber);
        updatebtn=(Button)root.findViewById(R.id.updatebtn);
        auth = FirebaseAuth.getInstance();
        uid = auth.getUid();
        database = FirebaseDatabase.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Customers");
        reference = database.getReference("Customers").child(uid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                update_name.setText(snapshot.child("name").getValue(String.class));
                String email = auth.getCurrentUser().getEmail();
                textname.setText(snapshot.child("name").getValue(String.class));
                updateemail.setText(email);
                updatephonenumber.setText(snapshot.child("phonenumber").getValue(String.class));
                if (snapshot.child("profileimage").exists()) {
                    Picasso.get().load(snapshot.child("profileimage").getValue().toString()).into(profilpic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        profilpic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // upload pic code here.....
                Intent galleryintent = new Intent();
                galleryintent.setAction(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent, 2);

                return false;
            }
        });
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageuri != null) {
                    SaveImageUrl(imageuri);
                }
            }
        });
        return root;
    }

    private void SaveImageUrl(Uri uri) {
        StorageReference fileref = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        profilepicurl = uri.toString();
                        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Customers").child(auth.getUid());
                        HashMap<String, Object> saveimage = new HashMap<>();
                        saveimage.put("profileimage", profilepicurl);
                        saveimage.put("name",update_name.getText().toString());
                        reff.updateChildren(saveimage).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });

    }

    private String getFileExtension(Uri mUri) {
        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageuri = data.getData();
            profilpic.setImageURI(imageuri);


        }
    }
}
