package com.example.moz3com;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        FirebaseDatabase.getInstance().getReference("checkdate").child("date")
                .setValue(df.format(new Date()) + "");
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("fcm", "getInstanceId failed", task.getException());
                        return;
                    }
                    String token = task.getResult().getToken();
                    sendRegistrationToServer(token);
                });
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        findViewById(R.id.add).setOnClickListener(v -> startActivity(new Intent(MainActivity.this,AddItem.class)));
        findViewById(R.id.change).setOnClickListener(v -> startActivity(new Intent(MainActivity.this,ItemList.class)));
        findViewById(R.id.bill).setOnClickListener(v -> startActivity(new Intent(MainActivity.this,Bills.class)));
        findViewById(R.id.custom).setOnClickListener(v -> startActivity(new Intent(MainActivity.this,PageUser.class)));
        findViewById(R.id.storag).setOnClickListener(v -> startActivity(new Intent(MainActivity.this,Jard.class)));
        findViewById(R.id.order).setOnClickListener(v -> startActivity(new Intent(MainActivity.this,Auth.class)));
        findViewById(R.id.kshf).setOnClickListener(v -> {
            Intent intent =new Intent(MainActivity.this,User.class);
            intent.putExtra("key","key2");
            startActivity(intent);
        });
        findViewById(R.id.report).setOnClickListener(v -> {
            Intent intent =new Intent(MainActivity.this,User.class);
            intent.putExtra("key","key");
            startActivity(intent);
        });
    }
    private void sendRegistrationToServer(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("admin");
        reference.child("token").setValue(token);
    }
}
