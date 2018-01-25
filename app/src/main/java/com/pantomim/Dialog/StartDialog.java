package com.pantomim.Dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pantomim.Interface.StartInterface;
import com.pantomim.R;

/**
 * Created by aryahm on 1/23/18.
 */

public class StartDialog extends DialogFragment {
    ViewGroup rootView;
    StartInterface set;
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.alert_start, container, false);
        setCancelable(false);
        run();
        return rootView;
    }

    public void init(final StartInterface set){
        this.set = set;
    }

    public void run(){
        Button button = (Button)rootView.findViewById(R.id.start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set.start();
                dismiss();

            }
        });
    }

}