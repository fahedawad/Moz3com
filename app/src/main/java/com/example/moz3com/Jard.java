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
import android.widget.Button;
import android.widget.DatePicker;

import com.example.moz3com.PackageAdapter.AdapterJard;
import com.example.moz3com.PackageData.Data;
import com.example.moz3com.PackageData.DataJard;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Jard extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterJard adapterJard;
    List<DataJard>list;
    String getdate;
    Double sum,total,sum2,y,z,x,sumcount,buy,sale,aval;
    DatePickerDialog.OnDateSetListener dateSetListener;
    String date;
    Calendar calendar ;
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
        calendar = Calendar.getInstance();
        Date currentdate = new Date();
        calendar.setTime(currentdate);
        final Button date = findViewById(R.id.getdate);
         date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Jard.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, month, day);
                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                String dateString = formatter.format(calendar.getTime());
                                date.setText(dateString);
                            }
                        },calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }

        });
            findViewById(R.id.okpushdata).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 getData(date.getText().toString());
                }
            });
    }
    public void getData(final String getdate){

        FirebaseDatabase.getInstance().getReference("jard").child(getdate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    list.add(new DataJard(getdate , ds.getKey() , ds.child("المجموع").getValue(String.class)+"",""+ds.child("سعر البيع").getValue(String.class),""+ds.child("سعر البيع").getValue(String.class),""+ds.child("العدد").getValue(String.class),
                            ""+ds.child("الربح").getValue(String.class),""+ds.child("العدد المتاح").getValue(String.class)));
                }
                adapterJard =new AdapterJard(list,Jard.this);
                recyclerView.setAdapter(adapterJard);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}