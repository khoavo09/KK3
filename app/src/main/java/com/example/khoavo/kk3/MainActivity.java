package com.example.khoavo.kk3;

import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        myDb = DatabaseHelper.newInstance(this);



        // Inserting items
        try {
            myDb.insertData(new Item("Com", 10));
            myDb.insertData(new Item("Ga Mieng", 27));
            myDb.insertData(new Item("Com Suon", 35));
            myDb.insertData(new Item("Mieng Ga", 37));
            myDb.insertData(new Item("Chao Ga", 35));
            myDb.insertData(new Item("Bo Ne", 45));
            myDb.insertData(new Item("Mi Xao Bo", 60));
        }
        catch (SQLiteConstraintException e){
            Log.d("Insert: ", "ERROR");
            e.printStackTrace();

        }

        // Reading all contacts
        Log.d("Reading: ", "Reading all items..");
        List<Item> items = myDb.getAllItems();

        for (Item cn : items) {
            String log = "Name: " + cn.getName() + " ,Phone: " + cn.getPrice();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }



        MenuFragment menuFragment = new MenuFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.MenuLayout,menuFragment,menuFragment.getTag()).commit();

        //DetailsFragment detailsFragment = new DetailsFragment();
        //manager.beginTransaction().replace(R.id.DetailsLayout,detailsFragment,detailsFragment.getTag()).commit();

      //  ButtonFragment buttonFragment = new ButtonFragment();
      //  manager.beginTransaction().replace(R.id.ButtonLayout, buttonFragment,buttonFragment.getTag()).commit();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}