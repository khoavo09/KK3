package com.example.khoavo.kk3;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
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
    ArrayList<Order> order;
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
/*
        try {
            findBT();
            openBT();
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        // send data typed by the user to be printed
        sendButton = (Button)v.findViewById(R.id.PrintButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    sendData();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // close bluetooth connection
        closeButton = (Button)v.findViewById(R.id.CloseButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    closeBT();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        */
        return v;
    }


    // this will find a bluetooth printer device
   /* void findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if(mBluetoothAdapter == null) {
                myLabel.setText("No bluetooth adapter available");
            }

            if(!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if(pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    if (device.getName().equals("RPP300-E")) {
                        mmDevice = device;
                        break;
                    }
                }
            }

            myLabel.setText("Bluetooth device found.");

        }catch(Exception e){
            e.printStackTrace();
        }
    }


    // tries to open a connection to the bluetooth printer device
    void openBT() throws IOException {
        try {
            Toast.makeText(getContext(), "OPEN IN TRY", Toast.LENGTH_SHORT).show();
            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();
            Toast.makeText(getContext(), "OpenBT", Toast.LENGTH_SHORT).show();

            beginListenForData();

            myLabel.setText("Bluetooth Opened");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];
            Toast.makeText(getContext(), "StartWorker", Toast.LENGTH_SHORT).show();

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
                                                myLabel.setText(data);
                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });
         //   Toast.makeText(getContext(), "StartWorker", Toast.LENGTH_SHORT).show();
            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // this will send text data to be printed by the bluetooth printer
    void sendData() throws IOException {
        try {
          //  Toast.makeText(getContext(), "sendData", Toast.LENGTH_SHORT).show();

            // the text typed by the user
            DateFormat dateFormatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
            Date today = new Date();
            String s = dateFormatter.format(today);
            String header = "COM GA KHANH KY\n"
                            +"Dia Chi: 61 Tran Quang Dieu \n" ;
            header += ("Date: " + s + "\n") ;


            String printString ="";
            int i =0;
            for(Order temp: order) {
                String orderDetails = order.get(i).getName() + "  " + order.get(i).getAmount();
                orderDetails += "\n";
                printString += orderDetails;
                i++;
            }

            String orderTotal = "Total: " + Double.toString(CalculateTotal(order,order.get(0).getTax())) + "\n";
            if(order.get(0).getTax() == 0){

            }
            else{
               // orderTotal += ("Tax: " + Double.toString(CalculateTax()));
            }
            String PRINTME = header + printString + orderTotal;

            byte[] center = new byte[]{ 0x1b, 0x61, 0x01 };
            mmOutputStream.write( center );
            mmOutputStream.write(PRINTME.getBytes());


            // tell the user data were sent
          //  myLabel.setText("Data sent.");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    // close the connection to bluetooth printer.
    void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
            myLabel.setText("Bluetooth Closed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/



    public double CalculateTax(Double total){
        return total * 0.5;
    }


    public double CalculateTotal (ArrayList<Order> order, int Tax){
        double total = 0;
        double value;


        for(int i =0; i < order.size();i++){
            value = (order.get(i).getPrice())  * order.get(i).getAmount();
            total += value;
        }

        if(Tax==1) {
            total = total + (total * 0.1);
            return total;
        }
        else
            return total;
    }




    public void display(View v){
        Bundle bundle = getArguments();
        if(bundle!= null) {
            order = getArguments().getParcelableArrayList("ORDER");
            int Tax = order.get(0).getTax();
            double total = CalculateTotal(order,Tax);
            detailsTextView = (TextView)v.findViewById(R.id.textViewDetails);
            for(int i=0; i < order.size();i++)
                detailsTextView.append(order.get(i).getName().toString() + " " +
                        Double.toString(order.get(i).getPrice()) + " " +
                        Integer.toString(order.get(i).getAmount()) + "\n");

            detailsTextView.append("Total: " + Double.toString(total));
        }
    // put all the display crap here
    }



}
