package com.example.khoavo.kk3;


/**
 * Created by khoavo on 7/8/17.
 */

public class Item {
    int ID;
    String Name;
    double Price;

    public Item(){

    }

    public Item(String name, double price) {
        Name = name;
        Price = price;
    }

    public Item(int id, String name, double price) {
        ID = id;

        Name = name;
        Price = price;
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

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

}
