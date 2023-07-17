package com.example.main;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class EditItem extends AppCompatDialogFragment {
    TextView item_name,item_price;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch availability;
    Button deleteItem;
    String itemName,itemPrice,itemAvailability;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_admin_edit_data,null);
        builder.setView(view)
                .setTitle("Edit Item")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        HashMap<String, Object> editItem = new HashMap<>();
                        editItem.put("itemName",item_name.getText().toString());
                        editItem.put("itemPrice",item_price.getText().toString());
                        if(availability.isChecked()){
                            editItem.put("itemAvailability","available");
                        }else{
                            editItem.put("itemAvailability","not available");
                        }
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Items");
                        reference.child(item_name.getText().toString().toUpperCase()).
                                updateChildren(editItem).
                                addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if(task.isSuccessful()){
                                    Log.d(TAG, "onComplete: Item updated succesfully");
                                }else{
                                    Log.d(TAG, "onComplete: Item was not updated");
                                }
                            }
                        });
                    }
                    
                });
        item_name = view.findViewById(R.id.item_name);
        item_name.setText(itemName);

        item_price = view.findViewById(R.id.item_price);
        item_price.setText(itemPrice);

        availability = view.findViewById(R.id.availability);
        availability.setChecked(itemAvailability.equals("available"));

        deleteItem = view.findViewById(R.id.delete_item_button);
        deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Items");
                reference.child(itemName.toUpperCase()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: Item removed succesfully");
                            dismiss();
                        }else{
                            Log.d(TAG, "onComplete: Item wasn't removed");
                        }
                    }
                });
            }
        });
        return builder.create();
    }

    public void editDataItem(Items items){
        itemName = items.getItemName();
        itemPrice = items.getItemPrice();
        itemAvailability = items.getItemAvailability();
    }
}
