package com.pantomim.Dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.JsonObject;
import com.pantomim.DataManager;
import com.pantomim.Interface.RoomInterface;
import com.pantomim.Model.Room;
import com.pantomim.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pantomim.ServerManager.getInterface;

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
        final TextView name = (TextView) rootView.findViewById(R.id.name);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( name.length()!=0) {

                    createRequest(name.getText().toString(),
                            DataManager.getUsername(getActivity()));
                }

            }
        });
    }

    public void createRequest(final String name, final String ownerName){
        Call<JsonObject> call = getInterface().addGame(setSignUp(name));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code()==200) {
                    String array = response.body().get("id").getAsString();
                    set.onCreate(new Room(name,ownerName,3,1,array));
                    dismiss();
                }
                else
                    Toast.makeText(getActivity(),"Error",Toast.LENGTH_LONG).show();


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(),"Error",Toast.LENGTH_LONG).show();

            }
        });

    }

    public JsonObject setSignUp(String name){
        JsonObject content = new JsonObject();
        try {
            content.addProperty("token", DataManager.getToken(getActivity()));
            content.addProperty("name", name);
        }
        catch (Exception e){

        }
        return content;

    }
}