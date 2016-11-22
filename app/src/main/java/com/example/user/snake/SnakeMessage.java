package com.example.user.snake;

import java.io.Serializable;

/**
 * Created by user on 08.11.2016.
 */
public class SnakeMessage{

    private int id;
    private int notification;//1-zjadlam, 0 - nie, 2 zderzenie
    private Point[] meal;
    private Point[] wall;
    private Snake snake;
    private Snake [] enemies;

    public int getNotification() {
        return notification;
    }

    public Notification getSnakeNotification()
    {
        return Notification.getNotification(notification);
    }

    public int getId() {
        return id;
    }

    public Point [] getMeal() {
        return meal;
    }

    public Point[] getWall()
    {
        return wall;
    }

    public Snake getSnake() {
        return snake;
    }

    public Snake[] getEnemies() {
        return enemies;
    }
}
