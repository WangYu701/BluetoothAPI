package com.biorics.lois.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.os.ParcelUuid;

/**
 * Created by lois on 24/10/16.
 */

public class Bluetooth_Device {

    private BluetoothDevice device;
    private int rssi;

    public Bluetooth_Device(BluetoothDevice bluetoothdevice) {
        device = bluetoothdevice;
    }

    public String getName()
    {
        return device.getName();
    }

    public ParcelUuid[] getUuid()
    {
        return device.getUuids();
    }

    public void setRSSI(int newrssi)
    {
        rssi = newrssi;
    }
    public int getRSSI()
    {
        return rssi;
    }

    public String getAddress()
    {
        return device.getAddress();
    }
}
