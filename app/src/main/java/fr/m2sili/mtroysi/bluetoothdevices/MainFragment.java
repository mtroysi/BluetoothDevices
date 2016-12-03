package fr.m2sili.mtroysi.bluetoothdevices;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static android.content.ContentValues.TAG;

/**
 * Created by android on 11/30/16.
 */

public class MainFragment extends ListFragment {

    private BtDevicesAdapter adapter;
    private List devices = new ArrayList();
    private BroadcastReceiver mBtBroadCastReceiver;
    private static int count = 0;

    private BluetoothDevices mainActivityListener = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivityListener = (BluetoothDevices) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            devices = savedInstanceState.getParcelableArrayList("knownDevices");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Register the BroadcastReceiver
        mBtBroadCastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                NotificationManager notificationManager = (NotificationManager) mainActivityListener.getSystemService(Activity.NOTIFICATION_SERVICE);
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (!adapter.contains(device.getAddress())) {
                        devices.add(device);
                        BtDevice btDevice = adapter.convertAndAdd(device);
                        adapter.notifyDataSetChanged();
                        Notification notification =
                                new Notification.Builder(mainActivityListener)
                                        .setContentTitle("New Bluetooth device")
                                        .setContentText(btDevice.getNom())
                                                .setSmallIcon(R.drawable.ic_bluetooth)
                                                .setAutoCancel(true)
                                                .build();
                        notificationManager.notify(count, notification);
                        ++count;
                    }
                } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                    getBondedDevices();
                    scan();
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        mainActivityListener.registerReceiver(mBtBroadCastReceiver, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_layout, container, false);
        setHasOptionsMenu(true);

        adapter = new BtDevicesAdapter(inflater.getContext());
        setListAdapter(adapter);
        getBondedDevices();
        if(!devices.isEmpty()) {
            for (BluetoothDevice device : (List<BluetoothDevice>)devices) {
                adapter.convertAndAdd(device);
            }
        }
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.options_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                scan();
                break;
        }
        return true;
    }

    void getBondedDevices() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            // If there are paired devices
            if (pairedDevices.size() > 0) {
                // Loop through paired devices
                for (BluetoothDevice device : pairedDevices) {
                    if (!adapter.contains(device.getAddress())) {
//                        devices.add(device);
                        adapter.convertAndAdd(device);
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    void scan() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.startDiscovery();
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unregister broadcast receiver
        mainActivityListener.unregisterReceiver(mBtBroadCastReceiver);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("knownDevices", (ArrayList<BluetoothDevice>)devices);
    }
}
