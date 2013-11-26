package com.oscargomez.bluetoothcar;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Interfaz de callbacks para el ControllerFragment
 */
interface ControllerListener {
    public void onClickBtnFwd(View view);

    public void onClickBtnBack(View view);

    public void onClickBtnLeft(View view);

    public void onClickBtnRight(View view);
}

public class ControllerActivity extends ActionBarActivity implements ControllerListener {

    private BluetoothDevice device;
    private BluetoothSocket socket = null;
    private String str;
    private OutputStream outputStream;
    private InputStream inputStream;
    private StringBuffer sbu;
    private Handler _handler = new Handler();

    private final byte[] FORWARD = {'1'};
    private final byte[] BACKWARD = {'0'};
    private final byte[] LEFT = {'2'};
    private final byte[] RIGHT = {'3'};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        // Show the Up button in the action bar.
        try {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.d("bluetoothcar", "No hay ActionBar");
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ControllerFragment())
                    .commit();
        }

        Log.d("bluetoothcar", "Bluetooth desde MainActivity: " + MainActivity.bluetooth.getName());

        device = getIntent().getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        Log.d("bluetoothcar", "recibido parcelable BluetoothDevice!");
        connect(device);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.controller, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickBtnFwd(View view) {
        Log.d("bluetoothcar", "Forward/from Activity");
        // Mandar un 0 por el serial del bluetooth
        try {
            outputStream.write(FORWARD);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClickBtnBack(View view) {
        Log.d("bluetoothcar", "Back/from Activity");
        // Mandar un 1 por el serial del bluetooth
        try {
            outputStream.write(BACKWARD);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClickBtnLeft(View view) {
        Log.d("bluetoothcar", "Left/from Activity");
        // Mandar un 2 por el serial del bluetooth
        try {
            outputStream.write(LEFT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClickBtnRight(View view) {
        Log.d("bluetoothcar", "Right/from Activity");
        // Mandar un 3 por el serial del bluetooth
        try {
            outputStream.write(RIGHT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void connect(BluetoothDevice device) {
        //BluetoothSocket socket = null;
        try {
            //Create a Socket connection: need the server's UUID number of registered
            socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));

            socket.connect();
            Log.d("bluetoothcar", "conectado!");

            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

        } catch (IOException e) {
            Log.d("bluetoothcar", "Error al conectar con el coche");
        }
    }
}
