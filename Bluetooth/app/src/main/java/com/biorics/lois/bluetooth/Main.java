package com.biorics.lois.bluetooth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by wy490 on 2016/12/5.
 */

public class Main extends AppCompatActivity {

    private Controller controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new Controller(this);
        controller.defaultInitial();
        controller.scanController();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}













