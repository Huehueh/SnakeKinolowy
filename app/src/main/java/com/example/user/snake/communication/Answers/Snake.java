package com.example.user.snake.communication.Answers;

import com.example.user.snake.communication.Direction;

import java.util.Iterator;

/**
 * Created by user on 20.11.2016.
 */
public class Snake {

    private final static String TAG = "snake";
    int x;
    int y;
    int [] seg;
    boolean laser;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int[] getSeg() {
        return seg;
    }

    public boolean isLaser() {
        return laser;
    }

}
