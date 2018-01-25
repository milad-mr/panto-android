package com.pantomim.Activity;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pantomim.DataManager;
import com.pantomim.Dialog.LoginDialog;
import com.pantomim.Interface.LoginInterface;
import com.pantomim.R;
import com.pantomim.Dialog.SignUpDialog;
import com.pantomim.Interface.SignupInterface;

public class LandingActivity extends AppCompatActivity implements SignupInterface,LoginInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        Button login = (Button) findViewById(R.id.login);
        Button signup = (Button) findViewById(R.id.signup);
        final SignupInterface signUp = this;
        final LoginInterface log = this;
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginDialog dFragment = new LoginDialog  ();
                dFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                dFragment.show(getSupportFragmentManager(), null);
                dFragment.init(log);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpDialog dFragment = new SignUpDialog ();
                dFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                dFragment.show(getSupportFragmentManager(), null);
                dFragment.init(signUp);
            }
        });
    }

    @Override
    public void onLogin(String username, String password){
        startActivity(new Intent(this, RoomActivity.class));
        setDataManager(username,0);
        finish();
        //TODO:milad add on login function
    }
    @Override
    public void onSignUp(String username, String password, String email){
        startActivity(new Intent(this, RoomActivity.class));
        setDataManager(username,0);
        finish();
        //TODO:milad add on signup function
    }

    public void setDataManager(String username , int id){
        DataManager.setId(this, id);
        DataManager.setScore(this, 0);
        DataManager.setUsername(this, username);
    }


}
