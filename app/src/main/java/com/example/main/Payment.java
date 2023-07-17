package com.example.main;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dev.shreyaspatil.easyupipayment.EasyUpiPayment;
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener;
import dev.shreyaspatil.easyupipayment.model.PaymentApp;
import dev.shreyaspatil.easyupipayment.model.TransactionDetails;

import java.util.ArrayList;
import java.util.Iterator;

public class Payment extends AppCompatActivity implements PaymentStatusListener {
    TextView itemDetails, itemPrices, itemQuant, orderPrice;
    Button orderPayment,tempOrder;
    ArrayList<OrderItem> orderitems;
    String totalPrice = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        getSupportActionBar().hide();


        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        orderitems = (ArrayList<OrderItem>) args.getSerializable("ORDERITEMS");
        totalPrice = intent.getStringExtra("price");

        StringBuffer itemDetailsText = new StringBuffer();
        StringBuffer itemPricesText = new StringBuffer();
        StringBuffer itemQuantText = new StringBuffer();

        if(orderitems!=null){
            Iterator<OrderItem> iterator = orderitems.iterator();
            while (iterator.hasNext()) {
                OrderItem i = iterator.next();
                itemDetailsText.append(i.getItemId() + "\n");
                itemPricesText.append(i.getPrice() + "\n");
                itemQuantText.append(i.getQuantity() + "\n");
            }
        }

        itemDetails = findViewById(R.id.order_item_names);
        itemPrices = findViewById(R.id.order_item_price);
        itemQuant = findViewById(R.id.order_item_quant);
        orderPrice = findViewById(R.id.order_total_price);

        itemDetails.setText(itemDetailsText.toString());
        itemPrices.setText(itemPricesText.toString());
        itemQuant.setText(itemQuantText.toString());
        orderPrice.setText(totalPrice);

        tempOrder = findViewById(R.id.temp_order);
        tempOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temporaryOrder();
            }
        });

        orderPayment = findViewById(R.id.order_payment);
        orderPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(Payment.this, "To be implemented", Toast.LENGTH_SHORT).show();
                makingPayment(totalPrice);
            }
        });
    }

    public void temporaryOrder(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Orders");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");

        String order_id = reference.push().getKey();
        Order order = new Order();

        assert user != null;
        String uid = user.getEmail();
        userRef.orderByChild("email").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot userSnapshot:snapshot.getChildren()){
                    Users users = userSnapshot.getValue(Users.class);
                    String uuid = users.getName();
                    Log.d(TAG, "onClick: "+uuid);
                    order.setUserId(uuid);
                    order.setOrderItems(orderitems);
                    order.setTotalPrice(Double.parseDouble(totalPrice));
                    order.setStatus("In Progress");
                    String time = String.valueOf(System.currentTimeMillis());
                    order.setTimestamp(time);
                    reference.child(time).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(Payment.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                            tempOrder.setEnabled(false);
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @SuppressLint("NonConstantResourceId")
    private void makingPayment(String tP) {
        // START PAYMENT INITIALIZATION
        EasyUpiPayment.Builder builder = new EasyUpiPayment.Builder(this)
                .with(PaymentApp.ALL)
                .setPayeeVpa("9731940313@ybl")
                .setPayeeName("Mohammed Zabiullah C")
                .setTransactionId("1234567890")
                .setTransactionRefId("1234567890")
                .setPayeeMerchantCode("123")
                .setDescription("description")
                .setAmount(tP+".00");
        // END INITIALIZATION

        try {
            // Build instance
            EasyUpiPayment easyUpiPayment = builder.build();

            // Register Listener for Events
            easyUpiPayment.setPaymentStatusListener(this);

            // Start payment / transaction
            easyUpiPayment.startPayment();
        } catch (Exception exception) {
            exception.printStackTrace();
            toast("Error: " + exception.getMessage());
        }
    }

    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails) {

        // Transaction Completed
        Log.d("TransactionDetails", transactionDetails.toString());
        switch (transactionDetails.getTransactionStatus()) {
            case SUCCESS:
                onTransactionSuccess();
                break;
            case FAILURE:
                onTransactionFailed();
                break;
            case SUBMITTED:
                onTransactionSubmitted();
                break;
        }
    }

    @Override
    public void onTransactionCancelled() {
        // Payment Cancelled by User
        toast("Cancelled by user");
    }

    private void onTransactionSuccess() {
        // Payment Success
        toast("Success");

    }

    private void onTransactionSubmitted() {
        // Payment Pending
        toast("Pending | Submitted");
    }

    private void onTransactionFailed() {
        // Payment Failed
        toast("Failed");
    }

    public void toast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}