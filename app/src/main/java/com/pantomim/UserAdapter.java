package com.pantomim;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aryahm on 1/22/18.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>  {

    private boolean imHost;
    private List<User> parent = new ArrayList<User>();
    public UserAdapter (List<User> parent, boolean imHost) {
        this.parent = parent;
        this.imHost = imHost;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        public MyViewHolder(View view ) {
            super(view);
            card = (CardView) view.findViewById(R.id.card);
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
    }

    @Override
    public int getItemCount() {
        return parent.size();
    }

}
