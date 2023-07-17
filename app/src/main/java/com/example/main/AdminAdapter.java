package com.example.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.MyViewHolder> {

    Context context;
    ArrayList<Items> list;

    public AdminAdapter(Context context, ArrayList<Items> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.admin_item_recycler,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Items items = list.get(position);
        holder.item_name.setText(items.getItemName());
        holder.item_price.setText(items.getItemPrice());
        holder.editData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditItem editor = new EditItem();
                editor.editDataItem(items);
                editor.show(((AppCompatActivity) context).getSupportFragmentManager(),"edit data");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView item_name,item_price;
        Button editData;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.recycler_item_name);
            item_price = itemView.findViewById(R.id.recycler_item_price);
            editData = itemView.findViewById(R.id.edit_item_data);
        }
    }
}
