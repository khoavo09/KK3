package com.example.khoavo.kk3;

/**
 * Created by khoavo on 1/17/18.
 */

import com.android.print.sdk.PrinterInstance;

import android.content.Intent;

public interface PrinterOperation {
    public void open(Intent data);
    public void close();
    public void chooseDevice();
    public PrinterInstance getPrinter();
}
