package com.biorics.lois.bluetooth;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.logging.Handler;

import static android.content.ContentValues.TAG;

/**
 * Created by lois on 24/10/16.
 */

class ListDevicesView extends ArrayAdapter<Bluetooth_Device> {
    private ArrayList<Bluetooth_Device> listDevices;
    private Activity activity;

    private int layoutid;


    public ListDevicesView(Activity activity, int layoutid, ArrayList<Bluetooth_Device> objects) {
        super(activity.getApplicationContext(),layoutid , objects);
        this.activity = activity;
        this.listDevices = objects;
        this.layoutid = layoutid;
    }

    public int getLayoutid() {
        return layoutid;
    }

    public void setLayoutid(int layoutid) {
        this.layoutid = layoutid;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null ){
            LayoutInflater inflater =
                    (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layoutid, viewGroup, false);
        }
        Bluetooth_Device temp_device = listDevices.get(i);
        String device_name = temp_device.getName();
        String device_address = temp_device.getAddress();
        TextView text_name = (TextView) view.findViewById(R.id.device_name);
        if (device_name != null && device_name.length() > 0) {
            text_name.setText(device_name+temp_device.getRSSI());
        } else {
            text_name.setText("None"+temp_device.getRSSI());
        }

        TextView text_add = (TextView) view.findViewById(R.id.device_address);
        if (device_address != null && device_address.length() > 0) {
            text_add.setText(device_address);
        }
        else {
            text_add.setText("None");
        }
        return view;
    }
}

