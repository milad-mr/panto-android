package com.pantomim;

/**
 * Created by aryahm on 1/22/18.
 */

public class Chat {
    private String chat;
    private String name;

    public Chat(String chat, String name) {
        this.chat = chat;
        this.name = name;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
