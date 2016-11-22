package com.example.user.snake;

/**
 * Created by user on 22.11.2016.
 */
public class Steering {
    private static int id;

    private int direction = 0;

    private boolean laser = false;

    private static int defaultDirection;

    public Steering (Direction newDirection, boolean laser_)
    {
        direction = newDirection.getValue();
        defaultDirection = newDirection.getValue();
        this.laser = laser_;
    }

    public Steering(boolean laser_)
    {
        direction = defaultDirection;
        this.laser = laser_;
    }

    public int getId() {
        return id;
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

    public static void setId(int i)
    {
        id = i;
    }
}
