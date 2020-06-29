package com.example.moz3com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.moz3com.PackageAdapter.AdapterSuper;
import com.example.moz3com.PackageData.itmeList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Bills extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterSuper adapterSuper;
    String date,id;
    static ArrayList<itmeList> ncdlist;
    public ArrayList <List<itmeList>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills);
        recyclerView =findViewById(R.id.re);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("dd-MM-yyyy");
        date =simpleDateFormat.format(new Date());
        ncdlist = new ArrayList<>();
        list = new ArrayList<>();
        getitem();
    }
    public void getitem (){

        FirebaseDatabase.getInstance().getReference("order").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (final DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        for (final DataSnapshot ds1 : ds.getChildren())//date
                        {
                            if (ds1.getKey().equals(date)){
                                FirebaseDatabase.getInstance().getReference("user").child(ds.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot ds2 : ds1.getChildren())//items
                                        {           if (ds2.getKey().equals("طريقة الدفع")){}
                                        else {
                                            int i = Integer.parseInt(ds2.child("العدد").getValue(String.class));
                                            Double total = Double.parseDouble(ds2.child("المجموع").getValue(String.class));
                                            ncdlist.add(new itmeList(ds2.getKey(), ds2.child("السعر").getValue(String.class), i, ds1.getKey(), dataSnapshot.child("name").getValue(String.class), ds.getKey(), total, ds1.child("نوع البيع").getValue(String.class), total, total));
                                        }
                                        }
                                        ArrayList<itmeList> ncdlist1 = new ArrayList<>();
                                        ncdlist1.addAll(ncdlist);
                                        list.add(ncdlist1);
                                        ncdlist.clear();
                                        adapterSuper = new AdapterSuper(Bills.this,list);
                                        recyclerView.setAdapter(adapterSuper);

                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}