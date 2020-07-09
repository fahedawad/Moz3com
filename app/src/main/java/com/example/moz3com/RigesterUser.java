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

import com.example.moz3com.PackageAdapter.AdapterRegister;
import com.example.moz3com.PackageAdapter.AdapterUser;
import com.example.moz3com.PackageData.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RigesterUser extends AppCompatActivity {
    AdapterRegister adapterUser;
    List<User> users;
    RecyclerView recyclerView;
    ArrayList<String > strings;
    String uid;
    AutoCompleteTextView autoCompleteTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rigester_user);
        autoCompleteTextView =findViewById(R.id.autoa);
        recyclerView =findViewById(R.id.recuser);
        recyclerView.setHasFixedSize(true);
        strings =new ArrayList<>();
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        users =new ArrayList<>();
        users.clear();
        FirebaseDatabase.getInstance().getReference("user")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            if (snapshot.hasChild("key")){
                                users.add(new User(snapshot.child("name").getValue(String.class),snapshot.child("id").getValue(String.class)));
                                uid =snapshot.child("id").getValue(String.class);
                                strings.add(snapshot.child("name").getValue(String.class));
                            }

                        }
                        adapterUser =new AdapterRegister(RigesterUser.this,users);
                        recyclerView.setAdapter(adapterUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,strings );
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedStr  = parent.getItemAtPosition(position).toString();
                Intent intent =new Intent(RigesterUser.this, ListScreen.class);
                intent.putExtra("id",uid);
                startActivity(intent);
            }
        });
    }
}