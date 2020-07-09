package com.example.moz3com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

public class AllBillsForUser extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterSuper adapterSuper;
    String date,id,name,phon;
    TextView n,p,r;
    static ArrayList<itmeList> ncdlist;
    static ArrayList<String> datelist , uidlist;
    public ArrayList <List<itmeList>> list;
    String wieght;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_bills_for_user);
        recyclerView =findViewById(R.id.all);
        recyclerView.setHasFixedSize(true);
        r =findViewById(R.id.r);
        n =findViewById(R.id.n);
        p =findViewById(R.id.p);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        id =getIntent().getStringExtra("id");
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("dd-MM-yyyy");
        date =simpleDateFormat.format(new Date());
            datelist = new ArrayList<>();
            uidlist = new ArrayList<>();
            ncdlist = new ArrayList<>();
            list = new ArrayList<>();

        getitem();
        FirebaseDatabase.getInstance().getReference("user").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name =dataSnapshot.child("name").getValue(String.class);
                n.setText("الاسم:"+"\t"+"\t"+"\t"+dataSnapshot.child("name").getValue(String.class));
                p.setText("رقم الهاتف:"+"\t"+"\t"+"\t"+dataSnapshot.child("phon").getValue(String.class));
                try {
                    phon =dataSnapshot.child("phon").getValue(String.class);
                    phon =phon.substring(4,14);
                    System.out.println(phon);
                }catch (Exception e){}

                r.setText("الرصيد السابق:"+"\t"+"\t"+"\t"+dataSnapshot.child("رصيد السابق").getValue(Double.class)+"");
                System.out.println(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phon));
                if (intent.resolveActivity(getPackageManager())!=null){
                startActivity(intent);}
            }
        });
    }
    public void getitem (){

        FirebaseDatabase.getInstance().getReference("order").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()) // dates
                    {
                        ncdlist.clear();
                        for (DataSnapshot ds1 : ds.getChildren())//items
                        {
                            if (ds1.getKey().equals("طريقة الدفع")){ }
                            else {

                                Double i = Double.parseDouble(ds1.child("العدد").getValue(String.class));
                                Double total = Double.parseDouble(ds1.child("المجموع").getValue(String.class));
                                if (ds1.hasChild("الوزن")){
                                    wieght =ds1.child("الوزن").getValue(String.class);
                                }
                                else wieght ="--";
                                ncdlist.add(new itmeList(ds1.getKey(), ds1.child("السعر").getValue(String.class), i,ds.getKey(), name,id, total,  ds1.child("نوع البيع").getValue(String.class), total, total,wieght));
                            }
                        }
                        ArrayList<itmeList> ncdlist1 = new ArrayList<>();
                        ncdlist1.addAll(ncdlist);
                        list.add(ncdlist1);
                    }
                    adapterSuper = new AdapterSuper(AllBillsForUser.this,list);
                    recyclerView.setAdapter(adapterSuper);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}