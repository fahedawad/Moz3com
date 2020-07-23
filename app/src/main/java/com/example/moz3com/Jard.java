package com.example.moz3com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Jard extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterJard adapterJard;
    List<DataJard>list;
    Double sum,total,sum2,y,z,x,sumcount;
    Calendar calendar ;
    LinearLayout printlayout,linearLayout , comname , datelinear ;
    private static BluetoothSocket btsocket;
    private static OutputStream outputStream;
    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket bluetoothSocket;
    BluetoothDevice bluetoothDevice;
    TextView fahedDate;
    InputStream inputStream;
    Thread thread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    ImageView print;
    ProgressDialog dialog;
    String dateString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jard);
        printlayout = findViewById(R.id.printlayout);
        linearLayout = findViewById(R.id.linerprinnterHead);
        comname = findViewById(R.id.comname);
        datelinear = findViewById(R.id.datelayout);
        dialog =new ProgressDialog(this);
        dialog.setMessage("جاري تحميل الاصناف");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        fahedDate =findViewById(R.id.fahedsate);
        list =new ArrayList<>();
        sumcount =0.0;
        sum = 0.0;
        sum2 = 0.0;
        x = 0.0;
        y = 0.0;
        z = 0.0;
        total = 0.0;
        print = findViewById(R.id.print);
        dateString ="";
        recyclerView =findViewById(R.id.jard);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        calendar = Calendar.getInstance();
        Date currentdate = new Date();
        calendar.setTime(currentdate);
        final Button date = findViewById(R.id.getdate);
         date.setOnClickListener(view -> {
             DatePickerDialog datePickerDialog = new DatePickerDialog(Jard.this,
                     (datePicker, year, month, day) -> {
                         Calendar calendar = Calendar.getInstance();
                         calendar.set(year, month, day);
                         SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                          dateString = convertToEnglish(formatter.format(calendar.getTime())) ;
                         date.setText(dateString);
                         fahedDate.setText("جرد مبيعات ليوم "+"\t"+"\t"+"\t"+date.getText());
                     },calendar.get(Calendar.YEAR),
                     calendar.get(Calendar.MONTH),
                     calendar.get(Calendar.DAY_OF_MONTH));
             datePickerDialog.show();
         });
            findViewById(R.id.okpushdata).setOnClickListener(view -> {
                TextView view1 =findViewById(R.id.fahedsate);
                if (dateString.equals("")){
                    Toast.makeText(Jard.this, "يجب تحديد التاريخ في البداية", Toast.LENGTH_SHORT).show();
                }else {
                    list.clear();
                    view1.setText("\t"+"\t"+"\t"+"جرد  المبيعات  لتاريخ :"+dateString);
                    dialog.setMessage("جاري تحميل الاصناف");
                    dialog.show();
                    getData(date.getText().toString());
                }
            });
            print.setOnClickListener(view -> {
                dialog.setMessage("جاري طباعة الجرد");
                print.setVisibility(View.GONE);
                try{
                    FindBluetoothDevice();
                    openBluetoothPrinter();
                }catch (Exception ex){
                    ex.printStackTrace(); }
                try {

                    printData(printlayout);
                    printData(comname);
                    printData(datelinear);
                    printData(linearLayout);
                    int size = recyclerView.getAdapter().getItemCount();
                    for (int i = 0 ; i<size ; i++){
                        printData(recyclerView.getChildAt(i));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });

    }public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
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
        //return the bitmap
        return returnedBitmap;
    }
    void FindBluetoothDevice() {
        try {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null) {
            }
            if (bluetoothAdapter.isEnabled()) {
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBT, 0);
            }
            Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();
            if (pairedDevice.size() > 0) {
                for (BluetoothDevice pairedDev : pairedDevice) {

                    // My Bluetoth printer name
                    if (pairedDev.getName().equals("printer001")) {
                        bluetoothDevice = pairedDev;
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    void openBluetoothPrinter() throws IOException {
        try{
            //Standard uuid from string //
            UUID uuidSting = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            bluetoothSocket=bluetoothDevice.createRfcommSocketToServiceRecord(uuidSting);
            bluetoothSocket.connect();
            outputStream=bluetoothSocket.getOutputStream();
            inputStream=bluetoothSocket.getInputStream();
            beginListenData();
        }catch (Exception ex){
        }
    }
    void beginListenData(){
        try{
            final Handler handler =new Handler();
            final byte delimiter=10;
            stopWorker =false;
            readBufferPosition=0;
            readBuffer = new byte[1024];
            thread=new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!Thread.currentThread().isInterrupted() && !stopWorker){
                        try{
                            int byteAvailable = inputStream.available();
                            if(byteAvailable>0){
                                byte[] packetByte = new byte[byteAvailable];
                                inputStream.read(packetByte);

                                for(int i=0; i<byteAvailable; i++){
                                    byte b = packetByte[i];
                                    if(b==delimiter){
                                        byte[] encodedByte = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer,0,
                                                encodedByte,0,
                                                encodedByte.length
                                        );
                                        final String data = new String(encodedByte,"UTF-8");
                                        readBufferPosition=0;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                            }
                                        });
                                    }else{
                                        readBuffer[readBufferPosition++]=b;
                                    }
                                }
                            }
                        }catch(Exception ex){
                            stopWorker=true;
                        }
                    }
                }
            });
            thread.start();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    void printData(View view) throws  IOException{
        dialog.show();
        try{
            Bitmap bmp = getBitmapFromView(view);
            Bitmap b2 = Bitmap.createScaledBitmap(bmp , 800, 120 , false);
            if(b2!=null){
                byte[] command = Utils.decodeBitmap(b2);
                outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                outputStream.write(command);
            }else{
                //Toast.makeText(MainActivity.this , "Print Photo error"+"the file isn't exists" , Toast.LENGTH_LONG).show();
                Log.e("Print Photo error", "the file isn't exists");
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        dialog.dismiss();
    }
    public void getData(final String getdate){

        FirebaseDatabase.getInstance().getReference("jard").child(getdate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    list.add(new DataJard(getdate , ds.getKey() , ds.child("المجموع").getValue(String.class)+"",""+ds.child("سعر البيع").getValue(String.class),""+ds.child("سعر الشراء").getValue(String.class),""+ds.child("العدد").getValue(String.class),
                            ""+ds.child("الربح").getValue(String.class),""+ds.child("العدد المتاح").getValue(String.class)));
                }
                adapterJard =new AdapterJard(list,Jard.this);
                recyclerView.setAdapter(adapterJard);
                dialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
 public void printPhoto() {
        try {
            printPhotoFirst();
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
        }
    }
    public static String convertToEnglish(String value)
    {
        String newValue =   (((((((((((value+"").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0"));
        return newValue;
    }
}