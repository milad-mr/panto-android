package com.pantomim.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.pantomim.DataManager;
import com.pantomim.Dialog.LoginDialog;
import com.pantomim.Interface.LoginInterface;
import com.pantomim.R;
import com.pantomim.Dialog.SignUpDialog;
import com.pantomim.Interface.SignupInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pantomim.ServerManager.getInterface;

public class LandingActivity extends AppCompatActivity implements SignupInterface,LoginInterface {
    private Activity activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!DataManager.getToken(this).equals("")){
            startActivity(new Intent(this,RoomActivity.class));
            finish();
            return;

        }
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
    public void onLogin(final String username, String password){
        request(username,password);
    }
    @Override
    public void onSignUp(final String username, String password, String email){
        request(username,password);
    }

    public void request(final String username, String password){
        Call<JsonObject> call = getInterface().register(setSignUp(username,password));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code()==200) {
                    startActivity(new Intent(activity, RoomActivity.class));
                    setDataManager(username,response.body().get("token").getAsString());
                    finish();
                }
                else
                    Toast.makeText(activity,"wrong password or username",Toast.LENGTH_LONG);


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(activity,"There is a problem",Toast.LENGTH_LONG);

                // handle execution failures like no internet connectivity
                //BusProvider.getInstance().post(new ErrorEvent(-2,t.getMessage()));
            }
        });

    }

    public void setDataManager(String username,String token){
        //DataManager.setId(this, id);
        DataManager.setToken(this,token);
        DataManager.setScore(this, 0);
        DataManager.setUsername(this, username);
    }

    public JsonObject setSignUp(String username, String password){
        JsonObject content = new JsonObject();
        try {
            content.addProperty("username", username);
            content.addProperty("password", password);
        }
        catch (Exception e){

        }
        return content;

    }



}
