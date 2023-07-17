package com.example.main;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Button logout;
    TextView detailsText;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logout = findViewById(R.id.logoutbtn);
        detailsText = findViewById(R.id.details);
        String name = user.getDisplayName();
        reference.child(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("Firebase", "Email ID is already present in the database.");
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String num = dataSnapshot.child("number").getValue(String.class);
                    String full_name = dataSnapshot.child("name").getValue(String.class);
                    Log.d(TAG, "onDataChange: "+full_name);
                    Log.d(TAG, "onDataChange: "+email);
                    Log.d(TAG, "onDataChange: "+num);
                    detailsText.setText(""+full_name+"\n"+email+"\n"+num+"\n");


                } else {
                    Log.d("Firebase", "User is not present in the database.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // An error occurred while querying the database
                Log.e("Firebase", "Error querying the database: " + databaseError.getMessage());
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();
                Intent i = new Intent(MainActivity.this,Welcome.class);
                startActivity(i);
                finish();

            }
        });
    }
}