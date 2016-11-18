package com.example.user.snake;

import java.io.Serializable;

/**
 * Created by user on 08.11.2016.
 */
public class SnakeMessage implements Serializable{
    private int id;
    private Point newPos;
    private int notification;//1-zjadlam, 0 - nie, 2 zderzenie
    private int length;
    private Point meal;
    private Point[] enemiesPositions;

    public Point getNewPos() {
        return newPos;
    }

    public int getNotification() {
        return notification;
    }

    public int getLength() {
        return length;
    }

    public int getId() {
        return id;
    }

    public Point getMeal() {
        return meal;
    }

    public Point[] getEnemiesPositions() {
        return enemiesPositions;
    }


}
