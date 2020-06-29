package com.example.moz3com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.moz3com.PackageAdapter.AdapterRetuenData;
import com.example.moz3com.PackageData.Data;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ItemList extends AppCompatActivity {
    AdapterRetuenData retuenData;
    List<Data>data;
    RecyclerView recyclerView;
    AutoCompleteTextView autoCompleteTextView;
  ArrayList<String >strings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
       strings = new ArrayList<>();
        recyclerView =findViewById(R.id.list);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        Toast.makeText(this, "heello", Toast.LENGTH_SHORT).show();
        GridLayoutManager glm = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(glm);
        data =new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("item")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot s1:dataSnapshot.getChildren()){
                            String img = s1.child("صورة المنتج").getValue(String.class);
                            data.add(new Data(s1.getKey(), img));
                            strings.add(s1.getKey());
                            retuenData =new AdapterRetuenData(data,ItemList.this);
                            recyclerView.setAdapter(retuenData);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,strings );
       autoCompleteTextView = (AutoCompleteTextView)
                findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedStr  = parent.getItemAtPosition(position).toString();
                Toast.makeText(ItemList.this, selectedStr, Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(ItemList.this, Change.class);
                intent.putExtra("itme",selectedStr);
                startActivity(intent);
            }
        });
    }
}
