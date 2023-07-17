package com.example.main;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    TextView name,phno;
    Button registerBtn;
    FirebaseUser user;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        name = findViewById(R.id.fullname);
        phno = findViewById(R.id.number);
        user = FirebaseAuth.getInstance().getCurrentUser();
        registerBtn = findViewById(R.id.register);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = user.getEmail();
                String fullname = name.getText().toString();
                String phone_num = phno.getText().toString();
                if (!fullname.isEmpty() && !phone_num.isEmpty() && !email.isEmpty()){
                    Users users = new Users(fullname,phone_num,email);
                    reference = FirebaseDatabase.getInstance().getReference("Users");
                    //String uid = reference.push().getKey();
                    String uid = user.getDisplayName();
                    reference.child(uid).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(Register.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Register.this,Customer_Home.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        });

    }
}