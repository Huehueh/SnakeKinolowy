package com.example.user.snake.communication.Answers;

import java.io.Serializable;

/**
 * Created by user on 08.11.2016.
 */
public class User implements Serializable{
    public final static String USER = "USER";

    private int id;
    private String login;
    private float frameRate;
    private Point size;
    private Point [] positions;
    private int time2resp;

    public Point[] getPositions() {
        return positions;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getId() {
        return id;
    }

    public float getFrameRate() {
        return frameRate;
    }

    public Point getSize() {
        return size;
    }

    public int getTime2resp() {
        return time2resp;
    }



}
