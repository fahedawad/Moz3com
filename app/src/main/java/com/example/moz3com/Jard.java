package com.example.moz3com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.example.moz3com.PackageAdapter.AdapterJard;
import com.example.moz3com.PackageData.Data;
import com.example.moz3com.PackageData.DataJard;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Jard extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterJard adapterJard;
    List<DataJard>list;
    String getdate;
    Double sum,total,sum2,y,z,x,sumcount,buy,sale,aval;
    DatePickerDialog.OnDateSetListener dateSetListener;
    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jard);
        list =new ArrayList<>();
        sumcount =0.0;
        sum = 0.0;
        sum2 = 0.0;
        x = 0.0;
        y = 0.0;
        z = 0.0;
        total = 0.0;
        recyclerView =findViewById(R.id.jard);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        findViewById(R.id.getdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar =Calendar.getInstance();
                int year =calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day =calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        Jard.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,year,month,day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();



            }

        });
        dateSetListener =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month =month+1;
                getdate =dayOfMonth +"-"+month+ "-"+year;
                System.out.println(getdate);
            }
        };
            findViewById(R.id.okpushdata).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 getData("04-07-2020");
                }
            });

    }
    public void getData(final String getdate1){
        FirebaseDatabase.getInstance().getReference("order")
               .addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       for (DataSnapshot id:dataSnapshot.getChildren()){
                           System.out.println(id.getKey());
                           for (DataSnapshot dsDate:id.getChildren()){
                               System.out.println(dsDate.getKey());
                               if (getdate1.equals("04-07-2020")){
                                   for (DataSnapshot snapshot:dsDate.getChildren()){
                                       if (snapshot.getKey().equals("طريقة الدفع")){

                                       }else {


//                                       total = Double.parseDouble(snapshot.child("المجموع").getValue(String.class));
                                           sum = sum + total;
//                                       Double tax4 = Double.parseDouble(snapshot.child("الضريبه").getValue(String.class));
//                                       if (tax4 == 0.04) {
//                                           double taxsum04 = tax4 * total;
//                                           y += taxsum04;
//                                           System.out.println(y + "         y");
//
//                                       } else {
//
//                                       }
//                                       if (tax4 == 0.10) {
//                                           double taxsum10 = tax4 * total;
//                                           x += taxsum10;
//
//                                       } else {
//
//                                       }
//                                       if (tax4 == 0.16) {
//                                           double taxsum16 = tax4 * total;
//                                           z += taxsum16;
//
//                                       } else {
//
//                                       }
                                           sum2 = y + x + z + sum;
//                                       Double count =Double.parseDouble(snapshot.child("العدد").getValue(String.class));
//                                       sumcount +=count;
                                           FirebaseDatabase.getInstance().getReference("item").child(snapshot.getKey())
                                                   .addValueEventListener(new ValueEventListener() {
                                                       @Override
                                                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                       date =dataSnapshot.child("تاريخ الانتهاء").getValue(String.class);
//                                                       aval =Double.parseDouble(dataSnapshot.child("العدد المتاح").getValue(String.class));
                                                       }

                                                       @Override
                                                       public void onCancelled(@NonNull DatabaseError databaseError) {

                                                       }
                                                   });
//                                        Double be3 =0.0;
//                                         be3 =Double.parseDouble(snapshot.child("سعر البيع").getValue(String.class));
//                                        Double shra2 =0.0;
//                                         shra2 =Double.parseDouble(snapshot.child("سعر الشراء").getValue(String.class));
                                           list.add(new DataJard(date,snapshot.getKey(),sum2,0.0,0.0,0.0
                                                   ,0.0,0.0));
                                           adapterJard =new AdapterJard(list,Jard.this);
                                           recyclerView.setAdapter(adapterJard);}
                                   }
                               }else System.out.println("error");
                           }
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });
    }
}