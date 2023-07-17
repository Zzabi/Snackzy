package com.example.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;
import java.util.Objects;

public class OrderManagement extends AppCompatDialogFragment {
    TextView itemDetails, itemPrices, itemQuant, orderPrice,userName,itemStatus;
    String userId,status;
    ArrayList<OrderItem> orderItems;
    String totalPrice;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.order_management,null);
        builder.setView(view)
                .setTitle("Order Details")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                })
                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        StringBuffer itemDetailsText = new StringBuffer();
        StringBuffer itemPricesText = new StringBuffer();
        StringBuffer itemQuantText = new StringBuffer();

        for (OrderItem i : orderItems) {
            itemDetailsText.append(i.getItemId() + "\n");
            itemPricesText.append(i.getPrice() + "\n");
            itemQuantText.append(i.getQuantity() + "\n");
        }

        itemDetails = view.findViewById(R.id.history_item_names);
        itemPrices = view.findViewById(R.id.history_item_price);
        itemQuant = view.findViewById(R.id.history_item_quant);
        orderPrice = view.findViewById(R.id.history_total_price);
        userName = view.findViewById(R.id.history_user_name);
        itemStatus = view.findViewById(R.id.history_status);

        itemDetails.setText(itemDetailsText.toString());
        itemPrices.setText(itemPricesText.toString());
        itemQuant.setText(itemQuantText.toString());
        orderPrice.setText(totalPrice);
        userName.setText(userId);
        itemStatus.setText(status);

        return builder.create();
    }

    public void editData(Order order){
        orderItems = order.getOrderItems();
        totalPrice = String.valueOf(order.getTotalPrice());
        userId = order.getUserId();
        status = order.getStatus();
    }
}
