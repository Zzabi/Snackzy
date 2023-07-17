package com.example.main;

import java.io.Serializable;
public class OrderItem implements Serializable{
    String itemId;
    int quantity,price;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public OrderItem(String itemId, int quantity, int price) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.price = price;
    }

    public OrderItem() {
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
