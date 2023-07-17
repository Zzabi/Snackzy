package com.example.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class Customer_Home extends AppCompatActivity {
    RecyclerView recycler;
    DatabaseReference reference;
    CustomerAdapter adapter;
    ArrayList<Items> list;
    String totalPrice;
    ArrayList<OrderItem> orderItems;
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
                totalPrice = intent.getStringExtra("price");
                Bundle args = intent.getBundleExtra("BUNDLE");
                orderItems = (ArrayList<OrderItem>)args.getSerializable("ORDERITEMS");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);
        getSupportActionBar().setTitle("Snackzy!");
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("custom-message"));

        recycler = findViewById(R.id.customer_item_recycler);
        reference = FirebaseDatabase.getInstance().getReference("Items");
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();

        adapter = new CustomerAdapter(this,list);
        recycler.setAdapter(adapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Items items = dataSnapshot.getValue(Items.class);
                    if(items!=null && items.getItemAvailability().equals("available")){
                        list.add(items);
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.signout_option:
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                startActivity(new Intent(Customer_Home.this,Welcome.class));
                finish();
                break;

            case R.id.proceed_to_cart:
                Intent intent = new Intent(Customer_Home.this, Payment.class);
                Bundle args = new Bundle();
                args.putSerializable("ORDERITEMS",(Serializable) orderItems);
                intent.putExtra("price",String.valueOf(totalPrice));
                intent.putExtra("BUNDLE",args);
                String totalPrice = intent.getStringExtra("price");

                if((totalPrice!=null) && (orderItems!=null)){
                    startActivity(intent);
                }
                else{
                    Toast.makeText(this, "Nothing in Cart", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}