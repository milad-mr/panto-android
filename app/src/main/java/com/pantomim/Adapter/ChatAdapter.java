package com.pantomim.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pantomim.Model.Chat;
import com.pantomim.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aryahm on 1/22/18.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder>  {

    private List<Chat> parent = new ArrayList<Chat>();
    public ChatAdapter (List<Chat> parent) {
        this.parent=parent;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,chat;
        public MyViewHolder(View view ) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            chat = (TextView) view.findViewById(R.id.chat);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);

        return new MyViewHolder(itemView);

    }
    @Override
    public int getItemViewType(int postion){
        return postion;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Chat item = parent.get(position);
        holder.name.setText(item.getName());
        holder.chat.setText(item.getChat());

    }

    @Override
    public int getItemCount() {
        return parent.size();
    }

}
