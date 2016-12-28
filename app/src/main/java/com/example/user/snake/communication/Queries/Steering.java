package com.example.user.snake.communication.Queries;

import android.util.Log;

import com.example.user.snake.communication.Direction;

/**
 * Created by user on 22.11.2016.
 */
public class Steering extends JsonMessage {
    private int direction = 0;
    private boolean laser = false;

    public Steering (Direction newDirection, boolean laser_)
    {
        super("?move=");
        TAG = "Steering";
        direction = newDirection.getValue();
        this.laser = laser_;
    }

    public int getDirection() {
        return direction;
    }

    public Direction getDir()
    {
        return Direction.getDirection(direction);
    }

    public boolean isLaser() {
        return laser;
    }
}
