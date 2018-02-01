package com.pantomim.Dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pantomim.R;

/**
 * Created by aryahm on 1/23/18.
 */

public class StartDialog extends DialogFragment {
    ViewGroup rootView;
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.alert_start, container, false);
        //setCancelable(false);
        return rootView;
    }


    public void start(Context context){
        Toast.makeText(context,"GAME STARTED!",Toast.LENGTH_LONG).show();
        dismiss();

    }

}