package com.pantomim;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by aryahm on 1/26/18.
 */

public class SignUpDialog extends DialogFragment {
    ViewGroup rootView;
    SignupInterface  set;
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.alert_signup, container, false);
        run();
        return rootView;
    }

    public void init(final SignupInterface set){
        this.set = set;
    }

    public void run(){
        Button button = (Button)rootView.findViewById(R.id.signup);
        final EditText email = (EditText) rootView.findViewById(R.id.email);
        final EditText password = (EditText) rootView.findViewById(R.id.password);
        final EditText username = (EditText) rootView.findViewById(R.id.username);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set.onSignUp(username.getText().toString(),
                        password.getText().toString(),email.getText().toString());
                dismiss();

            }
        });
    }

}