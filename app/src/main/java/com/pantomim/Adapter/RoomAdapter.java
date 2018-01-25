package com.pantomim.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pantomim.Activity.MainActivity;
import com.pantomim.Model.Room;
import com.pantomim.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aryahm on 1/22/18.
 */

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.MyViewHolder>  {

    private boolean imHost;
    private Activity activity;
    private List<Room> parent = new ArrayList<Room>();
    public RoomAdapter (List<Room> parent,Activity activity) {
        this.parent = parent;
        this.activity = activity;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,ownerName,maxCurrent;
        View view;
        public MyViewHolder(View view ) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            ownerName = (TextView) view.findViewById(R.id.owner);
            maxCurrent = (TextView) view.findViewById(R.id.count);
            this.view = view;
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_room, parent, false);

        return new MyViewHolder(itemView);

    }
    @Override
    public int getItemViewType(int postion){
        return postion;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Room item = parent.get(position);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MainActivity.class);
                intent.putExtra("id",String.valueOf(item.getId()));
                intent.putExtra("owner_name",item.getOwnername());
                activity.startActivity(intent);
            }
        });
        holder.name.setText(item.getName());
        holder.ownerName.setText(item.getOwnername());
        holder.maxCurrent.setText(String.valueOf(item.getCurrent() + "/"
         + String.valueOf(item.getMax())));

    }

    @Override
    public int getItemCount() {
        return parent.size();
    }

}
