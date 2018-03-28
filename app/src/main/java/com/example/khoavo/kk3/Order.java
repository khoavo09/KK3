package com.example.khoavo.kk3;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by khoavo on 7/17/17.
 */

public class Order implements Parcelable{

    int tableNumber;
    int isTax;
    double tax;
    double grandTotal_beforeTax;
    double grandTotal;
    ArrayList<Item> itemList = new ArrayList<Item>();
    int count;

    public double getTax() {
        calculateTax();
        return tax;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getIsTax() {
        return isTax;
    }

    public void setisTax(int tax) {
        this.isTax = tax;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public double getGrandTotal_beforeTax() {
        return grandTotal_beforeTax;
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
        tableNumber = in.readInt();
        isTax = in.readInt();
        tax = in.readDouble();
        grandTotal = in.readDouble();
        in.readTypedList(itemList,Item.CREATOR);
    }

    public Order (){
        count = 0;
        tableNumber = 0;

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

    public void calculateTax(){
        tax = grandTotal * 0.01;
    }


    public void CalculateTotal (){
        grandTotal = 0;
        double value;


        for(int i =0; i < itemList.size();i++){
            value = (itemList.get(i).getPrice())  * itemList.get(i).getAmount();
            grandTotal += value;
        }
        grandTotal_beforeTax = grandTotal;

        if(isTax==1) {
            grandTotal = grandTotal + (grandTotal * 0.01);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(tableNumber);
        dest.writeInt(isTax);
        dest.writeDouble(tax);
        dest.writeDouble(grandTotal);
        dest.writeTypedList(itemList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
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
