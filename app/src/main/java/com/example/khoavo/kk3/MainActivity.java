package com.example.khoavo.kk3;
import com.RT_Printer.BluetoothPrinter.BLUETOOTH.BluetoothPrintDriver;
import com.android.print.sdk.Barcode;
import com.android.print.sdk.CanvasPrint;
import com.android.print.sdk.FontProperty;
import com.android.print.sdk.PrinterInstance;
import com.android.print.sdk.PrinterConstants.Connect;
import com.android.print.sdk.PrinterType;
import com.android.print.sdk.PrinterConstants.Command;
import com.android.print.sdk.PrinterConstants.BarcodeType;


import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private DatabaseHelper myDb;
    List<EditText> amountED = new ArrayList<EditText>();
    private RadioGroup TaxRadioGroup;
    private RadioButton NoTaxRadioButton;
    private Button submitButton,editButton,connectButton;
    private EditText tableNumber;


    private Context mContext;
    private static boolean isConnected;
    private PrinterOperation myOpertion;
    private PrinterInstance mPrinter;
    private int currIndex = 0;
    private ProgressDialog dialog;



    public static final int CONNECT_DEVICE = 1;
    public static final int ENABLE_BT = 2;
/*
    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
*/
    private static final String TAG = "Main Activity";
    private static final boolean D = true;
    TextView detailsTextView;
    TextView mTitle;
    Button printButton, closeButton;
    private Order myOrder;

    // Name of the connected device
    private String mConnectedDeviceName = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothPrintDriver mChatService = null;

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);

        mContext = this;
        myDb = DatabaseHelper.newInstance(this);

     /* mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }*/
        //String myString = "";
        //ArrayList<Item> menu = new ArrayList<>();

        //myDb.clearDatabase();

        if(myDb.getItemsCount() == 0 || !myDb.isExisting()){


        Item [] menu = new Item []{
                new Item("Cơm Gà","Com Ga", 10),
                new Item("Gà Miếng","Ga Mieng", 28),
                new Item("Lòng Gà","Long Ga", 22),
                new Item("Gà Nướng","Ga Nuong", 105),
                new Item("Gà Nguyên Con","Ga Nguyen Con", 120),
                new Item("Cơm Nhiều","Com Nhieu", 12),
                new Item("Cơm Sườn","Com Suon", 35),
                new Item("Miến Gà","Mien Ga", 38),
                new Item("Cháo Gà","Chao Ga", 35),
                new Item("Bò Né","Bo Ne", 45),
                new Item("Mì Xào Bò","Mi Xao Bo",60 ),
                new Item("Khăn","Khan",2 ),
                new Item("Cơm Niêu","Com Nieu", 13),
                new Item("Gà Lá Giang Lớn","Ga La Giang Lon", 80),
                new Item("Gà Lá Giang Nhỏ","Ga La Giang Nho", 45),
                new Item("Canh Chua Lớn","Canh Chua Lon", 110),
                new Item("Canh Chua Nhỏ","Canh Chua Nho", 70),
                new Item("Canh Khổ Qua Lớn","Canh Kho Qua Lon", 80),
                new Item("Canh Khổ Qua Nhỏ","Canh Kho Qua Nho", 45),
                new Item("Canh Cải Lòng Gà Lớn","Canh Cai Long Ga Lon", 60),
                new Item("Canh Cải Lòng Gà Nhỏ","Canh Cai Long Ga Nho", 35),
                new Item("Mướp Xào Lòng Lớn","Muop Xao Long Lon", 70),
                new Item("Mướp Xào Lòng Nhỏ","Muop Xao Long Nho", 50),
                new Item("Mực Xào Lớn","Muc Xao Lon", 160),
                new Item("Mực Xào Nhỏ","Muc Xao Nho", 80),
                new Item("Bò Xào Lớn","Bo Xao Lon", 120),
                new Item("Bò Xào Nhỏ","Bo Xao Nho", 70),
                new Item("Cá Kho Tộ","Ca Kho To", 120),
                new Item("Gà Kho Xả Ớt Lớn","Ga Kho Xa Ot Lon ", 210),
                new Item("Gà Kho Xả Ớt Nhỏ","Ga Kho Xa Ot Nho", 110),
                new Item("Thịt Kho Trứng","Thit Kho Trung", 100),
                new Item("Thịt Luộc Mắm Nêm Lớn","Thit Luoc Mam Nem Lon", 50),
                new Item("Thịt Luộc Mắm Nêm Nhỏ","Thit Luoc Mam Nem Nho", 40),
                new Item("Sườn Chiên Mặn Lớn","Suon Chien Man Lon", 120),
                new Item("Sườn Chiên Mặn Nhỏ","Suon Chien Man Nho", 60),
                new Item("Cá Chiên","Ca Chien", 80),
                new Item("Gà Chiên","Ga Chien", 120),
                new Item("Tôm Ram Lớn","Tom Ram Lon", 160),
                new Item("Tôm Ram Nhỏ","Tom Ram Nho", 100),
                new Item("Bầu Luộc Trứng Lớn","Bau Luoc Trung Lon", 100),
                new Item("Bầu Luộc Trứng Nhỏ","Bau Luoc Trung Nho", 70),
                new Item("Rau Muống Xào Lớn","Rau Muong Xao Lon", 40),
                new Item("Rau Muống Xào Nhỏ","Rau Muong Xao Nho", 30),
                new Item("Nước Suối Mặn","Nuoc Suoi Man",10),
                new Item("Nước Suối Ngọt","Nuoc Suoi Ngot",12),
                new Item("Nước Khoáng","Nuoc Khoang",8),
                new Item("Bò Húc","Bo Huc",15),
                new Item("Nước Ngọt Lon","Nuoc Ngot Lon",12),
                new Item("Bia Tiger","Bia Tiger",16),
                new Item("Bia Heineken","Bia Heineken",20),
                new Item("Bia Sài Gòn","Bia Sai Gon",15),
        };
       // myDb.clearDatabase();
        try {
            for(int i=0;i<51;i++)
            menu[i].setName( URLDecoder.decode(menu[i].getName(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        // Inserting items
        try {
            for(int i =0;i< 51;i++){
                myDb.insertData(menu[i]);
            }

        }
        catch (SQLiteConstraintException e){
            Log.d("Insert: ", "ERROR");
            e.printStackTrace();

        }

        }

        //Toast.makeText(this, myDb.getItemsCount(), Toast.LENGTH_LONG).show();

        init();



        mTitle = (TextView)findViewById(R.id.label);
        tableNumber = (EditText)findViewById(R.id.table_num_ed) ;
        NoTaxRadioButton = (RadioButton)findViewById(R.id.NOradioButton);
        NoTaxRadioButton.setChecked(true);
        connectButton = (Button)findViewById(R.id.connectButton) ;
        submitButton = (Button)findViewById(R.id.submitButton);
        editButton = (Button)findViewById(R.id.editButton);


        // send data typed by the user to be printed
        printButton = (Button)findViewById(R.id.PrintButton);
        printButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);

                    // set title
                    alertDialogBuilder.setTitle(R.string.printing_confirmation_dialog_title);

                    // set dialog message
                    alertDialogBuilder
                            .setMessage(R.string.printing_confirmation_dialog_message)
                            .setCancelable(false)
                            .setPositiveButton(R.string.yes_label,new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, close
                                    // current activity
                                    try {
                                    sendData();
                                        for (int i=0;i< amountED.size();i++){
                                            amountED.get(i).setText("");
                                        }
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton(R.string.no_label,new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

            }


        });
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
                        myOrder.addItem(item);
                    }
                    //  bundle.putString("number", strings[i]);
                }
                if(!NoTaxRadioButton.isChecked()){
                    myOrder.setisTax(1);
                }
                if(!tableNumber.getText().toString().isEmpty())
                    myOrder.setTableNumber(Integer.parseInt(tableNumber.getText().toString()));



                if(myOrder.getCount() >0 && myOrder.getTableNumber() != 0) {
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
                    }
                    ft.commit();
                    detailsFragment.setArguments(bundle);
                    printButton.setEnabled(true);

                    for(int i=0;i<amountED.size();i++){
                        amountED.get(i).getText().clear();
                    }
                    tableNumber.getText().clear();

                    //  manager.beginTransaction().replace(R.id.DetailsLayout, detailsFragment).commit();
                }
                else if(myOrder.getCount() ==0)
                    Toast.makeText(getApplicationContext(), R.string.please_input_amounts, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), R.string.table_number_reminder_text, Toast.LENGTH_SHORT).show();

            }
        });

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serverIntent = null;
                openConn();
                // Launch the DeviceListActivity to see devices and do scan
               // serverIntent = new Intent(MainActivity.this, DeviceListActivity.class);
               // startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected) {
                    myOpertion.close();
                }
                Intent intent = new Intent(MainActivity.this,EditActivity.class);
              //  startActivityForResult(intent,1);
              //  init();
                finish();
                MainActivity.this.startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        switch (requestCode) {
            case CONNECT_DEVICE:
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    dialog.show();
                    new Thread(new Runnable(){
                        public void run() {
                            myOpertion.open(data);
                        }
                    }).start();
                }
                break;
            case ENABLE_BT:
                if (resultCode == AppCompatActivity.RESULT_OK){
                    myOpertion.chooseDevice();
                }else{
                    Toast.makeText(this, "NO BT", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void updateButtonState(){
        if(!isConnected)
        {
            String connStr = getResources().getString(R.string.connect);
            switch (currIndex) {
                case 0:
                    connStr = String.format(connStr, "Bluetooth");
                    break;
                case 1:
                   // connStr = String.format(connStr, "Wifi");
                    break;
                case 2:
             //       connStr = String.format(connStr, btnUsb.getText());
                    break;
                default:
                    break;
            }
            connectButton.setText(connStr);
        }else{
            connectButton.setText(R.string.disconnect);
        }

        //btnBluetooth.setEnabled(!isConnected);
        //btnWifi.setEnabled(!isConnected);
        //btnUsb.setEnabled(!isConnected);

        printButton.setEnabled(isConnected);
    }

    private void openConn() {
        if (!isConnected) {
            switch (currIndex) {
                case 0: // bluetooth
                    myOpertion = new BluetoothOperation(MainActivity.this, mHandler);
                    //myOpertion = new WifiOperation(MainActivity.this, mHandler);
                    break;
                //implement this later
                case 1: // wifi
                    //myOpertion = new WifiOperation(MainActivity.this, mHandler);
                    break;
                default:
                    break;
            }
            myOpertion.chooseDevice();
        } else {
            myOpertion.close();
            myOpertion = null;
            mPrinter = null;
        }

    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Connect.SUCCESS:
                    isConnected = true;
                    mPrinter = myOpertion.getPrinter();
                    mTitle.setText(R.string.title_connected_to);
                    mTitle.setTextColor(Color.GREEN);
                    Toast.makeText(mContext, R.string.title_connected_to, Toast.LENGTH_SHORT).show();
                    break;
                case Connect.FAILED:
                    isConnected = false;
                    mTitle.setText(R.string.title_not_connected);
                    mTitle.setTextColor(Color.RED);
                    Toast.makeText(mContext, R.string.could_not_connect, Toast.LENGTH_SHORT).show();
                    break;
                case Connect.CLOSED:
                    isConnected = false;
                    mTitle.setText(R.string.title_not_connected);
                    mTitle.setTextColor(Color.RED);
                    Toast.makeText(mContext, R.string.closed_connection, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

            updateButtonState();

            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    };



    void sendData() throws IOException {
        try {

          /*  if(BluetoothPrintDriver.IsNoConnection()){
                Toast.makeText(getApplicationContext(), R.string.title_not_connected, Toast.LENGTH_SHORT).show();
                return;
            }*/


            mPrinter.init();

            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.comgakk2);
            mPrinter.printImage(bitmap);

            DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
            Date today = new Date();
            String s = dateFormatter.format(today);

            DateFormat dateFormatter1 = new SimpleDateFormat("HH:mm:ss");
            Date today1 = new Date();
            String s1 = dateFormatter1.format(today1);


            CanvasPrint cp = new CanvasPrint();
            cp.init(PrinterType.T9);
            cp.setUseSplit(true);

            FontProperty fp = new FontProperty();
            fp.setFont(true, false, false, false, 40, null);
            cp.setFontProperty(fp);
            cp.drawText("           CƠM GÀ KHÁNH KỲ      ");

            fp.setFont(false, false, false, false, 30, null);
            cp.setFontProperty(fp);
            cp.drawText("          61 Trần Quang Diệu, Phan Rang       " +
                        "            ĐT:0259.3838886     0933.638686   " +
                        "                 Web: comgakhanhky.com.vn             "       +
                        "                            Phiếu Tính Tiền               ");
            fp.setFont(false, false, false, false, 25, null);
            cp.setFontProperty(fp);
            cp.drawText("Ngày: "+ s + "    Giờ: " + s1 + "                              Số Bàn: " + Integer.toString(myOrder.getTableNumber()));
            fp.setFont(true, false, false, false, 25, null);
            cp.setFontProperty(fp);
            //String label = String.format("%-5s%-22s%5s%4s%10s", "STT","Tên Hàng", "ĐG", "SL", "T.Tiền\n");
           // String label = String.format("%-5s%-28s%5s%4s%10s", "STT","Tên Hàng", "ĐG", "SL", "T.Tiền\n");
           // cp.drawText(label);
           // cp.drawText("Số Bàn: " + Integer.toString(myOrder.getTableNumber()));
            mPrinter.printImage(cp.getCanvasImage());
            //mPrinter.printImage(cp2.getCanvasImage());
            //mPrinter.printImage(cp3.getCanvasImage());

            ArrayList<Item> localOrder = myOrder.getItemList();
            String label = String.format("%-5s%-22s%5s%4s%10s", "STT","Ten Hang", "DG", "SL", "T.Tien\n");
            mPrinter.setPrinter(Command.ALIGN,Command.ALIGN_LEFT);
            mPrinter.setPrintModel(true,false,false,false);
            mPrinter.printText(label);
            mPrinter.printText(String.format("----------------------------------------------\n"));
            mPrinter.setPrintModel(false,false,false,false);

            int i =0;
            String orderDetails;
            for(Item temp: localOrder) {
                orderDetails = String.format("%-5d%-22s%5.1f%4d%9.1f\n",i+1, localOrder.get(i).getCleanName(),
                        localOrder.get(i).getPrice(),localOrder.get(i).getAmount(),localOrder.get(i).getSubTotal());

                mPrinter.printText(orderDetails);
                i++;
            }


            //Fix this later
            myOrder.CalculateTotal();
            String orderTotal="";// = "Total: " + Double.toString(myOrder.getGrandTotal()) + "\n";

            orderTotal += ("----------------------------------------------\n");
            if(myOrder.getIsTax() == 1) {
                orderTotal += (String.format("%27s %17.1f\n","Cong:",myOrder.getGrandTotal_beforeTax()));
                orderTotal += (String.format("%27s %17.1f\n","Thue:", myOrder.getTax()));
            }

            orderTotal +=(String.format("%27s %17.1f\n","Tong Cong:" ,myOrder.getGrandTotal()));
            orderTotal += ("----------------------------------------------\n");
            mPrinter.setPrintModel(true,false,false,false);
            mPrinter.printText(orderTotal);

            mPrinter.setPrinter(Command.ALIGN,Command.ALIGN_CENTER);
            mPrinter.setPrintModel(true,false,false,false);
            mPrinter.printText("Cam on va hen gap lai quy khach\n");
            Barcode barcode;
            barcode = new Barcode(BarcodeType.QRCODE, 2, 2, 6, "http://comgakhanhky.com.vn/");
            mPrinter.printBarCode(barcode);
            //mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 1);
            mPrinter.cutPaper();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }






// Set up the table in the main screen

    public void init() {
        List<Item> items = myDb.getAllItems();
        TableLayout stk = (TableLayout)findViewById(R.id.table_main);
        TableRow tbrow0 = new TableRow(this);
        //TableRow.LayoutParams tlp = new TableRow.LayoutParams(20,30);


        TextView tv0 = new TextView(this);
        tv0.setText(R.string.num_table_header);
        tv0.setMinWidth(25);
        tv0.setTextColor(Color.WHITE);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setMinWidth(500);
        tv1.setText(R.string.name_table_header);
        tv1.setWidth(25);
        tv1.setGravity(Gravity.CENTER_HORIZONTAL);
        tv1.setTextColor(Color.WHITE);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setGravity(Gravity.CENTER_HORIZONTAL);
        tv2.setText(R.string.unit_price_table_header);
        tv2.setTextColor(Color.WHITE);
        tv2.setMinWidth(200);
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(this);
        tv3.setText(R.string.amount_table_header);
        tv3.setTextColor(Color.WHITE);
       // tv3.setWidth(10);
        tbrow0.addView(tv3);
        stk.addView(tbrow0);

        int i =0;
        for (Item cn : items) {
            TableRow tbrow = new TableRow(this);
            TextView t1v = new TextView(this);
            t1v.setText("" + i);
            t1v.setTextSize(20);
            t1v.setTextColor(Color.WHITE);
            t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);
            TextView t2v = new TextView(this);
            t2v.setText(cn.getName());
            t2v.setTextSize(23);
            t2v.setTextColor(Color.WHITE);
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);
            TextView t3v = new TextView(this);
            t3v.setText(String.valueOf(cn.getPrice()));
            t3v.setTextSize(20);
            t3v.setTextColor(Color.WHITE);
            t3v.setGravity(Gravity.CENTER);
            tbrow.addView(t3v);
            EditText t4v = new EditText(this);
            t4v.setInputType(InputType.TYPE_CLASS_NUMBER);
            amountED.add(t4v);
            t4v.getBackground().mutate().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);
            t4v.setTextColor(Color.WHITE);
            t4v.setTextSize(20);
            t4v.setGravity(Gravity.CENTER);
            tbrow.addView(t4v);
            stk.addView(tbrow);
            i++;
        }

        dialog = new ProgressDialog(mContext);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Connecting...");
        dialog.setMessage("Please Wait...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
    }
}