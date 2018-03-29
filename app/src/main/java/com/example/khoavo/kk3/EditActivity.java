package com.example.khoavo.kk3;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    Button submitButton;
    ArrayList<EditText> NameED = new ArrayList<EditText>();
    ArrayList<EditText> PriceED = new ArrayList<EditText>();
    List<Item> itemList = new ArrayList<>();
    int message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        myDb = DatabaseHelper.newInstance(this);

        itemList = myDb.getAllItems();
        submitButton = (Button)findViewById(R.id.submitButtonEdit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   myDb.updateData(new Item ("Com", 70));
                for(int i=0; i< NameED.size();i++){
                    String name = NameED.get(i).getText().toString();
                    String myString="";

                    try {
                        myString = URLDecoder.decode(name, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    if( !PriceED.get(i).getText().toString().equals("") && !myString.equals("")) {
                        String string_price = PriceED.get(i).getText().toString();
                        Double price = Double.parseDouble(PriceED.get(i).getText().toString());
                        Item localItem = new Item(myString, price);
                        if(i > (myDb.getItemsCount() -1))
                            localItem.setCleanName(name);
                        else
                            localItem.setCleanName(itemList.get(i).getCleanName());
                        //itemList.add(localItem);
                        if(string_price.equals( "0.0")) {
                            myDb.deleteItem(localItem);
                            message = -1;
                            //Toast.makeText(getApplicationContext(),"Deleted the item",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            int flag = myDb.updateData(localItem);
                            if (flag == 0) {
                                myDb.insertData(localItem);
                                message = 0;
                               // Toast.makeText(getApplicationContext(), "Inserted the items", Toast.LENGTH_SHORT).show();
                            }
                            else
                                message = 1;
                                //Toast.makeText(getApplicationContext(),"Updated the item",Toast.LENGTH_SHORT).show();

                        }
                    }
                }

                if(message == -1)
                    Toast.makeText(getApplicationContext(),R.string.deleted_items_toast,Toast.LENGTH_SHORT).show();
                else if(message ==0)
                    Toast.makeText(getApplicationContext(), R.string.inserted_items_toast, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(),R.string.updated_items_toast,Toast.LENGTH_SHORT).show();

                finish();
                Intent intent = new Intent(EditActivity.this,MainActivity.class);
                EditActivity.this.startActivity(intent);
            }
        });


        init();
    }


    //Set up the table for the screen
    public void init() {
        List<Item> items = myDb.getAllItems();
        TableLayout stk = (TableLayout)findViewById(R.id.table_main_edit);
        TableRow tbrow0 = new TableRow(this);
        //TableRow.LayoutParams tlp = new TableRow.LayoutParams(20,30);

        TextView tv0 = new TextView(this);
        tv0.setText(R.string.num_table_header);
        tv0.setTextColor(Color.WHITE);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setMinWidth(300);
        tv1.setText(R.string.name_table_header);
        tv1.setGravity(Gravity.CENTER_HORIZONTAL);
        tv1.setTextColor(Color.WHITE);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setGravity(Gravity.CENTER_HORIZONTAL);
        tv2.setText(R.string.unit_price_table_header);
        tv2.setTextColor(Color.WHITE);
        tbrow0.addView(tv2);
        stk.addView(tbrow0);

        int i =0;
        //Display all the items in the menu
        for (Item cn : items) {
            TableRow tbrow = new TableRow(this);
            TextView t1v = new TextView(this);
            t1v.setText("" + i);
            t1v.setTextColor(Color.WHITE);
            t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);
            EditText t2v = new EditText(this);
            t2v.setText(cn.getName());
            t2v.setEnabled(false);
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


        int maxLength = 19;
        //Extra space to add new items
        for(int k=0; k< 4; k++){
            TableRow tbrow = new TableRow(this);
            TextView t1v = new TextView(this);
            t1v.setText("" + (i+k));
            t1v.setTextColor(Color.WHITE);
            t1v.setGravity(Gravity.CENTER);
            t1v.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
            tbrow.addView(t1v);
            EditText t2v = new EditText(this);
            //t2v.setText(cn.getName());
            t2v.setTextColor(Color.WHITE);
            t2v.setGravity(Gravity.CENTER);
            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(20);
            t2v.setFilters(filterArray);
            NameED.add(t2v);
            tbrow.addView(t2v);
            EditText t3v = new EditText(this);
           // t3v.setText(String.valueOf(cn.getPrice()));
            t3v.setTextColor(Color.WHITE);
            t3v.setGravity(Gravity.CENTER);
            t3v.setInputType(InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL);
            PriceED.add(t3v);
            tbrow.addView(t3v);
            stk.addView(tbrow);
        }
    }
}
