package com.biorics.lois.bluetooth;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.ScrollView;

/**
 * Created by lois on 23/03/17.
 */

public class Controller{
    private Context activity;
    private BleManager device_scan;
    private DeviceManager deviceManager;

    public Controller(Context context) {
        this.activity = context;
    }
    public void defaultInitial() {
        ((Activity)activity).setContentView(R.layout.activity_bluetooth);
        deviceManager = new DeviceManager();
        device_scan = new BleManager(activity,deviceManager,R.layout.list_items);
    }

    public void scanController() {
        if (!device_scan.isScanning()) {
            deviceManager.clear();
            device_scan.startScan();
        } else {
            device_scan.stopScan();
        }
    }
}
