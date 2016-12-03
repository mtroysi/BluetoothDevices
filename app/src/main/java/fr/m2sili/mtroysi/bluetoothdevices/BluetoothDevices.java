package fr.m2sili.mtroysi.bluetoothdevices;

import android.app.Activity;
import android.os.Bundle;

public class BluetoothDevices extends Activity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        MainFragment fragmentBluetooth = new MainFragment();
        getFragmentManager().beginTransaction().add(R.id.main, fragmentBluetooth).commit();
    }
}
