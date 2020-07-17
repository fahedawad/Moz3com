package com.example.moz3com;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.print.PrintHelper;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moz3com.ForPrint.DeviceList;
import com.example.moz3com.ForPrint.PrinterCommands;
import com.example.moz3com.ForPrint.Utils;
import com.example.moz3com.PackageAdapter.AdapterSubList;
import com.example.moz3com.PackageData.itmeList;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class print_invoices extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayout printlayout,printlayout2,printtotal,printtotal2,zzz , fulllayout,hhhhhhh;
    private static OutputStream outputStream;
    AdapterSubList adapterSubList;
    ArrayList<itmeList> arrayList;
    ImageView print ;
    LinearLayout logo ;
    TextView date ,name,type,total,txt10,txt4,txt16,title;
    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket bluetoothSocket;
    BluetoothDevice bluetoothDevice;
    InputStream inputStream;
    Thread thread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_invoices);
        print = findViewById(R.id.imageView2);
        recyclerView = findViewById(R.id.rec);
        recyclerView.setHasFixedSize(true);
        date =findViewById(R.id.txtdate);
        name =findViewById(R.id.nametxt);
        type =findViewById(R.id.t);
        fulllayout = findViewById(R.id.fulllayout);
        date.setText(getIntent().getStringExtra("date"));
        type.setText(getIntent().getStringExtra("type"));
        name.setText(getIntent().getStringExtra("username"));
        txt4 = findViewById(R.id.tax);
        hhhhhhh=findViewById(R.id.hhhhhhhh);
        txt10 =findViewById(R.id.tax10);
        txt16=findViewById(R.id.tax16);
        total =findViewById(R.id.total);
        title =findViewById(R.id.titlename);
        zzz =findViewById(R.id.zzzz);
        total.setText(getIntent().getStringExtra("total"));
        txt16.setText(getIntent().getStringExtra("0.16"));
        txt10.setText(getIntent().getStringExtra("0.10"));
        txt4.setText(getIntent().getStringExtra("0.04"));
        printtotal2 =findViewById(R.id.printtotal2);
        printtotal =findViewById(R.id.printtotal);
        printlayout =findViewById(R.id.printlayout);
        printlayout2 =findViewById(R.id.printlayout2);
        logo =findViewById(R.id.printimg);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        arrayList = new ArrayList<>();
        arrayList.addAll((Collection<? extends itmeList>) getIntent().getSerializableExtra("list"));
        adapterSubList = new AdapterSubList(print_invoices.this,arrayList);
        recyclerView.setAdapter(adapterSubList);
        try{
            FindBluetoothDevice();
            openBluetoothPrinter();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printPhoto();
            }
        });
    }
    public void printPhoto() {
        try {
            printDataLogo(logo);
            printData(title);
            printData(printlayout2);
            printData(type);
            printData(printlayout);
            int size = recyclerView.getAdapter().getItemCount();
            for (int i = 0 ; i<size ; i++){
                printData(recyclerView.getChildAt(i));
            }
            printData(printtotal);
            printData(hhhhhhh);
            printData(printtotal2);
            printData(zzz);
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(MainActivity.this,"PrintTools"+ "the file isn't exists" , Toast.LENGTH_LONG).show();
            Log.e("PrintTools", "the file isn't exists");
        }
    }
    public static Bitmap getBitmapFromView(View view) {
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
    // Open Bluetooth Printer
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
    } void printDataLogo(View view) throws  IOException{
        try{
            Bitmap bmp = getBitmapFromView(view);
            Bitmap b2 = Bitmap.createScaledBitmap(bmp , view.getWidth(), view.getHeight() , false);
            if(b2!=null){
                byte[] command = Utils.decodeBitmap(b2);
                outputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
                outputStream.write(command);
            }else{
                //Toast.makeText(MainActivity.this , "Print Photo error"+"the file isn't exists" , Toast.LENGTH_LONG).show();
                Log.e("Print Photo error", "the file isn't exists");
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    // Printing Text to Bluetooth Printer //
    void printData(View view) throws  IOException{
        try{
            Bitmap bmp = getBitmapFromView(view);
            Bitmap b2 = Bitmap.createScaledBitmap(bmp , 550, 70 , false);
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
    }
    // Disconnect Printer //
    void disconnectBT() throws IOException{
        try {
            stopWorker=true;
            outputStream.close();
            inputStream.close();
            bluetoothSocket.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}