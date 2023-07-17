package com.example.main;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.MyViewHolder> {

    Context context;
    ArrayList<Items> list;
    public static ArrayList<OrderItem> orderItems = new ArrayList<>();

    public CustomerAdapter(Context context, ArrayList<Items> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.customer_item_recycler,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Items items = list.get(position);
        holder.item_name.setText(items.getItemName());
        holder.item_price.setText(items.getItemPrice());
        holder.addToCart.setEnabled(false);
        holder.increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int number = Integer.parseInt(holder.stock.getText().toString());
                number+=1;
                holder.stock.setText(String.valueOf(number));
                if (number==1){
                    holder.addToCart.setEnabled(true);
                }
            }
        });

        holder.decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int number = Integer.parseInt(holder.stock.getText().toString());
                number-=1;
                if (number<=0){
//                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                    number = 0;
                    holder.addToCart.setEnabled(false);
                }
                holder.stock.setText(String.valueOf(number));
            }
        });

        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item_name = items.getItemName();
                int quantity = Integer.parseInt(holder.stock.getText().toString());
                int price = Integer.parseInt(items.getItemPrice())*quantity;
                if (holder.addToCart.isChecked()){
//                    Toast.makeText(context, ""+items.getItemName()+" Added", Toast.LENGTH_SHORT).show();
                    orderItems.add(new OrderItem(item_name,quantity,price));
                    holder.increment.setEnabled(false);
                    holder.decrement.setEnabled(false);
                }
                else{
                    Iterator<OrderItem> iterator = orderItems.iterator();
                    while (iterator.hasNext()) {
                        OrderItem item = iterator.next();
                        if (item.itemId.equals(items.getItemName())) {
                            iterator.remove();
                        }
                    }
                    holder.stock.setText("0");
                    holder.increment.setEnabled(true);
                    holder.decrement.setEnabled(true);
                    holder.addToCart.setEnabled(false);
//                    Toast.makeText(context, ""+items.getItemName()+" Removed from cart", Toast.LENGTH_SHORT).show();
                }
                updateData(orderItems);
            }

        });
    }

    public void updateData(ArrayList<OrderItem> orderItems){
        int totalPrice = 0;
        for(OrderItem item:orderItems){
            Log.d(TAG, "updateData: "+item.itemId);
            totalPrice+=item.price;
        }
        Intent intent = new Intent("custom-message");
        Bundle args = new Bundle();
        args.putSerializable("ORDERITEMS",(Serializable) orderItems);
        intent.putExtra("price",String.valueOf(totalPrice));
        intent.putExtra("BUNDLE",args);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView item_name,item_price,stock;
        Button increment,decrement;
        ToggleButton addToCart;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.recycler_item_name);
            item_price = itemView.findViewById(R.id.recycler_item_price);
            increment = itemView.findViewById(R.id.incrementor);
            decrement = itemView.findViewById(R.id.decrementor);
            stock = itemView.findViewById(R.id.item_stock);
            addToCart = itemView.findViewById(R.id.add_to_cart);
        }
    }
}
