package com.example.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Admin_Login extends AppCompatActivity {
    Button loginBtn;
    TextView id_number,passwordText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        getSupportActionBar().hide();
        id_number = findViewById(R.id.id_no);
        passwordText = findViewById(R.id.password);
        loginBtn = findViewById(R.id.admin_login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Login_id = id_number.getText().toString();
                String Password = passwordText.getText().toString();
                if (Login_id.equals("snackzy") && Password.equals("123")){
                    Toast.makeText(Admin_Login.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                    SharedPreferences preferences = getSharedPreferences("ADMIN_LOGIN",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("IS_ADMIN_LOGGED_IN","true");
                    editor.apply();
                    Intent i = new Intent(Admin_Login.this, Admin_home.class);
                    startActivity(i);
                    finish();
                }
                else{
                    Toast.makeText(Admin_Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}