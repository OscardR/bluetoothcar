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

    /**
     * Los handlers de los botones se idearon en un principio para comunicar con la actividad que
     * contiene este fragment, pero al final no han sido necesarios, ya que es la actividad
     * la que recoge los eventos de click en los botones
     *
     * @param view
     */
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
