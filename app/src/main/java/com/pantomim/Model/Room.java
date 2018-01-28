package com.pantomim.Model;

/**
 * Created by aryahm on 1/26/18.
 */

public class Room {
    private String name;
    private String ownername;
    private int max;
    private int current;
    private String id;

    public Room(String name, String ownername, int max, int current, String id) {
        this.name = name;
        this.ownername = ownername;
        this.max = max;
        this.current = current;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
