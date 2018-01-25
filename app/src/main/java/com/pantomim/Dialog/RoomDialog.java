package com.pantomim.Dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pantomim.DataManager;
import com.pantomim.Interface.RoomInterface;
import com.pantomim.Model.Room;
import com.pantomim.R;

/**
 * Created by aryahm on 1/26/18.
 */

public class RoomDialog extends DialogFragment {
    ViewGroup rootView;
    RoomInterface set;
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.alert_room, container, false);
        run();
        return rootView;
    }

    public void init(final RoomInterface set){
        this.set = set;
    }

    public void run(){
        Button button = (Button)rootView.findViewById(R.id.create);
        final TextView currentMax = (TextView) rootView.findViewById(R.id.count);
        final TextView name = (TextView) rootView.findViewById(R.id.name);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentMax.length()!=0&&name.length()!=0) {
                    set.onCreate(new Room(name.getText().toString(),
                            DataManager.getUsername(getActivity()),
                            Integer.parseInt(currentMax.getText().toString()), 0, DataManager.getId(getActivity())));
                    dismiss();
                }

            }
        });
    }

}