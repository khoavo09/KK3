package com.example.khoavo.kk3;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Color;
import android.os.Handler;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper myDb;
    List<EditText> amountED = new ArrayList<EditText>();
    private RadioGroup TaxRadioGroup;
    private RadioButton NoTaxRadioButton;
    private Button submitButton,editButton;

    TextView detailsTextView;
    TextView myLabel;
    Button sendButton, closeButton;
    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    private Order myOrder;
    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        myDb = DatabaseHelper.newInstance(this);


        //myDb.clearDatabase();

        // Inserting items
        try {
       /*     myDb.insertData(new Item("Com", 10));
            myDb.insertData(new Item("Ga Mieng", 27));
            myDb.insertData(new Item("Com Suon", 35));
            myDb.insertData(new Item("Mieng Ga", 37));
            myDb.insertData(new Item("Chao Ga", 35));
            myDb.insertData(new Item("Bo Ne", 45));
            myDb.insertData(new Item("Mi Xao Bo", 60));*/
        }
        catch (SQLiteConstraintException e){
            Log.d("Insert: ", "ERROR");
            e.printStackTrace();

        }

       // myDb.updateData(new Item("Com", 92));
      //  myDb.updateContact(new Item("Com", 92));

        init();





        myLabel = (TextView)findViewById(R.id.label);

        NoTaxRadioButton = (RadioButton)findViewById(R.id.NOradioButton);
        NoTaxRadioButton.setChecked(true);
        //  if(MainActivity.myBundle.getBoolean("START")) {


        submitButton = (Button)findViewById(R.id.submitButton);
        // submitButton.performClick();
        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                myOrder = new Order();

                String[] strings = new String[amountED.size()];

                for (int i = 0; i < amountED.size(); i++) {
                    strings[i] = amountED.get(i).getText().toString();
                    if (!strings[i].isEmpty()) {
                        int amount = Integer.parseInt(strings[i]);
                        Item item = myDb.getItem(i + 1);
                        item.setAmount(amount);
                        Order order = new Order(item.getID(), item.getName(), item.getPrice(), amount);
                        if(!NoTaxRadioButton.isChecked()){
                            order.setTax(1);
                        }
                        myOrder.addItem(item);
                    }

                    //  bundle.putString("number", strings[i]);
                }

                if(myOrder.getCount() >0 ) {
                    bundle.putParcelable("ORDER",myOrder);
                    FragmentManager manager = getSupportFragmentManager();
                    //FragmentTransaction
                    FragmentTransaction ft = manager.beginTransaction();

                    DetailsFragment detailsFragment = (DetailsFragment)manager.findFragmentByTag("DetailsFragment");
                    if(detailsFragment == null) {
                        detailsFragment = new DetailsFragment();
                        ft.add(R.id.DetailsLayout,detailsFragment,"DetailsFragment");
                    }
                    else{
                        ft.remove(detailsFragment);
                        detailsFragment = new DetailsFragment();
                        ft.add(R.id.DetailsLayout,detailsFragment,"DetailsFragment");
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


        try {
            findBT();
            openBT();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // send data typed by the user to be printed
        sendButton = (Button)findViewById(R.id.PrintButton);
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
        closeButton = (Button)findViewById(R.id.CloseButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    closeBT();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });



    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }




    // this will find a bluetooth printer device
    void findBT() {

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
            Toast.makeText(getApplicationContext(), "OPEN IN TRY", Toast.LENGTH_SHORT).show();
            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();
            Toast.makeText(getApplicationContext(), "OpenBT", Toast.LENGTH_SHORT).show();

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
            Toast.makeText(getApplicationContext(), "StartWorker", Toast.LENGTH_SHORT).show();

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


    void sendData() throws IOException {
        try {

            // the text typed by the user
            DateFormat dateFormatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
            Date today = new Date();
            String s = dateFormatter.format(today);
            String header = "COM GA KHANH KY\n"
                    +"Dia Chi: 61 Tran Quang Dieu \n" ;
            header += ("Date: " + s + "\n") ;


            String printString ="";
            int i =0;
            ArrayList<Item> localOrder = myOrder.getItemList();

            for(Item temp: localOrder) {
                String orderDetails = localOrder.get(i).getName() + "  " + localOrder.get(i).getAmount();
                orderDetails += "\n";
                printString += orderDetails;
                i++;
            }

            //Fix this later
            myOrder.CalculateTotal();
            String orderTotal = "Total: " + Double.toString(myOrder.getGrandTotal()) + "\n";
          //  if(myOrder.get(0).getTax() == 0){

          //  }
         //   else{
                // orderTotal += ("Tax: " + Double.toString(CalculateTax()));
        //    }
            String PRINTME = header + printString + orderTotal;

            byte[] center = new byte[]{ 0x1b, 0x61, 0x01 };
            Toast.makeText(getApplicationContext(), "sendData", Toast.LENGTH_SHORT).show();
            mmOutputStream.write( center );
            mmOutputStream.write(PRINTME.getBytes());


            // tell the user data were sent
              myLabel.setText("Data sent.");

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
        tv1.setTextColor(Color.RED);
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