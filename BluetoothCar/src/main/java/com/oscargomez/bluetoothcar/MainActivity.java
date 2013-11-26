package com.oscargomez.bluetoothcar;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    protected static BluetoothAdapter bluetooth;
    protected static ArrayList<BluetoothDevice> deviceList = new ArrayList<BluetoothDevice>();
    private boolean bluetoothActive;

    private TextView txtStatus;
    private Button btnExplore;
    private ListView listDevices;
    private DeviceListAdapter adapter;

    private String btAddress;
    private String btName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtStatus = (TextView) findViewById(R.id.txtStatus);
        btnExplore = (Button) findViewById(R.id.btnExplore);
        listDevices = (ListView) findViewById(R.id.listDevices);

        adapter = new DeviceListAdapter(this, deviceList);
        listDevices.setAdapter(adapter);
        listDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = getApplicationContext();
                CharSequence text = "Has hecho click en " + position;
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                Intent i = new Intent(getApplicationContext(), ControllerActivity.class);
                i.putExtra(BluetoothDevice.EXTRA_DEVICE, (BluetoothDevice) deviceList.get(position));
                Log.d("bluetoothcar", "A punto de empezar actividad ControllerActivity con bluetoothdevice: " + deviceList.get(position));
                startActivity(i);
            }
        });

        // Setear la action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(false);

        // Inicializar el Bluetooth
        bluetoothActive = false;
        bluetooth = BluetoothAdapter.getDefaultAdapter();

        if (bluetooth != null) {
            if (bluetooth.isEnabled()) {
                bluetoothActive = true;
                setBluetoothData();
                startDiscovery();
                btnExplore.setText("Desconectar");
            } else {
                Log.d("BluetoothCar", "Bluetooth desactivado... esperando acción del usuario");
            }
        } else {
            btnExplore.setEnabled(false);
        }
    }

    /**
     * Función helper para recoger datos relativos al dispositivo bluetooth encontrado
     */
    private void setBluetoothData() {
        btAddress = bluetooth.getAddress();
        btName = bluetooth.getName();
        Log.d("BluetoothCar", "Dirección Bluetooth: " + btAddress);
        Log.d("BluetoothCar", "Nombre Bluetooth: " + btName);
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
                btnConnectClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void btnExploreClick(View view) {
        startDiscovery();
        btnExplore.setText("Buscando Dispositivos...");
        btnExplore.setEnabled(false);
    }

    public void btnConnectClick() {
        if (bluetooth != null) {
            if (!bluetooth.isEnabled()) {
                startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 1);
                txtStatus.setText("Activando Bluetooth...");
            } else {
                txtStatus.setText("Desactivando Bluetooth...");
                if (bluetooth.isDiscovering()) bluetooth.cancelDiscovery();
                bluetooth.disable();
                bluetoothActive = false;
                txtStatus.setText("Bluetooth Desactivado");
                btnExplore.setEnabled(false);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) //Bluetooth permission request window
            if (resultCode == RESULT_OK) {
                txtStatus.setText("Bluetooth Activado");
                setBluetoothData();
                bluetoothActive = true;
                btnExplore.setEnabled(true);
                startDiscovery();
            } else {
                txtStatus.setText("Bluetooth NO Activado");
                btnExplore.setEnabled(false);
            }
    }

    private void startDiscovery(){

        if (bluetoothActive){
            deviceList.clear();
            registerReceiver(discoveryResult, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            registerReceiver(discoveryFinish, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
            //Device's discovery
            txtStatus.setText("Descubriendo dispositivos Bluetooth...");
            Log.d("BluetoothCar", "startDiscovery");
            bluetooth.startDiscovery();
        } else {
            txtStatus.setText("Activa primero el Bluetooth");
        }
    }

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
            listDevices.setAdapter(adapter);

            Log.d("BluetoothCar", "Descubierto " + remoteDeviceName);
            Log.d("BluetoothCar", "RSSI: "+ rssi + "dBm");
            Log.d("BluetoothCar", "MAC: "+ mac);
        }
    };

    BroadcastReceiver discoveryFinish = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            btnExplore.setText("Buscar de nuevo");
            btnExplore.setEnabled(true);
            txtStatus.setText("Búsqueda Finalizada");
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("BluetoothCar", "Saliendo...");
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
