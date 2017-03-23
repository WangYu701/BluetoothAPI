package com.biorics.lois.bluetooth;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by wy490 on 2017/3/9.
 */

public class DeviceManager {



    private ArrayList<Bluetooth_Device> devices;


    public DeviceManager(){
        this.devices = new ArrayList<>();
    }

    public ArrayList<Bluetooth_Device> getDevices() {
        return devices;
    }

    public void addNewDevice(BluetoothDevice device, int rssi) {
        String address = device.getAddress();
        if (devices.size()==0) {
            addDeviceToList(device,rssi);
        }else{
            boolean isfound = false;
            for(int i = 0; i < devices.size();i++)
            {
                if(devices.get(i).getAddress().equals(address))
                {
                    devices.get(i).setRSSI(rssi);
                    isfound = true;
                    break;
                }
            }
            if(!isfound) addDeviceToList(device,rssi);
        }
        sortListByRSSI(devices);
    }

    void addDeviceToList(BluetoothDevice device,int rssi){
        Bluetooth_Device tempDevice = new Bluetooth_Device(device);
        tempDevice.setRSSI(rssi);
        devices.add(tempDevice);
    }

    void sortListByRSSI(ArrayList<Bluetooth_Device> devices)
    {

        Collections.sort(devices, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Bluetooth_Device p1 = (Bluetooth_Device) o1;
                Bluetooth_Device p2 = (Bluetooth_Device) o2;
                if(p1.getRSSI() > p2.getRSSI())
                    return 1;
                else if(p1.getRSSI() < p2.getRSSI())
                    return -1;
                else return 0;
            }
        });
    }

    void clear(){
        devices.clear();
    }
}
