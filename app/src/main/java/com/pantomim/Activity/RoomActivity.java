package com.pantomim.Activity;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.pantomim.DataManager;
import com.pantomim.Model.Room;
import com.pantomim.R;
import com.pantomim.Adapter.RoomAdapter;
import com.pantomim.Dialog.RoomDialog;
import com.pantomim.Interface.RoomInterface;

import java.util.ArrayList;
import java.util.List;

public class RoomActivity extends AppCompatActivity implements RoomInterface {
    List<Room> rooms = new ArrayList<Room>();
    TextView score;
    RoomAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        RecyclerView roomR = (RecyclerView) findViewById(R.id.rooms);
        FloatingActionButton action = (FloatingActionButton) findViewById(R.id.fab);
        score = (TextView) findViewById(R.id.score);

        action.attachToRecyclerView(roomR);
        adapter = new RoomAdapter(rooms, this);
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

        generateRoom();

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

    //for prototype
    public void generateRoom(){
        addRoom(new Room("harchi","aryahm",10,5,1));
        addRoom(new Room("harchi","name",10,5,1));
        addRoom(new Room("harchi","name",10,5,1));
        addRoom(new Room("harchi","name",10,5,1));
        addRoom(new Room("harchi","name",10,5,1));
        addRoom(new Room("harchi","name",10,5,1));

    }

    @Override
    public void onCreate(Room room){
        addRoom(room);
    }
}
