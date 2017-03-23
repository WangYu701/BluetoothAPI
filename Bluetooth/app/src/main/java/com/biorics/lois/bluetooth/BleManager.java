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
import android.util.Log;
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
    private Activity activity;
    private boolean isScanning;
    private DeviceManager manager;
    private ScanCallback mLeScanCallbackA;
    private BluetoothAdapter bluetoothAdapter;
    private static final long SCAN_PERIOD = 10000;
    private BluetoothAdapter.LeScanCallback mLeScanCallbackB;
    private ListDevicesView listdevices;

    public static final int REQUEST_ENABLE_BT = 1;

    //Constructor
    public BleManager(Activity activity, DeviceManager deviceManager,ListDevicesView listdevices) {
        this.manager = deviceManager;
        this.handler = new Handler();
        this.activity = activity;
        this.listdevices = listdevices;

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
            Log.d(TAG,"test***");
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
                    Log.d(TAG,"Scan Le Device stop");
                    isScanning = false;
                    separateStopScan();


                }
            });
            isScanning = true;
            separateStartScan();
        } else {
            isScanning = false;
            Log.d(TAG,"Scan Le Device stop");
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
                                Log.d(TAG,"New device found");
                                listdevices.notifyDataSetChanged();
                            }
                        });
                    }
                };
        }


    }

    public void startScan() {
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Log.d(TAG,"scanLeDevice(false)");
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            scanLeDevice(false);
        }
        else {
            Log.d(TAG,"scanLeDevice(true)");
            scanLeDevice(true);

        }
    }

    public void stopScan() {
        scanLeDevice(false);
    }


}
