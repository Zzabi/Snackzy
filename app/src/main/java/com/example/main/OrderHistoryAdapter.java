package com.example.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder>{

    Context context;
    ArrayList<Order> list;

    public OrderHistoryAdapter(Context context, ArrayList<Order> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.order_history_recycler,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Order order = list.get(position);
        holder.oid.setText(order.getTimestamp());
        holder.total_price.setText(String.valueOf(order.getTotalPrice()));
        holder.showDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderManagement ordermanager = new OrderManagement();
                ordermanager.editData(order);
                ordermanager.show(((AppCompatActivity) context).getSupportFragmentManager(),"edit data");
            }
        });
        holder.markComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> editItem = new HashMap<>();
                editItem.put("status","Completed");
                DatabaseReference orderRef = FirebaseDatabase.getInstance().
                        getReference("Orders").
                        child(String.valueOf(order.getTimestamp()));

                orderRef.updateChildren(editItem)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Order removed successfully
                                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // An error occurred while removing the order
                                Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView oid,total_price;
        Button showDetails,markComplete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            oid = itemView.findViewById(R.id.history_oid);
            total_price = itemView.findViewById(R.id.history_total_price);
            showDetails = itemView.findViewById(R.id.history_show_details);
            markComplete = itemView.findViewById(R.id.history_mark_completed);
        }
    }
}
