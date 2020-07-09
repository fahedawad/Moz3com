package com.example.moz3com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Auth extends AppCompatActivity {
    EditText name;
    String id ,nametxt;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        progressDialog =new ProgressDialog(this);
        progressDialog.setMessage("جاري التحميل");
        name =findViewById(R.id.nameuser);
        auth =FirebaseAuth.getInstance();
       FirebaseAuth.getInstance().signOut();
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nametxt =name.getText().toString();
                if (nametxt.isEmpty()){
                    name.setError("أدخل الاسم");
                }
                else push(nametxt);
            }
        });
        findViewById(R.id.rigster).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Auth.this,RigesterUser.class));
            }
        });
    }
    public void push(final String name){
        progressDialog.show();
       auth.signInAnonymously().addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                final FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();
                HashMap<String,Object>map =new HashMap<>();
                map.put("id",user.getUid());
                map.put("name",name);
                map.put("رصيد السابق",0.0);
                map.put("key","key");
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user").child(user.getUid());
              reference.setValue(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                Intent intent =new Intent(Auth.this,ListScreen.class);
                                intent.putExtra("id",user.getUid());
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                      System.out.println(e);
                  }
              });
            }
        }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               System.out.println(e);
           }
       });
    }
}