package com.example.moz3com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.moz3com.ForPrint.DeviceList;
import com.example.moz3com.ForPrint.PrinterCommands;
import com.example.moz3com.ForPrint.Utils;
import com.example.moz3com.PackageAdapter.AdapterJard;
import com.example.moz3com.PackageData.Data;
import com.example.moz3com.PackageData.DataJard;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.OutputStream;
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
    LinearLayout printlayout;
    private static BluetoothSocket btsocket;
    private static OutputStream outputStream;
    ImageView print;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jard);
        printlayout = findViewById(R.id.printlayout);
        list =new ArrayList<>();
        sumcount =0.0;
        sum = 0.0;
        sum2 = 0.0;
        x = 0.0;
        y = 0.0;
        z = 0.0;
        total = 0.0;
        print = findViewById(R.id.print);
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

            print.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    printDemo();
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
                printPhoto();
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
//    public void printPhoto2() {
//        try {
//            //printPhotoFirst();
//            int size = recyclerView.getAdapter().getItemCount();
//            recyclerView.smoothScrollToPosition(0);
//            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//            for (int i = 0 ; i<size ; i++){
//                System.out.println(i + "                    i");
//                Bitmap bmp = getBitmapFromView(recyclerView.getChildAt(i));
//                Bitmap b2 = Bitmap.createScaledBitmap(bmp , 550, 100 , false);
//                if(b2!=null){
//                    byte[] command = Utils.decodeBitmap(b2);
//                    System.out.println(command + "          command");
//                    //outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
//                    //printText(command);
//                }else{
//                    //Toast.makeText(MainActivity.this , "Print Photo error"+"the file isn't exists" , Toast.LENGTH_LONG).show();
//                    Log.e("Print Photo error", "the file isn't exists");
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            //Toast.makeText(MainActivity.this,"PrintTools"+ "the file isn't exists" , Toast.LENGTH_LONG).show();
//            Log.e("PrintTools", "the file isn't exists");
//        }
//    }

    public void printPhoto() {
        try {
            //printPhotoFirst();
            int size = recyclerView.getAdapter().getItemCount();
                for (int i = 0 ; i<size ; i++){
                    System.out.println(i + "                    i");
                    Bitmap bmp = getBitmapFromView(recyclerView.getChildAt(i));
                    Bitmap b2 = Bitmap.createScaledBitmap(bmp , 550, 100 , false);
                    if(b2!=null){
                        byte[] command = Utils.decodeBitmap(b2);
                        outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                        printText(command);
                    }else{
                        //Toast.makeText(MainActivity.this , "Print Photo error"+"the file isn't exists" , Toast.LENGTH_LONG).show();
                        Log.e("Print Photo error", "the file isn't exists");
                    }

                }


        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(MainActivity.this,"PrintTools"+ "the file isn't exists" , Toast.LENGTH_LONG).show();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

    private void printPhotoFirst() {
        try {

                Bitmap bmp = getBitmapFromView(printlayout);
                Bitmap b2 = Bitmap.createScaledBitmap(bmp , 550, 100 , false);
                if(b2!=null){
                    byte[] command = Utils.decodeBitmap(b2);
                    outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    printText(command);
                }else{
                    //Toast.makeText(MainActivity.this , "Print Photo error"+"the file isn't exists" , Toast.LENGTH_LONG).show();
                    Log.e("Print Photo error", "the file isn't exists");
                }

            //printNewLine();

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
    public static Bitmap toGrayscale(Bitmap srcImage) {
        Bitmap bmpGrayscale = Bitmap.createBitmap(srcImage.getWidth(),
                srcImage.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(srcImage, 0, 0, paint);
        return bmpGrayscale;
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

    public Bitmap[][] splitBitmap(Bitmap bitmap, int xCount, int yCount) {
        // Allocate a two dimensional array to hold the individual images.
        Bitmap[][] bitmaps = new Bitmap[xCount][yCount];
        int width, height;
        // Divide the original bitmap width by the desired vertical column count
        width = bitmap.getWidth() / xCount;
        // Divide the original bitmap height by the desired horizontal row count
        height = bitmap.getHeight() / yCount;
        // Loop the array and create bitmaps for each coordinate
        for(int x = 0; x < xCount; ++x) {
            for(int y = 0; y < yCount; ++y) {
                // Create the sliced bitmap
                bitmaps[x][y] = Bitmap.createBitmap(bitmap, x * width, y * height, width, height);
            }
        }
        // Return the array
        return bitmaps;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            btsocket = DeviceList.getSocket();
            if(btsocket != null){
                printPhoto();
            }

        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(MainActivity.this, "on activity result" , Toast.LENGTH_LONG).show();
        }
    }
}