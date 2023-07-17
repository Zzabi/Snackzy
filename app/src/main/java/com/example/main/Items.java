package com.example.main;

public class Items {
    String itemName,itemPrice,itemAvailability;

    public Items(String itemName, String itemPrice, String itemAvailability) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemAvailability = itemAvailability;
    }

    public Items() {
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemAvailability() {
        return itemAvailability;
    }


    public void setItemAvailability(String itemAvailability) {
        this.itemAvailability = itemAvailability;
    }
}
