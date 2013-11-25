package com.oscargomez.bluetoothcar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        Log.d("bluetoothcar", "Bluetooth desde MainActivity: " + MainActivity.bluetooth.getName());
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
    }

    @Override
    public void onClickBtnBack(View view) {
        Log.d("bluetoothcar", "Back/from Activity");
    }

    @Override
    public void onClickBtnLeft(View view) {
        Log.d("bluetoothcar", "Left/from Activity");
    }

    @Override
    public void onClickBtnRight(View view) {
        Log.d("bluetoothcar", "Right/from Activity");
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        ControllerListener listener = (ControllerListener) getActivity();

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_controller, container, false);
            return rootView;
        }

        public void onClickBtnFwd(View view) {
            Log.d("bluetoothcar", "Forward");
            listener.onClickBtnFwd(view);
        }

        public void onClickBtnBack(View view) {
            Log.d("bluetoothcar", "Back");
            listener.onClickBtnBack(view);
        }

        public void onClickBtnLeft(View view) {
            Log.d("bluetoothcar", "Left");
            listener.onClickBtnLeft(view);
        }

        public void onClickBtnRight(View view) {
            Log.d("bluetoothcar", "Right");
            listener.onClickBtnRight(view);
        }
    }

}
