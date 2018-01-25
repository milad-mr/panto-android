package com.pantomim.Dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.pantomim.Interface.LoginInterface;
import com.pantomim.R;

/**
 * Created by aryahm on 1/26/18.
 */

public class LoginDialog extends DialogFragment {
    ViewGroup rootView;
    LoginInterface set;
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.alert_login, container, false);
        run();
        return rootView;
    }

    public void init(final LoginInterface set){
        this.set = set;
    }

    public void run(){
        Button button = (Button)rootView.findViewById(R.id.login);
        final EditText password = (EditText) rootView.findViewById(R.id.password);
        final EditText username = (EditText) rootView.findViewById(R.id.username);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.length()!=0&&username.length()!=0) {
                    set.onLogin(username.getText().toString(),
                            password.getText().toString());
                    dismiss();
                }

            }
        });
    }

}