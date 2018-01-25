package com.pantomim.Model;


import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pantomim.Adapter.ChatAdapter;
import com.pantomim.R;

import java.util.List;

/**
 * Created by aryahm on 1/21/18.
 */

public class User{

    private String name;
    private int userId;
    private ChatAdapter adapter;
    private int score;
    private TextView nameText;
    private ImageView microphone;
    private TextView scoreText;
    private boolean isTalking;

    public User(String name, int userId, int score, ChatAdapter adapter) {
        this.name = name;
        this.userId = userId;
        this.adapter = adapter;
        this.score = score;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.nameText.setText(name);
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
        this.scoreText.setText(score);
    }

    public void microOn(){
        if(!isTalking){
            isTalking = true;
            this.microphone.setVisibility(View.VISIBLE);
        }
    }
    public void setCard(CardView card){
        this.nameText = (TextView) card.findViewById(R.id.name);
        this.microphone = (ImageView) card.findViewById(R.id.microphone);
        this.scoreText = (TextView) card.findViewById(R.id.score);

    }
    public void microOff(){
        if(!isTalking){
            isTalking = false;
            this.microphone.setVisibility(View.INVISIBLE);
        }
    }

    public void sendMessage(String message, List<Chat> chats){
        chats.add(0, new Chat(name,message));
        adapter.notifyDataSetChanged();

    }
}
