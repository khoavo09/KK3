package com.example.khoavo.kk3;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    Button submitButton;
    ArrayList<EditText> NameED = new ArrayList<EditText>();
    ArrayList<EditText> PriceED = new ArrayList<EditText>();
    ArrayList<Item> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        myDb = DatabaseHelper.newInstance(this);

        submitButton = (Button)findViewById(R.id.submitButtonEdit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   myDb.updateData(new Item ("Com", 70));
                for(int i=0; i< NameED.size();i++){
                    String name = NameED.get(i).getText().toString();
                    if( !PriceED.get(i).getText().toString().equals("") && !name.equals("")) {
                        Double price = Double.parseDouble(PriceED.get(i).getText().toString());
                        Item localItem = new Item(name, price);
                        //itemList.add(localItem);
                        if(price == 999.999)
                            myDb.deleteItem(localItem);
                        else {
                            int flag = myDb.updateData(localItem);
                            if (flag == 0)
                                myDb.insertData(localItem);
                        }

                    }
                }
               // Intent intent = new Intent(EditActivity.this,MainActivity.class);
               // EditActivity.this.startActivity(intent);
            }
        });


        init();
    }



    public void init() {
        List<Item> items = myDb.getAllItems();
        TableLayout stk = (TableLayout)findViewById(R.id.table_main_edit);
        TableRow tbrow0 = new TableRow(this);
        //TableRow.LayoutParams tlp = new TableRow.LayoutParams(20,30);

        TextView tv0 = new TextView(this);
        tv0.setText(" STT ");
        tv0.setTextColor(Color.WHITE);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setWidth(200);
        tv1.setText(" NAME ");
        //tv1.setGravity(Gravity.CENTER_HORIZONTAL);
        tv1.setTextColor(Color.WHITE);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText(" Unit Price ");
        tv2.setTextColor(Color.WHITE);
        tbrow0.addView(tv2);
        stk.addView(tbrow0);

        int i =0;
        for (Item cn : items) {
            TableRow tbrow = new TableRow(this);
            TextView t1v = new TextView(this);
            t1v.setText("" + i);
            t1v.setTextColor(Color.WHITE);
            t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);
            EditText t2v = new EditText(this);
            t2v.setText(cn.getName());
            t2v.setTextColor(Color.WHITE);
            t2v.setGravity(Gravity.CENTER);
            NameED.add(t2v);
            tbrow.addView(t2v);
            EditText t3v = new EditText(this);
            t3v.setText(String.valueOf(cn.getPrice()));
            t3v.setTextColor(Color.WHITE);
            t3v.setGravity(Gravity.CENTER);
            t3v.setInputType(InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL);
            PriceED.add(t3v);
            tbrow.addView(t3v);
            stk.addView(tbrow);
            i++;
        }

        for(int k=0; k< 1; k++){
            TableRow tbrow = new TableRow(this);
            TextView t1v = new TextView(this);
            t1v.setText("" + (i+k +1));
            t1v.setTextColor(Color.WHITE);
            t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);
            EditText t2v = new EditText(this);
            //t2v.setText(cn.getName());
            t2v.setTextColor(Color.WHITE);
            t2v.setGravity(Gravity.CENTER);
            NameED.add(t2v);
            tbrow.addView(t2v);
            EditText t3v = new EditText(this);
           // t3v.setText(String.valueOf(cn.getPrice()));
            t3v.setTextColor(Color.WHITE);
            t3v.setGravity(Gravity.CENTER);
            t3v.setInputType(InputType.TYPE_CLASS_NUMBER);
            PriceED.add(t3v);
            tbrow.addView(t3v);
            stk.addView(tbrow);
        }
    }
}
