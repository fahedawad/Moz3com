package com.example.moz3com;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moz3com.PackageData.User2;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Document extends AppCompatActivity {
    TextView name,date;
    LinearLayout linearLayout;
    EditText mony,numshak,nameshak,bio,naony;
    String key,monytxt,numtxt,bank,biotxt,type,datetxt,n,fahed;
    RadioGroup radioGroup;
    Button ok;
    List<User2>user2s;
    Double f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        name =findViewById(R.id.namecust);
        user2s =new ArrayList<>();
        linearLayout =findViewById(R.id.shak);
        radioGroup =findViewById(R.id.ra);
        mony=findViewById(R.id.many);
        naony =findViewById(R.id.manytxt);
        date =findViewById(R.id.daterep);
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        date.setText(simpleDateFormat.format(new Date()));
        datetxt =simpleDateFormat.format(new Date());
        nameshak =findViewById(R.id.nameshak);
        numshak =findViewById(R.id.numshak);
        bio =findViewById(R.id.bio);
        type ="";
        ok =findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monytxt =mony.getText().toString();
                numtxt =numshak.getText().toString();
                bank =nameshak.getText().toString();
                biotxt =bio.getText().toString();
                fahed =naony.getText().toString();
                if (mony.getText().toString().isEmpty()){
                    mony.setError("أدخل قيمة السند");
                }
                else if (bio.getText().toString().isEmpty()){
                    bio.setError("يجب ادخال معلزمات السند");
                }
               else if (fahed.isEmpty()){
                    naony.setError("أدخل المبلغ كتابة");
                }
                else PushData();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.cashrep:
                        type ="نقد";
                        break;
                    case R.id.shecrep:
                        type ="شيك";
                        linearLayout.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        key =getIntent().getStringExtra("id");
        FirebaseDatabase.getInstance().getReference("user").child(key)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        name.setText("استلمنا من السيد/السادة:"+"\t"+"\t"+dataSnapshot.child("name").getValue(String.class));
                        f =dataSnapshot.child("رصيد السابق").getValue(Double.class);
                        n = dataSnapshot.child("name").getValue(String.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void PushData() {
        Double s =Double.parseDouble(monytxt);
        Double x =f-s;
       if (type.isEmpty()){
           Toast.makeText(this, "قم بأخنيار نوع الدقع شيك ام نقد", Toast.LENGTH_SHORT).show();
       }
       else if (s>f){
           Toast.makeText(this, "قيمة الدفعة أكبر من الرصيد السابق", Toast.LENGTH_SHORT).show();
       }
       else {

           HashMap<String,Object>map =new HashMap<>();
           map.put("رصيد السابق",x);
           DatabaseReference reference= FirebaseDatabase.getInstance().getReference("user").child(key);
           reference.updateChildren(map, new DatabaseReference.CompletionListener() {
               @Override
               public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

               }
           });
           DatabaseReference databaseReference =FirebaseDatabase.getInstance().getReference("Catch Receipt").child(key);
           HashMap<String ,Object>hashMap =new HashMap<>();
           if (type.equals("نقد")){
               hashMap.put("المبلغ",monytxt);
               hashMap.put("التاريخ",datetxt);
               hashMap.put("الاسم",n);
               hashMap.put("مقابل",biotxt);
               hashMap.put("طرقة الدقع",type);
               hashMap.put("كتابة",fahed);
           }
           if (type.equals("شيك")){
               hashMap.put("المبلغ",monytxt);
               hashMap.put("التاريخ",datetxt);
               hashMap.put("الاسم",n);
               hashMap.put("مقابل",biotxt);
               hashMap.put("رقم السيك",numtxt);
               hashMap.put("البنك",bank);
               hashMap.put("طرقة الدقع",type);
               hashMap.put("كتابة",fahed);
           }
           databaseReference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
               @Override
               public void onSuccess(Void aVoid) {
                   Toast.makeText(Document.this,"تم تخزين هذا السند ",Toast.LENGTH_LONG).show();
               }
           });
       }
    }

    @Override
    public void onBackPressed() {
        user2s.clear();
        super.onBackPressed();

    }
}