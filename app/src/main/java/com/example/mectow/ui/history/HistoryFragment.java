package com.example.mectow.ui.history;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mectow.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HistoryFragment extends Fragment {

    private HistoryViewModel historyViewModel;
    RecyclerView recyclerView;
    DatabaseReference mRef , databaseReference;
    ArrayList<HistoryModel> details;
    private HistoryAdapter adapter;
    FirebaseAuth auth;
    SearchView searchView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        historyViewModel =
                ViewModelProviders.of(this).get(HistoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_nav_notification, container, false);
        auth = FirebaseAuth.getInstance();
        recyclerView =root. findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        details = new ArrayList<>();

        mRef = FirebaseDatabase.getInstance().getReference("Complaint");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {

                    if(dataSnapshot1.child("uid").getValue().toString().equals(auth.getUid()))
                    {
                        HistoryModel cm = new HistoryModel();
                        cm.setTime(dataSnapshot1.child("time").getValue().toString());
                        cm.setCatagor(dataSnapshot1.child("Category").getValue().toString());
                        cm.setField(dataSnapshot1.child("Subcategory").getValue().toString());
                        cm.setService(dataSnapshot1.child("service").getValue().toString());
                        cm.setCash(dataSnapshot1.child("charges").getValue().toString());
                        details.add(cm);
                    }

                }
                Activity activity = (Activity) getActivity();
                adapter = new HistoryAdapter(details , activity);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


      /*  final TextView textView = root.findViewById(R.id.text_history);
        historyViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }
}
