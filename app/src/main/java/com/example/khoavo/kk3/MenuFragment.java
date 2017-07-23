package com.example.khoavo.kk3;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    DatabaseHelper myDb;
    List<EditText> amountED = new ArrayList<EditText>();
    private RadioGroup TaxRadioGroup;
    private RadioButton NoTaxRadioButton;
    private Button submitButton,editButton;

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
        NoTaxRadioButton = (RadioButton)v.findViewById(R.id.NOradioButton);
        NoTaxRadioButton.setChecked(true);
      //  if(MainActivity.myBundle.getBoolean("START")) {


        submitButton = (Button)v.findViewById(R.id.submitButton);
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
                    FragmentManager manager = getFragmentManager();
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
                        Toast.makeText(getContext(), "ELSE.....", Toast.LENGTH_SHORT).show();
                    }
                    ft.commit();
                    detailsFragment.setArguments(bundle);
                  //  manager.beginTransaction().replace(R.id.DetailsLayout, detailsFragment).commit();
                }
                else
                    Toast.makeText(getContext(), "Please inpunt the amounts", Toast.LENGTH_SHORT).show();

        }
       });

        editButton = (Button)v.findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),EditActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }


    public void init(View v) {
        List<Item> items = myDb.getAllItems();
        TableLayout stk = (TableLayout) v.findViewById(R.id.table_main);
        TableRow tbrow0 = new TableRow(getActivity());
        //TableRow.LayoutParams tlp = new TableRow.LayoutParams(20,30);

        TextView tv0 = new TextView(getActivity());
        tv0.setText(" STT ");
        tv0.setTextColor(Color.WHITE);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(getActivity());
        //tv1.setWidth(400);
        tv1.setText(" NAME ");
        tv1.setGravity(Gravity.CENTER_HORIZONTAL);
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
            t4v.setTextColor(Color.WHITE);
            t4v.setGravity(Gravity.CENTER);
            tbrow.addView(t4v);
            stk.addView(tbrow);
            i++;
        }
    }
}
