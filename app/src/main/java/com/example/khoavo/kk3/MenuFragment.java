package com.example.khoavo.kk3;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    DatabaseHelper myDb;
    List<EditText> amountED = new ArrayList<EditText>();

    public MenuFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu, container, false);
        myDb = DatabaseHelper.newInstance(getActivity());
        init(v);
      //  if(MainActivity.myBundle.getBoolean("START")) {


        Button submitButton = (Button)v.findViewById(R.id.submitButton);
            submitButton.performClick();
        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            DetailsFragment detailsFragment = new DetailsFragment();
            Bundle bundle = new Bundle();
            ArrayList<Order> OrderList = new ArrayList<>();
            // bundle.putString("number", "TWO");
            bundle.putString("name", "KIO");


            String[] strings = new String[amountED.size()];

            for (int i = 0; i < amountED.size(); i++) {
                strings[i] = amountED.get(i).getText().toString();
                // AmountList.add(Integer.parseInt(amountED.get(i).getText().toString()));
                if (!strings[i].isEmpty()) {
                    int amount = Integer.parseInt(strings[i]);
                    Item item = myDb.getItem(i + 1);
                    Order order = new Order(item.getID(), item.getName(), item.getPrice(), amount);
                    OrderList.add(order);
                }

                //  bundle.putString("number", strings[i]);
            }
            Item kk = myDb.getItem(2);

            bundle.putString("number", amountED.get(0).getText().toString());
            //   bundle.putString("number", Integer.toString(myDb.getItemsCount()));

            //          boolean Tax = MainActivity.myBundle.getBoolean("TAX");
            //          if(!Tax) {
            bundle.putParcelableArrayList("WHAT", OrderList);
            //           }
            //           else{

            //          }
            detailsFragment.setArguments(bundle);
            FragmentManager manager = getFragmentManager();
            manager.beginTransaction().replace(R.id.DetailsLayout, detailsFragment).commit();
        }
       });
   // }
      //  myDb.clearDatabase();
       // MainActivity.myBundle.putBoolean("START",false);
        return v;
    }


    public void init(View v) {
        Log.d("READING: ", "Reading all items........");
        List<Item> items = myDb.getAllItems();
        for (Item cn : items) {
            String log = "Name: " + cn.getName() + " ,Phone: " + cn.getPrice();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }

        TableLayout stk = (TableLayout) v.findViewById(R.id.table_main);
        TableRow tbrow0 = new TableRow(getActivity());
        TextView tv0 = new TextView(getActivity());
        tv0.setText(" STT ");
        tv0.setTextColor(Color.WHITE);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(getActivity());
        tv1.setText(" NAME ");
        tv1.setTextColor(Color.WHITE);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(getActivity());
        tv2.setText(" Unit Price ");
        tv2.setTextColor(Color.WHITE);
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(getActivity());
        tv3.setText(" AMOUNT ");
        tv3.setTextColor(Color.WHITE);
        tbrow0.addView(tv3);
        stk.addView(tbrow0);
        int i =0;

        for (Item cn : items) {
            TableRow tbrow = new TableRow(getActivity());
            TextView t1v = new TextView(getActivity());
            t1v.setText("" + i);
            t1v.setTextColor(Color.WHITE);
            t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);
            TextView t2v = new TextView(getActivity());
            t2v.setText(cn.getName());
            t2v.setTextColor(Color.WHITE);
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);
            TextView t3v = new TextView(getActivity());
            t3v.setText(String.valueOf(cn.getPrice()));
            t3v.setTextColor(Color.WHITE);
            t3v.setGravity(Gravity.CENTER);
            tbrow.addView(t3v);
            EditText t4v = new EditText(getActivity());
            t4v.setInputType(InputType.TYPE_CLASS_NUMBER);
            amountED.add(t4v);
            //  t4v.setText("" + i * 15 / 32 * 10);
            t4v.setTextColor(Color.WHITE);
            t4v.setGravity(Gravity.CENTER);
            tbrow.addView(t4v);
            stk.addView(tbrow);
            i++;
        }

    }
}
