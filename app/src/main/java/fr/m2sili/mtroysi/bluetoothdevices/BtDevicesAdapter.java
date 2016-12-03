package fr.m2sili.mtroysi.bluetoothdevices;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by android on 11/30/16.
 */

public class BtDevicesAdapter extends ArrayAdapter<BtDevice> {

    private final Context context;

    public BtDevicesAdapter(Context context) {
        super(context, R.layout.ligne);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AssetManager asset = context.getAssets();

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.ligne, parent, false);

        TextView nom = (TextView)v.findViewById(R.id.nom);
        nom.setText(getItem(position).getNom());

        TextView adresse = (TextView)v.findViewById(R.id.adresse);
        adresse.setText(getItem(position).getAdresse());

        if(getItem(position).getState() == BluetoothDevice.BOND_NONE) {
            nom.setTextColor(Color.parseColor("#9E9E9E"));
            adresse.setTextColor(Color.parseColor("#9E9E9E"));
        }

        ImageView icon = (ImageView)v.findViewById(R.id.icon);
        String imgPath = "";
        switch (this.getItem(position).getClasse()) {
            case BluetoothClass.Device.Major.AUDIO_VIDEO:
                imgPath = "ic_headset_black_48dp.png";
                break;
            case BluetoothClass.Device.Major.PHONE:
                imgPath = "ic_phone_android_black_48dp.png";
                break;
            case BluetoothClass.Device.Major.COMPUTER:
                imgPath = "ic_computer_black_48dp.png";
                break;
        }
        if(!imgPath.isEmpty()) {
            try {
                InputStream ims = asset.open(imgPath);
                Drawable d = Drawable.createFromStream(ims, null);
                icon.setImageDrawable(d);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return v;
    }

    public boolean contains(String adress) {
        int i;
        boolean found = false;
        for(i = 0; i < this.getCount(); ++i) {
            if(this.getItem(i).getAdresse().equals(adress)) {
                found = true;
                break;
            }
        }
        return found;
    }
}
