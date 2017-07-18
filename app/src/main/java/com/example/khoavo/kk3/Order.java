package com.example.khoavo.kk3;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by khoavo on 7/17/17.
 */

public class Order extends Item implements Parcelable{
    int Amount;
    int Tax;
    double grandTotal;

    public int Tax() {
        return Tax;
    }

    public void setTax(int tax) {
        Tax = tax;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }

    protected Order(Parcel in) {
        //super(in);
        ID = in.readInt();
        Name = in.readString();
        Price = in.readDouble();
        Amount = in.readInt();
        Tax = in.readInt();
        grandTotal = in.readDouble();
    }

    public Order(int id, String name, double price, int amount) {
        super(id, name, price);
        Amount = amount;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      //  super.writeToParcel(dest, flags);
        dest.writeInt(ID);
        dest.writeString(Name);
        dest.writeDouble(Price);
        dest.writeInt(Amount);
        dest.writeInt(Tax);
        dest.writeDouble(grandTotal);
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
