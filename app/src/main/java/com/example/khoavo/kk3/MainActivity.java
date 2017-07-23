package com.example.khoavo.kk3;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper myDb;
    List<EditText> amountED = new ArrayList<EditText>();
    private RadioGroup TaxRadioGroup;
    private RadioButton NoTaxRadioButton;
    private Button submitButton,editButton;

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

        init();



        


        NoTaxRadioButton = (RadioButton)findViewById(R.id.NOradioButton);
        NoTaxRadioButton.setChecked(true);
        //  if(MainActivity.myBundle.getBoolean("START")) {


        submitButton = (Button)findViewById(R.id.submitButton);
        // submitButton.performClick();
        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                ArrayList<Order> OrderList = new ArrayList<>();

                String[] strings = new String[amountED.size()];

                for (int i = 0; i < amountED.size(); i++) {
                    strings[i] = amountED.get(i).getText().toString();
                    if (!strings[i].isEmpty()) {
                        int amount = Integer.parseInt(strings[i]);
                        Item item = myDb.getItem(i + 1);
                        Order order = new Order(item.getID(), item.getName(), item.getPrice(), amount);
                        if(!NoTaxRadioButton.isChecked()){
                            order.setTax(1);
                        }
                        OrderList.add(order);
                    }

                    //  bundle.putString("number", strings[i]);
                }

                if(OrderList.size() >0 ) {
                    //DetailsFragment detailsFragment;
                    bundle.putParcelableArrayList("ORDER", OrderList);
                    //detailsFragment.setArguments(bundle);
                    FragmentManager manager = getSupportFragmentManager();
                    //FragmentTransaction
                    FragmentTransaction ft = manager.beginTransaction();

                    DetailsFragment detailsFragment = (DetailsFragment)manager.findFragmentByTag("tag");
                    if(detailsFragment == null) {
                        detailsFragment = new DetailsFragment();
                        ft.add(R.id.DetailsLayout,detailsFragment,"tag");
                    }
                    else{
                        ft.remove(detailsFragment);
                        detailsFragment = new DetailsFragment();
                        ft.add(R.id.DetailsLayout,detailsFragment,"tag");
                        Toast.makeText(getApplicationContext(), "ELSE.....", Toast.LENGTH_SHORT).show();
                    }
                    ft.commit();
                    detailsFragment.setArguments(bundle);
                    //  manager.beginTransaction().replace(R.id.DetailsLayout, detailsFragment).commit();
                }
                else
                    Toast.makeText(getApplicationContext(), "Please inpunt the amounts", Toast.LENGTH_SHORT).show();

            }
        });

        editButton = (Button)findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,EditActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }



    public void init() {
        List<Item> items = myDb.getAllItems();
        TableLayout stk = (TableLayout)findViewById(R.id.table_main);
        TableRow tbrow0 = new TableRow(this);
        //TableRow.LayoutParams tlp = new TableRow.LayoutParams(20,30);

        TextView tv0 = new TextView(this);
        tv0.setText(" STT ");
        tv0.setTextColor(Color.WHITE);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        //tv1.setWidth(400);
        tv1.setText(" NAME ");
        tv1.setGravity(Gravity.CENTER_HORIZONTAL);
        tv1.setTextColor(Color.WHITE);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText(" Unit Price ");
        tv2.setTextColor(Color.WHITE);
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(this);
        tv3.setText(" AMOUNT ");
        tv3.setTextColor(Color.WHITE);
        tbrow0.addView(tv3);
        stk.addView(tbrow0);

        int i =0;
        for (Item cn : items) {
            TableRow tbrow = new TableRow(this);
            TextView t1v = new TextView(this);
            t1v.setText("" + i);
            t1v.setTextColor(Color.WHITE);
            t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);
            TextView t2v = new TextView(this);
            t2v.setText(cn.getName());
            t2v.setTextColor(Color.WHITE);
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);
            TextView t3v = new TextView(this);
            t3v.setText(String.valueOf(cn.getPrice()));
            t3v.setTextColor(Color.WHITE);
            t3v.setGravity(Gravity.CENTER);
            tbrow.addView(t3v);
            EditText t4v = new EditText(this);
            t4v.setInputType(InputType.TYPE_CLASS_NUMBER);
            amountED.add(t4v);
            t4v.setTextColor(Color.WHITE);
            t4v.setGravity(Gravity.CENTER);
            tbrow.addView(t4v);
            stk.addView(tbrow);
            i++;
        }
    }
}