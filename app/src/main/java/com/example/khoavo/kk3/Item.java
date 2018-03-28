package com.example.khoavo.kk3;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by khoavo on 7/8/17.
 */

public class Item implements Parcelable {
    int ID;
    String Name;
    String CleanName;
    double Price;
    int Amount;
    double subTotal;


    public Item(){

    }

    public Item(String name, double price) {
        Name = name;
        Price = price;
    }

    public Item(String name, String cleanName, double price) {
        Name = name;
        CleanName = cleanName;
        Price = price;
    }

    public Item(int id, String name,String cleanName, double price) {
        ID = id;
        Name = name;
        CleanName = cleanName;
        Price = price;
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
        calculateSubTotal();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCleanName() {
        return CleanName;
    }

    public void setCleanName(String cleanName) {
        CleanName = cleanName;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    private void calculateSubTotal(){
        subTotal = Amount * Price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(Name);
        dest.writeString(CleanName);
        dest.writeDouble(Price);
        dest.writeInt(Amount);
        dest.writeDouble(subTotal);

    }

    protected Item(Parcel in){
        ID = in.readInt();
        Name = in.readString();
        CleanName = in.readString();
        Price = in.readDouble();
        Amount = in.readInt();
        subTotal = in.readDouble();

    }
}

