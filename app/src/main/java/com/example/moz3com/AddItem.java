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
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddItem extends AppCompatActivity {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
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
                        AddItem.this,
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

                else if (pricsale.isEmpty())
                {
                    pricesaletxt.setError("أدخل سعر البيع");
                }
                else if (tax.isEmpty()){
                    taxtxt.setError("أدخل النسبة الضريبيه في حال كان الصنف من دون ضريبه ادخل 0");
                }
                else if (pricbuy.isEmpty()){
                    pricebuytxt.setError("أدخل سعر الشراء");
                }
                else  if (count.isEmpty()){
                    counttxt.setError("أدخل العدد المتاح في مخزنك");
                }


                else {
                    loadImg();
                    progressDialog.show();
                    Thread thread =new Thread(){
                        @Override
                        public void run() {
                            super.run();
                           try {
                               sleep(10000);
                               setData(getdate,item,pricsale,pricbuy,count,countitem,tax,type);
                           }

                           catch (Exception e){

                           }
                        }
                    };thread.start();
        }
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            uri = data.getData();
            imgItem.setImageURI(uri);
        }
    }
    public void loadImg(){
        if (uri!=null){
            mStorageRef =storage.getReference("item");
            storageReference =mStorageRef.child(item);
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddItem.this, "تم أختيار الصورة", Toast.LENGTH_SHORT).show();
                }
            });
        }else  {
            progressDialog.dismiss();
            Toast.makeText(this, "اختار صورة من فضلك", Toast.LENGTH_SHORT).show();


        }
    }
    public void setData(final  String getdate ,final  String item ,final  String  pricesale , final  String pricebuy , final  String count , final  String countitem,final  String tax, final  String type) {
        try {
        uriTask = storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                geturi = task.getResult();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("item").child(item);
                hashMap =new HashMap<>();
                hashMap.put("تاريخ الانتهاء",getdate);
                hashMap.put("سعر الشراء",pricebuy);
                hashMap.put("سعر البيع",pricesale);
                hashMap.put("العدد المتاح",count);
                hashMap.put("عدد الحبات داخل الكرتونه",countitem);
                hashMap.put("الضريبة",tax);
                hashMap.put("طريقة البيع",type);
                hashMap.put("صورة المنتج",geturi+"");
                reference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddItem.this, "تم ادخال الصنف بنجاح", Toast.LENGTH_SHORT).show();
                        itemtxt.setText("");
                        pricebuytxt.setText("");
                        pricesaletxt.setText("");
                        counttxt.setText("");
                        coutitemtxt.setText("");
                        coutitemtxt.setVisibility(View.GONE);
                        taxtxt.setText("");
                        date.setText("أضغط لاختيار التاريخ");
                        datetxt.setText("");
                        Hyper.setChecked(false);
                        cartons.setChecked(false);
                        kilo.setChecked(false);
                        progressDialog.dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println(e);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddItem.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });}catch (NullPointerException e){}
    }
}
