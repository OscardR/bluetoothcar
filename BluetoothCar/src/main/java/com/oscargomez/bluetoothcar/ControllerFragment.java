package com.oscargomez.bluetoothcar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by oscar on 26/11/13.
 */
public class ControllerFragment extends Fragment {

        ControllerListener listener = (ControllerListener) getActivity();

        public ControllerFragment() {
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
