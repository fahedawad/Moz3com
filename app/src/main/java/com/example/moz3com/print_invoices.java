package com.example.moz3com;


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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moz3com.ForPrint.DeviceList;
import com.example.moz3com.ForPrint.PrinterCommands;
import com.example.moz3com.ForPrint.Utils;
import com.example.moz3com.PackageAdapter.AdapterSubList;
import com.example.moz3com.PackageData.itmeList;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class print_invoices extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayout printlayout,printlayout2,printtotal,printtotal2,zzz;
    private static BluetoothSocket btsocket;
    private static OutputStream outputStream;
    AdapterSubList adapterSubList;
    ArrayList<itmeList> arrayList;
    ImageView print ;
    ImageView logo ;
        TextView date ,name,type,total,txt10,txt4,txt16,title;

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
        date.setText(getIntent().getStringExtra("date"));
        type.setText(getIntent().getStringExtra("type"));
        name.setText(getIntent().getStringExtra("username"));
        txt4 = findViewById(R.id.tax);
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
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printDemo();
            }
        });
    }
    public void printDemo() {
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

            printPhotoFirst3();
            printTitle();
            printPhotoFirst2();
            printType();
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

            printTotal();
            Printtota2l();
            PrintFinal();

        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(MainActivity.this,"PrintTools"+ "the file isn't exists" , Toast.LENGTH_LONG).show();
            Log.e("PrintTools", "the file isn't exists");
        }

    }
    private void printType() {
        try {

            Bitmap bmp = getBitmapFromView(type);
            Bitmap b2 = Bitmap.createScaledBitmap(bmp , 550, 40 , false);
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
    private void printTitle() {
        try {

            Bitmap bmp = getBitmapFromView(title);
            Bitmap b2 = Bitmap.createScaledBitmap(bmp , 550, 40 , false);
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
    private void Printtota2l() {
        try {

            Bitmap bmp = getBitmapFromView(printtotal2);
            Bitmap b2 = Bitmap.createScaledBitmap(bmp , 550, 40 , false);
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
    private void PrintFinal() {
        try {

            Bitmap bmp = getBitmapFromView(zzz);
            Bitmap b2 = Bitmap.createScaledBitmap(bmp , 550, 40 , false);
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
    private void printTotal() {
        try {

            Bitmap bmp = getBitmapFromView(printtotal);
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
    private void printPhotoFirst3() {
        try {

            Bitmap bmp = getBitmapFromView(logo);
            Bitmap b2 = Bitmap.createScaledBitmap(bmp , 550, 150 , false);
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
    private void printPhotoFirst2() {
        try {

            Bitmap bmp = getBitmapFromView(printlayout2);
            Bitmap b2 = Bitmap.createScaledBitmap(bmp , 550, 50 , false);
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
    private void printPhotoFirst() {
        try {

            Bitmap bmp = getBitmapFromView(printlayout);
            Bitmap b2 = Bitmap.createScaledBitmap(bmp , 550, 50 , false);
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