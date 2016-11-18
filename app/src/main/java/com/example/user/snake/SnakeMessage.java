package com.example.user.snake;

import java.io.Serializable;

/**
 * Created by user on 08.11.2016.
 */
public class SnakeMessage{

    public static Point boardSize;

    public class Snake
    {
        int x;
        int y;
        int [] seg;

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int[] getSeg() {
            return seg;
        }

        public Point [] transform()
        {
            Point [] result = new Point[seg.length + 1];
            result[0] = new Point(x, y);
            for(int i = 0; i < seg.length; i++)
            {
                result[i+1] = new Point(result[i], Direction.getDirection(seg[i]), SnakeMessage.boardSize);
            }
            return result;
        }
    }

    private int id;
    private int notification;//1-zjadlam, 0 - nie, 2 zderzenie
    private Point[] meal;
    private Point[] wall;
    private Snake snake;
    private Snake [] enemies;

    public int getNotification() {
        return notification;
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
