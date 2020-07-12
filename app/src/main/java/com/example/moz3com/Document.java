package com.example.moz3com;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moz3com.ForPrint.DeviceList;
import com.example.moz3com.ForPrint.PrinterCommands;
import com.example.moz3com.ForPrint.Utils;
import com.example.moz3com.PackageData.User2;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Document extends AppCompatActivity {
    TextView name,date;
    EditText mony,numshak,nameshak,bio,naony;
    String key,monytxt,numtxt,bank,biotxt,type,datetxt,n,fahed,d;
    RadioGroup radioGroup;
    Button ok;
    List<User2>user2s;
    Double f;
    private static BluetoothSocket btsocket;
    private static OutputStream outputStream;
    LinearLayout header1 , header2 , header3 , linearLayout;
    ImageView print;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        header1 = findViewById(R.id.header1);
        header2 = findViewById(R.id.header2);
        header3 = findViewById(R.id.header3);
        print = findViewById(R.id.print);
        linearLayout = findViewById(R.id.shak);
        name =findViewById(R.id.namecust);
        user2s =new ArrayList<>();
        radioGroup =findViewById(R.id.ra);
        mony=findViewById(R.id.many);
        naony =findViewById(R.id.manytxt);
        date =findViewById(R.id.daterep);
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        date.setText(simpleDateFormat.format(new Date()));
        d=simpleDateFormat.format(new Date());
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

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printDemo();
            }
        });
    }

    private void PushData() {
        Double s =Double.parseDouble(monytxt);
        Double x =f-s;
       if (type.isEmpty()){
           Toast.makeText(this, "قم بأخنيار نوع الدقع شيك ام نقد", Toast.LENGTH_SHORT).show();
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
           DatabaseReference databaseReference =FirebaseDatabase.getInstance().getReference("Catch Receipt").child(key).child(d);
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
    protected void printDemo() {
        if(btsocket == null){
            Intent BTIntent = new Intent(getApplicationContext(), DeviceList.class);
            this.startActivityForResult(BTIntent,DeviceList.REQUEST_CONNECT_BT);
        }
        else{
            OutputStream opstream = null;
            try {
                opstream = btsocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            outputStream = opstream;

            //print command
            try {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                outputStream = btsocket.getOutputStream();
                printPhotoFirst(header1);
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void printPhotoFirst(View view) {
        try {

            Bitmap bmp = getBitmapFromView(view);
            Bitmap b2 = Bitmap.createScaledBitmap(bmp , 550, 100 , false);
            if(b2!=null){
                byte[] command = Utils.decodeBitmap(b2);
                outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                printText(command);
            }else{
                //Toast.makeText(MainActivity.this , "Print Photo error"+"the file isn't exists" , Toast.LENGTH_LONG).show();
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(MainActivity.this,"PrintTools"+ "the file isn't exists" , Toast.LENGTH_LONG).show();
            Log.e("PrintTools", "the file isn't exists");
        }
        printPhotoFirst2(header2);
        printPhotoFirst2(header3);
    }

    private void printPhotoFirst2(View view) {
        try {

            Bitmap bmp = getBitmapFromView(view);
            Bitmap b2 = Bitmap.createScaledBitmap(bmp , 550, 100 , false);
            if(b2!=null){
                byte[] command = Utils.decodeBitmap(b2);
                outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                printText(command);
            }else{
                //Toast.makeText(MainActivity.this , "Print Photo error"+"the file isn't exists" , Toast.LENGTH_LONG).show();
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(MainActivity.this,"PrintTools"+ "the file isn't exists" , Toast.LENGTH_LONG).show();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

    private void printNewLine() {
        try {
            outputStream.write(PrinterCommands.FEED_LINE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //print byte[]
    private void printText(byte[] msg) {
        try {
            // Print normal text
            outputStream.write(msg);
            printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitma
        return returnedBitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            btsocket = DeviceList.getSocket();
            if(btsocket != null){
                printPhotoFirst(header1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}