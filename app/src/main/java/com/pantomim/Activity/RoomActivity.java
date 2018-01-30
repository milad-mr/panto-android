package com.pantomim.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.melnykov.fab.FloatingActionButton;
import com.pantomim.DataManager;
import com.pantomim.Interface.JoinRequest;
import com.pantomim.Model.Room;
import com.pantomim.R;
import com.pantomim.Adapter.RoomAdapter;
import com.pantomim.Dialog.RoomDialog;
import com.pantomim.Interface.RoomInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pantomim.ServerManager.getInterface;

public class RoomActivity extends AppCompatActivity implements RoomInterface,JoinRequest {
    List<Room> rooms = new ArrayList<Room>();
    TextView score;
    RoomAdapter adapter;
    Activity activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        RecyclerView roomR = (RecyclerView) findViewById(R.id.rooms);
        FloatingActionButton action = (FloatingActionButton) findViewById(R.id.fab);
        score = (TextView) findViewById(R.id.score);

        action.attachToRecyclerView(roomR);
        adapter = new RoomAdapter(rooms, this,this);
        roomR.setAdapter(adapter);
        roomR.setLayoutManager(new LinearLayoutManager(this));
        final RoomInterface roomInterface = this;
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RoomDialog dFragment = new RoomDialog();
                dFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                dFragment.show(getSupportFragmentManager(), null);
                dFragment.init(roomInterface);
            }
        });
        getRequest();

    }

    public void addRoom(Room room){
        rooms.add(0,room);
        adapter.notifyDataSetChanged();

    }


    @Override
    public void onResume(){
        super.onResume();
        score.setText(String.valueOf(DataManager.getScore(this)));

    }

    @Override
    public void onCreate(Room room){
        addRoom(room);
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra("id",String.valueOf(room.getId()));
        intent.putExtra("owner_name",room.getOwnername());
        activity.startActivity(intent);

    }

    public void getRequest(){
        Call<JsonObject> call = getInterface().getGames();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code()==200) {
                    JsonArray array = response.body().get("games").getAsJsonArray();
                    for(int i = 0 ; i<array.size() ; i++){
                        JsonObject object = array.get(i).getAsJsonObject();
                        String host = object.get("host").getAsString();
                        int count = 1;
                        if(object.has("client1"))
                            count++;
                        if(object.has("client2"))
                            count++;
                        Room room = new Room(array.get(i).getAsJsonObject().get("game name").getAsString(),host,3,count,
                                array.get(i).getAsJsonObject().get("game id").getAsString());
                        addRoom(room);
                    }
                }
                else
                    Toast.makeText(activity,"Error",Toast.LENGTH_LONG);


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(activity,"Error",Toast.LENGTH_LONG);
            }
        });

    }
    public JsonObject addClient(String gameId){
        JsonObject content = new JsonObject();
        try {
            content.addProperty("game id", gameId);
            content.addProperty("client token", DataManager.getToken(this));
        }
        catch (Exception e){

        }
        return content;

    }

    @Override
    public void joinRequest(final String gameId){
        Call<JsonObject> call = getInterface().addClient(addClient(gameId));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code()==200) {
                    for(int i = 0 ; i < rooms.size() ; i++){
                        if(rooms.get(i).getId().equals(gameId)){
                            Intent intent = new Intent(activity, MainActivity.class);
                            intent.putExtra("id",String.valueOf(gameId));
                            intent.putExtra("owner_name",rooms.get(i).getOwnername());
                            activity.startActivity(intent);
                            break;
                        }
                    }

                }
                else
                    Toast.makeText(activity,"Error",Toast.LENGTH_LONG);


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(activity,"Error",Toast.LENGTH_LONG);

            }
        });

    }

}
