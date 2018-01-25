package com.pantomim.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pantomim.Interface.WinnerInterface;
import com.pantomim.Model.User;
import com.pantomim.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aryahm on 1/22/18.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>  {

    private boolean imHost;
    private WinnerInterface winner;
    private List<User> parent = new ArrayList<User>();
    public UserAdapter (List<User> parent, boolean imHost, WinnerInterface winner) {
        this.winner = winner;
        this.parent = parent;
        this.imHost = imHost;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        View view;
        public MyViewHolder(View view ) {
            super(view);
            card = (CardView) view.findViewById(R.id.card);
            this.view = view;
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);

        return new MyViewHolder(itemView);

    }
    @Override
    public int getItemViewType(int postion){
        return postion;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final User item = parent.get(position);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        item.setCard(holder.card);
        if(imHost)
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    winner.selectWinner(item.getUserId());
                }
            });
    }

    @Override
    public int getItemCount() {
        return parent.size();
    }

}
