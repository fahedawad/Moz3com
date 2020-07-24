package com.example.moz3com;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Change extends AppCompatActivity {
    EditText itemtxt, pricesaletxt, pricebuytxt, coutitemtxt, counttxt,taxtxt,datetxt;
    Button push;
    TextView date;
    CircleImageView imgItem;
    StorageReference mStorageRef,storageReference;
    FirebaseStorage storage;
    String item, pricsale, pricbuy, getdate, countitem, count, type,tax;
    DatePickerDialog.OnDateSetListener dateSetListener;
    RadioGroup typeslae;
    Uri uri,geturi;
    Task<Uri> uriTask;
    HashMap<String,Object> hashMap;
    RadioButton Hyper,cartons,kilo;
    ProgressDialog progressDialog;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        key =getIntent().getStringExtra("itme");
        System.out.println(key);
        Hyper =findViewById(R.id.b);
        datetxt =findViewById(R.id.datetxt);
        datetxt.setEnabled(false);
        kilo =findViewById(R.id.k);
        progressDialog =new ProgressDialog(this);
        progressDialog.setMessage("جاري تحميل الصنف");
        cartons =findViewById(R.id.krton);
        storage =FirebaseStorage.getInstance();
        typeslae =findViewById(R.id.typesale);
        itemtxt =findViewById(R.id.item);
        pricebuytxt =findViewById(R.id.pricebuy);
        pricesaletxt =findViewById(R.id.pricesale);
        counttxt =findViewById(R.id.count);
        coutitemtxt =findViewById(R.id.itemcount);
        taxtxt =findViewById(R.id.tax);
        push =findViewById(R.id.pushdata);
        imgItem =findViewById(R.id.img);
        date =findViewById(R.id.date);
        imgItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent,""),1);
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar =Calendar.getInstance();
                int year =calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day =calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        Change.this,
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
                datetxt.setText(getdate);
            }
        };
        typeslae.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.k:
                        type = "كيلو";
                        break;
                    case R.id.b:
                        type = "فرط";
                        coutitemtxt.setVisibility(View.VISIBLE);
                        break;
                    case R.id.krton:
                        type = "كرتونه";
                }
            }
        });
        getData(key);
        push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item =itemtxt.getText().toString();
                pricbuy =pricebuytxt.getText().toString();
                pricsale =pricesaletxt.getText().toString();
                count =counttxt.getText().toString();
                countitem =coutitemtxt.getText().toString();
                tax =taxtxt.getText().toString();

                if (item.isEmpty()){
                    itemtxt.setError("أدخل اسم الصنف");
                }

                 if (pricsale.isEmpty())
                {
                    pricesaletxt.setError("أدخل سعر البيع");
                }
                 if (tax.isEmpty()){
                    taxtxt.setError("أدخل النسبة الضريبيه في حال كان الصنف من دون ضريبه ادخل 0");
                }
                 if (pricbuy.isEmpty()){
                    pricebuytxt.setError("أدخل سعر الشراء");
                }
                  if (count.isEmpty()){
                    counttxt.setError("أدخل العدد المتاح في مخزنك");
                }
                System.out.println((item.length()>0 && pricsale.length()>0 && tax.length()>0 && pricbuy.length()>0 && count.length()>0) + "         boolean");
                if(item.length()>0 && pricsale.length()>0 && tax.length()>0 && pricbuy.length()>0 && count.length()>0) {
                    progressDialog.show();
                    loadImg();
                }
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            uri = data.getData();
            imgItem.setImageURI(uri);
            System.out.println(uri + "              uri");
        }
    }
    public void loadImg(){
        if (uri!=null) {
            if (uri.toString().contains("content")){
                mStorageRef = storage.getReference("item");
            storageReference = mStorageRef.child(item);
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Change.this, "تم أختيار الصورة", Toast.LENGTH_SHORT).show();
                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        uriTask = storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                setData(datetxt.getText().toString(), item, pricsale, pricbuy, count, countitem, tax, type , task.getResult()+"");
                                progressDialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Change.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            });
        }
            else {
                setData(getdate, item, pricsale, pricbuy, count, countitem, tax, type , uri+"");
                progressDialog.dismiss();
            }
        }else  {
            progressDialog.dismiss();
            Toast.makeText(this, "اختار صورة من فضلك", Toast.LENGTH_SHORT).show();


        }

    }
   public void getData(String key){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("item").child(key);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemtxt.setText(dataSnapshot.getKey());
                itemtxt.setEnabled(false);
                pricesaletxt.setText(dataSnapshot.child("سعر البيع").getValue(String.class));
                taxtxt.setText(dataSnapshot.child("الضريبة").getValue(String.class));
                pricebuytxt.setText(dataSnapshot.child("سعر الشراء").getValue(String.class));
                counttxt.setText(dataSnapshot.child("العدد المتاح").getValue(String.class));
                datetxt.setText(dataSnapshot.child("تاريخ الانتهاء").getValue(String.class));
                getdate =datetxt.getText().toString()+"";
                coutitemtxt.setText(dataSnapshot.child("عدد الحبات داخل الكرتونه").getValue(String.class));
                String u = dataSnapshot.child("صورة المنتج").getValue(String.class);
                uri=Uri.parse(u);
                Picasso.get().load(u).resize(1080,1080).into(imgItem);
                String type =dataSnapshot.child("طريقة البيع").getValue(String.class);
                System.out.println(u);
                if (type.equals("فرط")){
                    Hyper.setChecked(true);
                }  if (type.equals("كيلو")){
                    kilo.setChecked(true);
                }if (type.equals("كرتونه")){
                    cartons.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void setData(final String getdate ,final  String item ,final  String  pricesale , final  String pricebuy , final  String count , final  String countitem,final  String tax, final  String type , String url) {
        try {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("item").child(item);
                    hashMap =new HashMap<>();
                    hashMap.put("تاريخ الانتهاء",getdate);
                    hashMap.put("سعر الشراء",pricebuy);
                    hashMap.put("سعر البيع",pricesale);
                    hashMap.put("العدد المتاح",count);
                    hashMap.put("عدد الحبات داخل الكرتونه",countitem);
                    hashMap.put("الضريبة",tax);
                    hashMap.put("طريقة البيع",type);
                    hashMap.put("صورة المنتج",url+"");
                    reference.updateChildren(hashMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            Toast.makeText(Change.this, "تم ادخال الصنف بنجاح", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    });
                    reference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println(e);
                        }
                    });
               }catch (NullPointerException e){}
    }

}