package com.example.moz3com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.moz3com.PackageAdapter.AdapterUser;
import com.example.moz3com.PackageAdapter.AdapterUser2;
import com.example.moz3com.PackageAdapter.AdapterUser3;
import com.example.moz3com.PackageData.User2;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class User extends AppCompatActivity {
    AdapterUser2 adapterUser;
    List<com.example.moz3com.PackageData.User2> users;
    RecyclerView recyclerView;
    AutoCompleteTextView autoCompleteTextView;
    String uid;
    Button button;
    ArrayList<String>strings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        recyclerView =findViewById(R.id.ff);
        button =findViewById(R.id.pr);
        autoCompleteTextView =findViewById(R.id.autoCompleteTextView2);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        users =new ArrayList<>();
        users.clear();
        strings =new ArrayList<>();
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        FirebaseDatabase.getInstance().getReference("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    users.add(new User2(snapshot.child("name").getValue(String.class),snapshot.child("id").getValue(String.class)));
                    strings.add(snapshot.child("name").getValue(String.class));
                    uid =snapshot.child("id").getValue(String.class);
                }

                    String key =getIntent().getStringExtra("key");
                    if (key.equals("key")){
                        button.setVisibility(View.VISIBLE);
                        AdapterUser3 adapterUser3 =new AdapterUser3(User.this,users);
                        recyclerView.setAdapter(adapterUser3);
                    }
                    else if (key.equals("key2")){
                        adapterUser =new AdapterUser2(User.this,users);
                        recyclerView.setAdapter(adapterUser);
                    }
               


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User.this,AllDocuments.class));
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,strings );
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedStr  = parent.getItemAtPosition(position).toString();
                Intent intent =new Intent(User.this, AccountStatement.class);
                intent.putExtra("id",uid);
                startActivity(intent);
            }
        });
    }
}