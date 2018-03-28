package com.example.khoavo.kk3;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {


    TextView detailsTextView;
    TextView myLabel;
    Button sendButton, closeButton;
    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    Order myOrder;
    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;


    public DetailsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View v = inflater.inflate(R.layout.fragment_details, container, false);
        myLabel = (TextView)v.findViewById(R.id.label);
        display(v);
        return v;
    }





    public void display(View v){
        Bundle bundle = getArguments();
        if(bundle!= null) {
            myOrder = getArguments().getParcelable("ORDER");
            Double Tax = myOrder.getTax();
            myOrder.CalculateTotal();
           // double total = myOrder.getGrandTotal();
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Consolas.ttf");
            ArrayList<Item> localOrder = myOrder.getItemList();
            detailsTextView = (TextView)v.findViewById(R.id.textViewDetails);
            detailsTextView.setTextSize(17);
            detailsTextView.setTypeface(tf);
            detailsTextView.append(String.format("%-5s%-20s%5s%4s%10s", "STT","Ten Hang", "DG", "SL", "T.Tien\n\n"));
            for(int i=0; i < localOrder.size();i++)
                detailsTextView.append(String.format("%-5d%-20s%5.1f%4d%9.1f\n",i+1, localOrder.get(i).getCleanName(),
                        localOrder.get(i).getPrice(),localOrder.get(i).getAmount(),localOrder.get(i).getSubTotal()));

            detailsTextView.append("--------------------------------------------\n");
            if(myOrder.getIsTax() == 1) {
                detailsTextView.append(String.format("%25s %17.1f\n","Cong:", myOrder.getGrandTotal_beforeTax()));
                detailsTextView.append(String.format("%25s %17.1f\n","Thue:", myOrder.getTax()));
            }

            detailsTextView.append(String.format("%25s %17.1f\n","Tong Cong:", myOrder.getGrandTotal()));
        }
    // put all the display crap here
    }



}
