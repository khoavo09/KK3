package com.example.khoavo.kk3;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by khoavo on 7/17/17.
 */

public class Order implements Parcelable{

    int tax;
    double grandTotal;
    ArrayList<Item> itemList = new ArrayList<>();
    int count;

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public ArrayList<Item> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<Item> itemList) {
        this.itemList = itemList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    protected Order(Parcel in) {
        //super(in);
       // ID = in.readInt();
       // Name = in.readString();
       // Price = in.readDouble();
       // Amount = in.readInt();
        tax = in.readInt();
        grandTotal = in.readDouble();
        itemList = in.readArrayList(null);
      //  itemList = in.readTypedList(yourList, YourParcelable.CREATOR);
    }

    public Order (){
        int count = 0;

    }

    public Order(int id, String name, double price, int amount) {
    //    super(id, name, price);
    }

    public boolean addItem(Item item){

        if(itemList.add(item)) {
            count++;
            return true;
        }
        else
            return false;
    }

    public double CalculateTax(){
        return grandTotal * 0.5;
    }


    public double CalculateTotal (){
        grandTotal = 0;
        double value;


        for(int i =0; i < itemList.size();i++){
            value = (itemList.get(i).getPrice())  * itemList.get(i).getAmount();
            grandTotal += value;
        }

        if(tax==1) {
            grandTotal = grandTotal + (grandTotal * 0.1);
            return grandTotal;
        }
        else
            return grandTotal;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {;
        dest.writeInt(tax);
        dest.writeDouble(grandTotal);
        dest.writeList(itemList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
