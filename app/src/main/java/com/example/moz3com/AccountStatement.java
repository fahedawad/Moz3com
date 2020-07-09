package com.example.moz3com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moz3com.PackageAdapter.AccountAdapter;
import com.example.moz3com.PackageAdapter.AdapterKshf;
import com.example.moz3com.PackageData.AccountData;
import com.example.moz3com.PackageData.Kshf;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AccountStatement extends AppCompatActivity  {
       String id;
       RecyclerView recyclerView;
       RecyclerView df3atrec;
       AccountAdapter accountAdapter;
       AdapterKshf adapterKshf;
       List<Kshf>kshfs;
       TextView totalacc;
       List<AccountData>users;
       Double sum ,sum2,taxsum04,x,y,taxsum10,taxsum16,z,i,f,total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_statement);
        recyclerView =findViewById(R.id.Account);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        totalacc =findViewById(R.id.totalacc);
        users =new ArrayList<>();
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        taxsum04 = 0.0;
        taxsum10 = 0.0;
        taxsum16 = 0.0;
        sum = 0.0;
        sum2 = 0.0;
        x = 0.0;
        y = 0.0;
        z = 0.0;
      total =0.0;
        kshfs =new ArrayList<>();
        df3atrec =findViewById(R.id.hala);
        df3atrec.setItemAnimator(new DefaultItemAnimator());
        df3atrec.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager1 =new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        df3atrec.setLayoutManager(linearLayoutManager1);
      FirebaseDatabase.getInstance().getReference("order").child(getIntent().getStringExtra("id")).addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              for (DataSnapshot datesnap:dataSnapshot.getChildren()){
                 for (DataSnapshot item:datesnap.getChildren()){
                     id =datesnap.getKey();
                     if (item.getKey().equals("طريقة الدفع")) {
                     }else {
                         total = Double.parseDouble(item.child("المجموع").getValue(String.class));
                         sum = sum + total;
                         Double tax4 = Double.parseDouble(item.child("الضريبه").getValue(String.class));
                         if (tax4 == 0.04) {
                             taxsum04 = tax4 * total;
                             y += taxsum04;
                             System.out.println(y + "         y");

                         } else {
                         }

                         if (tax4 == 0.10) {
                             taxsum10 = tax4 * total;
                             x += taxsum10;
                         } else {

                         }
                         if (tax4 == 0.16) {
                             taxsum16 = tax4 * total;
                             z += taxsum16;

                         } else {

                         }

                         sum2 = y + x + z + sum;


                     }

                 }
                  users.add(new AccountData(id,sum2+""));
                  taxsum04 = 0.0;
                  taxsum10 = 0.0;
                  taxsum16 = 0.0;
                  sum = 0.0;
                  sum2 = 0.0;
                  x = 0.0;
                  y = 0.0;
                  z = 0.0;
                  total =0.0;
              }
              accountAdapter =new AccountAdapter(users,AccountStatement.this);
              recyclerView.setAdapter(accountAdapter);
          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
      });


            FirebaseDatabase.getInstance().getReference("Catch Receipt").child(getIntent().getStringExtra("id")).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        kshfs.clear();
                        kshfs.add(new Kshf(snapshot.child("المبلغ").getValue(String.class),snapshot.child("طرقة الدقع").getValue(String.class),snapshot.child("التاريخ").getValue(String.class)));
                        System.out.println(snapshot.child("التاريخ").getValue(String.class));
                        System.out.println(snapshot.child("المبلغ").getValue(String.class));

                    }
                    adapterKshf =new AdapterKshf(AccountStatement.this,kshfs);
                    df3atrec.setAdapter(adapterKshf);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }


}