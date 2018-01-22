package com.pantomim;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * Created by aryahm on 1/23/18.
 */

public class DialogChoose extends DialogFragment {
    ViewGroup rootView;
    ChooseInterface set;
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.alert_choose, container, false);
        setCancelable(false);
        run();
        return rootView;
    }

    public void init(final ChooseInterface set){
        this.set=set;
    }

    public void run(){
        Button button = (Button)rootView.findViewById(R.id.choose);
        final EditText text = (EditText) rootView.findViewById(R.id.text);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set.choose(text.getText().toString());
                dismiss();

            }
        });
    }

}
