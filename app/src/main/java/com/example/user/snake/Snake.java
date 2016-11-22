package com.example.user.snake;

import android.util.Log;

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

    public Point [] transformToArray(Point boardSize)
    {
        Point [] result = new Point[seg.length + 1];
        result[0] = new Point(x, y);

        for(int i = 0; i < seg.length; i++)
        {
            result[i+1] = new Point(result[i], Direction.getDirection(seg[i]), boardSize);
        }
        return result;
    }

    public Point[] getLaserArray(Point boardSize)
    {
        Point[] laserArray = null;
        if(laser) {
            laserArray = new Point[3];
            Direction myDirection = Direction.getDirection(seg[0]).getOpposite();
            laserArray[0] = new Point(new Point(x, y), myDirection, boardSize);
            laserArray[1] = new Point(laserArray[0], myDirection, boardSize);
            laserArray[2] = new Point(laserArray[1], myDirection, boardSize);
        }
        else {
            laserArray = new Point[0];
        }
        return laserArray;
    }


}
