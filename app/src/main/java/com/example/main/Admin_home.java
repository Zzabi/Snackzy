package com.example.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Admin_home extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        getSupportActionBar().setTitle("Snackzy!");
        BottomNavigationView bnv = findViewById(R.id.bottomNavigationView);
        replaceFragment(new ViewListFragment());
        bnv.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.view_list:
                    replaceFragment(new ViewListFragment());
                    break;
                case R.id.add_item:
                    replaceFragment(new AddItemFragment());
                    break;
                case R.id.order_history:
                    replaceFragment(new OrderHistoryFragment());
                    break;
            }
            return true;
        });
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.signout_option:
                SharedPreferences preferences = getSharedPreferences("ADMIN_LOGIN",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("IS_ADMIN_LOGGED_IN","false");
                editor.apply();
                startActivity(new Intent(Admin_home.this,Welcome.class));
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}