package com.biorics.lois.bluetooth;

import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.ScrollView;

/**
 * Created by lois on 23/03/17.
 */

public class Controller{
    private AppCompatActivity activity;
    private BleManager device_scan;
    private DeviceManager deviceManager;
    private ListDevicesView defaule_listview;

    public Controller(AppCompatActivity activity) {
        this.activity = activity;
    }
    public void defaultInitial(int layout) {
        activity.setContentView(R.layout.activity_bluetooth);
        deviceManager = new DeviceManager();
        defaule_listview = new ListDevicesView(activity, layout, deviceManager.getDevices());
        ListView listView = new ListView(activity);
        listView.setAdapter(defaule_listview);
        ((ScrollView) activity.findViewById(R.id.scrollview)).addView(listView);
        device_scan = new BleManager(activity,deviceManager, defaule_listview);
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
