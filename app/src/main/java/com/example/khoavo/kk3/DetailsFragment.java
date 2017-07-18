package com.example.khoavo.kk3;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {


    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View v = inflater.inflate(R.layout.fragment_details, container, false);

        Bundle bundle = getArguments();
        if(bundle!= null) {
            String number = bundle.getString("number");
            String name = bundle.getString("name");
            Toast.makeText(getContext(), "name" + name + "number: " + number, Toast.LENGTH_SHORT).show();
            ArrayList<Order> order = getArguments().getParcelableArrayList("WHAT");
            boolean Tax = MainActivity.myBundle.getBoolean("TAX");
            double total = CalculateTotal(order,Tax);
            TextView textView = (TextView)v.findViewById(R.id.textViewDetails);
            for(int i=0; i < order.size();i++)
                textView.append(order.get(i).getName().toString() + " " +
                        Double.toString(order.get(i).getPrice()) + " " +
                        Integer.toString(order.get(i).getAmount()) + "\n");

            textView.append("Total: " + Double.toString(total));

        }


        else
            Toast.makeText(getContext(), "HELOOOOOO", Toast.LENGTH_SHORT).show();

        return v;
    }

    public double CalculateTotal (ArrayList<Order> order, boolean Tax){
        double total = 0;
        double value = 0;


        for(int i =0; i < order.size();i++){
            value = (order.get(i).getPrice())  * order.get(i).getAmount();
            total += value;
        }

        if(Tax) {
            total = total + (total * 0.1);
            return total;
        }
        else
            return total;
    }

    public void display(){
    // put all the display crap here
    }



}
