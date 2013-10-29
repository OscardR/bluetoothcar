package com.oscargomez.bluetoothcar;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    private BluetoothAdapter bluetooth;
    private ArrayList<BluetoothDevice> deviceList = new ArrayList<BluetoothDevice>();
    private boolean bluetoothActive;

    private TextView txtStatus;
    private Button btnConnect;

    private String btAddress;
    private String btName;

    BroadcastReceiver discoveryResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String remoteDeviceName =
                    intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
            BluetoothDevice remoteDevice =
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            int rssi =
                    intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
            String mac = remoteDevice.getAddress();
            deviceList.add(remoteDevice);
            Log.d("HelloWorld", "Descubierto " + remoteDeviceName);
            Log.d("HelloWorld", "RSSI: "+ rssi + "dBm");
            Log.d("HelloWorld", "MAC: "+ mac);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtStatus = (TextView) findViewById(R.id.txtStatus);
        btnConnect = (Button) findViewById(R.id.btnConnect);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);

        bluetoothActive = false;
        bluetooth = BluetoothAdapter.getDefaultAdapter();

        if (bluetooth != null) {
            if (bluetooth.isEnabled()) {
                bluetoothActive = true;
                setBluetoothData();
                startDiscovery();
            } else {
                Log.d("HelloWorld", "Bluetooth desactivado... esperando acción del usuario");
            }
        } else {
            btnConnect.setEnabled(false);
        }
    }

    private void setBluetoothData() {
        btAddress = bluetooth.getAddress();
        btName = bluetooth.getName();
        Log.d("HelloWorld", "Dirección Bluetooth: " + btAddress);
        Log.d("HelloWorld", "Nombre Bluetooth: " + btName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_bluetooth:
                btnConnectClick(btnConnect.getRootView());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void btnConnectClick(View view) {
        if (bluetooth != null) {
            if (!bluetooth.isEnabled()) {
                startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 1);
                txtStatus.setText("Conectando Bluetooth...");
            } else {
                txtStatus.setText("Desactivando Bluetooth...");
                bluetooth.disable();
                bluetoothActive = false;
                txtStatus.setText("Bluetooth Desactivado");
                btnConnect.setText("Conectar Bluetooth");
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) //Bluetooth permission request window
            if (resultCode == RESULT_OK) {
                txtStatus.setText("Bluetooth " + requestCode + " Activado");
                setBluetoothData();
                bluetoothActive = true;
                btnConnect.setText("Desconectar Bluetooth");
                startDiscovery();
            } else {
                txtStatus.setText("Bluetooth " + requestCode + " NO Activado");
                btnConnect.setText("Conectar Bluetooth");
            }
    }

    private void startDiscovery(){

        if (bluetoothActive){
            deviceList.clear();
            registerReceiver(discoveryResult, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            //Device's discovery
            txtStatus.setText("Descubrimiento de dispositivos Bluetooth");
            Log.d("HelloWorld", "startDiscovery");
            bluetooth.startDiscovery();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("HelloWorld", "Saliendo...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(discoveryResult, new IntentFilter(BluetoothDevice.ACTION_FOUND));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(discoveryResult);
    }
}
