package com.example.moz3com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.print.PrintHelper;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.moz3com.ForPrint.DeviceList;
import com.example.moz3com.ForPrint.PrinterCommands;
import com.example.moz3com.ForPrint.Utils;
import com.example.moz3com.PackageAdapter.AccountAdapter;
import com.example.moz3com.PackageAdapter.AdapterKshf;
import com.example.moz3com.PackageData.AccountData;
import com.example.moz3com.PackageData.Kshf;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class AccountStatement extends AppCompatActivity  {
       String id;
       RecyclerView recyclerView;
       RecyclerView df3atrec;
       AccountAdapter accountAdapter;
       AdapterKshf adapterKshf;
       List<Kshf>kshfs;
       LinearLayout linearLayout;
       TextView totalacc , totalhala , fulltotal,zz,af,name;
       List<AccountData>users;
       Double sum ,sum2,taxsum04,x,y,taxsum10,taxsum16,z,i,f,total,fahed,totaldf3,to;
        private static BluetoothSocket btsocket;
        private static OutputStream outputStream;
    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket bluetoothSocket;
    BluetoothDevice bluetoothDevice;
    InputStream inputStream;
    Thread thread;
    String string;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
        LinearLayout header1 , header2 ;
        ImageView print;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_statement);
        recyclerView =findViewById(R.id.Account);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        totalacc =findViewById(R.id.totalacc);
        totalhala = findViewById(R.id.totalhala);
        linearLayout = findViewById(R.id.printimg);
        fulltotal = findViewById(R.id.fulltotal);
        zz = findViewById(R.id.z);
        af = findViewById(R.id.fa);
        print = findViewById(R.id.print);
        name = findViewById(R.id.nameacc);
        name.setText("كشف حساب السيد/السادة"+"\t"+"\t"+getIntent().getStringExtra("name"));
        header1 = findViewById(R.id.header1);
        header2 = findViewById(R.id.header2);
        final DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        users =new ArrayList<>();
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        taxsum04 = 0.0;
        taxsum10 = 0.0;
        taxsum16 = 0.0;
        sum = 0.0;
        sum2 = 0.0;
        x = 0.0;
        y = 0.0;
        z = 0.0;
      total =0.0;
        fahed =0.0;
        to =0.0;
        kshfs =new ArrayList<>();
        df3atrec =findViewById(R.id.hala);
        df3atrec.setItemAnimator(new DefaultItemAnimator());
        df3atrec.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager1 =new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        df3atrec.setLayoutManager(linearLayoutManager1);
      FirebaseDatabase.getInstance().getReference("order").child(getIntent().getStringExtra("id")).addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              for (DataSnapshot datesnap:dataSnapshot.getChildren()){
                 for (DataSnapshot item:datesnap.getChildren()){
                     id =datesnap.getKey();
                     if (item.getKey().equals("طريقة الدفع")) {
                     }else {
                         total = Double.parseDouble(item.child("المجموع").getValue(String.class));
                         string=datesnap.child("طريقة الدفع").getValue(String.class);
                         System.out.println(string);
                         sum = sum + total;
                         Double tax4 = Double.parseDouble(item.child("الضريبه").getValue(String.class));
                         if (tax4 == 0.04) {
                             taxsum04 = tax4 * total;
                             y += taxsum04;
                             System.out.println(y + "         y");
                         } else {
                         }
                         if (tax4 == 0.10) {
                             taxsum10 = tax4 * total;
                             x += taxsum10;
                         } else {
                         }
                         if (tax4 == 0.16) {
                             taxsum16 = tax4 * total;
                             z += taxsum16;
                         } else {

                         }
                         sum2 = y + x + z + sum;
                         System.out.println(sum2+"               sum2");
                         fahed+=sum2;


                     }
                 }
                  if (datesnap.child("طريقة الدفع").getValue(String.class).equals("ذمم"))
                  {
                      to+=sum2;
                  }
                  totalacc.setText(" مجموع السحوبات المذممة : " + df .format(to));
                  users.add(new AccountData(id,df.format(sum2)+"",datesnap.child("طريقة الدفع").getValue(String.class)));
                  taxsum04 = 0.0;
                  taxsum10 = 0.0;
                  taxsum16 = 0.0;
                  sum = 0.0;
                  sum2 = 0.0;
                  x = 0.0;
                  y = 0.0;
                  z = 0.0;
                  total =0.0;
              }
              accountAdapter =new AccountAdapter(users,AccountStatement.this);
              recyclerView.setAdapter(accountAdapter);
              FirebaseDatabase.getInstance().getReference("Catch Receipt").child(getIntent().getStringExtra("id")).addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      totaldf3 = 0.0;
                      kshfs.clear();
                      for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                          //kshfs.clear(); // i think it was wrong
                          kshfs.add(new Kshf(snapshot.child("المبلغ").getValue(String.class),snapshot.child("طرقة الدقع").getValue(String.class),snapshot.child("التاريخ").getValue(String.class)));
                          try {
                              totaldf3 += Double.parseDouble(snapshot.child("المبلغ").getValue(String.class));
                          }
                          catch (Exception e){
                              totaldf3 += 0;
                          }
                      }
                      totalhala.setText(" مجموع الدفع : "+df.format(totaldf3));
                      adapterKshf =new AdapterKshf(AccountStatement.this,kshfs);
                      df3atrec.setAdapter(adapterKshf);
                      fulltotal.setText("المبلغ المطلوب : "+df.format(fahed-totaldf3));
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError databaseError) {
                  }
              });
          }
          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {
          }
      });

      print.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              try{
                  FindBluetoothDevice();
                  openBluetoothPrinter();
              }catch (Exception ex){
                  ex.printStackTrace();
              }
              printfahed();
          }
      });
    }
    public void printfahed() {
        try {
            printData(linearLayout,550,200);
            printData(name,550,100);
            printData(header1,550,200);

            int size = recyclerView.getAdapter().getItemCount();
            for (int i = 0 ; i<size ; i++){
                printData(recyclerView.getChildAt(i),550,100);
            }


            int size1 = recyclerView.getAdapter().getItemCount();
            printData(header2,550,200);
            for (int i = 0 ; i<size1 ; i++){
                printData(df3atrec.getChildAt(i),550,100);
            }
            printData(totalacc,550,50);

            printData(totalhala,550,50);

            printData(fulltotal,550,50);
            printData(af,550,50);
            printData(zz,550,50);

        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(MainActivity.this,"PrintTools"+ "the file isn't exists" , Toast.LENGTH_LONG).show();
            Log.e("PrintTools", "the file isn't exists");
        }
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
            else {
                bluetoothAdapter.enable();
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
    } void printDataLogo(View view,int w,int h) throws  IOException{
        try{
            Bitmap bmp = getBitmapFromView(view);
            Bitmap b2 = Bitmap.createScaledBitmap(bmp , w, h , false);
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
    void printData(View view,int w,int h) throws  IOException{
        try{
            Bitmap bmp = getBitmapFromView(view);
            Bitmap b2 = Bitmap.createScaledBitmap(bmp , w, h , false);
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

    public void printPhoto() {
        try {
            printPhotoFirst(header1);
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
            printPhotoFirst(totalacc);
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(MainActivity.this,"PrintTools"+ "the file isn't exists" , Toast.LENGTH_LONG).show();
            Log.e("PrintTools", "the file isn't exists");
        }

        printPhoto2();
    }

    public void printPhoto2() {
        try {
            printPhotoFirst(header2);
            int size = df3atrec.getAdapter().getItemCount();
            for (int i = 0 ; i<size ; i++){
                System.out.println(i + "                    i");
                Bitmap bmp = getBitmapFromView(df3atrec.getChildAt(i));
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
            printPhotoFirst(totalhala);
            printPhotoFirst(fulltotal);
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(MainActivity.this,"PrintTools"+ "the file isn't exists" , Toast.LENGTH_LONG).show();
            Log.e("PrintTools", "the file isn't exists");
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
                printPhoto();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}