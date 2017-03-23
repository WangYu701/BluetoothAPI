package com.biorics.lois.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.content.ContentValues.TAG;

/**
 * Created by trouble on 3/5/17.
 */

public class BleManager {
    private Handler handler;
    private AppCompatActivity activity;
    private boolean isScanning;
    private DeviceManager manager;
    private ScanCallback mLeScanCallbackA;
    private BluetoothAdapter bluetoothAdapter;
    private static final long SCAN_PERIOD = 10000;
    private BluetoothAdapter.LeScanCallback mLeScanCallbackB;
    private ListDevicesView defaule_listview;

    public static final int REQUEST_ENABLE_BT = 1;

    //Constructor
    public BleManager(Context context, DeviceManager deviceManager,int layout) {
        this.manager = deviceManager;
        this.handler = new Handler();
        this.activity = (AppCompatActivity) context;

        setView(deviceManager,layout);
        initial();
    }

    private void setView(DeviceManager deviceManager, int layout) {
        if(layout!= 0)
        {
            defaule_listview = new ListDevicesView(activity, layout, deviceManager.getDevices());
            ListView listView = new ListView(activity);
            listView.setAdapter(defaule_listview);
            ((ScrollView)  activity.findViewById(R.id.scrollview)).addView(listView);
        }else{
            defaule_listview = null;
        }
    }

    private void initial() {
        // Check support available
        if (!activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(activity, "BLE Not Supported", Toast.LENGTH_SHORT).show();
            activity.finish();
        }

        // SetUp bluetoothManager
        BluetoothManager bluetoothManager =
                (BluetoothManager) this.activity.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
    }

    //Separate Android version (Before and after API 21)
    private void separateStartScan(){

        createCallback();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bluetoothAdapter.getBluetoothLeScanner().startScan(mLeScanCallbackA);
        } else {
            bluetoothAdapter.startLeScan(mLeScanCallbackB);
        }
    }

    public void separateStopScan(){
        createCallback();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bluetoothAdapter.getBluetoothLeScanner().stopScan(mLeScanCallbackA);
        } else {
            bluetoothAdapter.stopLeScan(mLeScanCallbackB);
        }
    }

    //Scan device
    public void scanLeDevice(boolean enable) {

        if (enable && !isScanning) {

            handler.post(new Runnable() {
                @Override
                public void run() {
                    isScanning = false;
                    separateStopScan();
                }
            });
            isScanning = true;
            separateStartScan();
        } else {
            isScanning = false;
            separateStopScan();
        }
    }

    public boolean isScanning() {
        return isScanning;
    }

    // Device scan callback.
    private void createCallback(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
         mLeScanCallbackA =
                new ScanCallback() {
                    @Override
                    public void onScanResult(int callbackType, ScanResult result) {
                        super.onScanResult(callbackType, result);
                    }
                };
        }
        else {
            mLeScanCallbackB =
                new BluetoothAdapter.LeScanCallback() {
                    @Override
                    public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                        final int new_rssi = rssi;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                manager.addNewDevice(device, new_rssi);
                                print();
                                if(defaule_listview !=  null)
                                    defaule_listview.notifyDataSetChanged();
                            }
                        });

                    }
                };
        }


    }

    public void startScan() {
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            scanLeDevice(false);
        }
        else {
            scanLeDevice(true);
        }
    }

    public void print()
    {
        ArrayList<Bluetooth_Device> deviceslist = manager.getDevices();
        for(Bluetooth_Device device : deviceslist) {
            System.out.println("************* " + device.getName());
        }

    }

    public void stopScan() {
        scanLeDevice(false);
    }


}
