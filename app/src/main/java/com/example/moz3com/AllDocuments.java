package com.example.moz3com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.moz3com.PackageAdapter.AdapterDoc;
import com.example.moz3com.PackageData.Doc;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllDocuments extends AppCompatActivity {
    AdapterDoc adapterDoc;
    List<Doc>docs;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_documents);
        recyclerView =findViewById(R.id.all);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        docs =new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Catch Receipt").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        for (DataSnapshot s1:snapshot.getChildren()){
                            if (snapshot.child("طرقة الدقع").getValue(String.class).equals("شيك")){
                                docs.add(new Doc(s1.child("الاسم").getValue(String.class),s1.child("التاريخ").getValue(String.class),
                                        s1.child("المبلغ").getValue(String.class),s1.child("كتابة").getValue(String.class),s1.child("رقم السيك").getValue(String.class),
                                        s1.child("البنك").getValue(String.class),s1.child("مقابل").getValue(String.class),s1.child("طرقة الدقع").getValue(String.class)));
                            }
                            if (snapshot.child("طرقة الدقع").getValue(String.class).equals("نقد")){
                                docs.add(new Doc(s1.child("الاسم").getValue(String.class),s1.child("التاريخ").getValue(String.class),
                                        s1.child("المبلغ").getValue(String.class),s1.child("كتابة").getValue(String.class),"-----------","-------------",s1.child("مقابل").getValue(String.class),s1.child("طرقة الدقع").getValue(String.class)));
                            }
                        }

                    }
                    adapterDoc =new AdapterDoc(docs,AllDocuments.this);

                    recyclerView.setAdapter(adapterDoc);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}