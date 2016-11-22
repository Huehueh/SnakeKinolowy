package com.example.user.snake;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by user on 08.11.2016.
 */
public class Point implements Serializable{
    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point()
    {

    }


    public Point (int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Point(Point previous, Direction direction, Point board)
    {
        this(previous.getX(), previous.getY());
        switch (direction)
        {
            case DOWN:
                moveDown(board.getY());
                break;
            case RIGHT:
                moveRight(board.getX());
                break;
            case UP:
                moveUp(board.getY());
                break;
            case LEFT:
                moveLeft(board.getX());
                break;
        }
    }

    private void moveUp(int sizeY)
    {
        if(y == 0)
        {
            y = sizeY - 1;
        }
        else
        {
            y--;
        }
    }

    private void moveDown(int sizeY)
    {
        if(y == sizeY - 1)
        {
            y = 0;
        }
        else
        {
            y++;
        }
    }

    private void moveRight(int sizeX)
    {
        if(x == sizeX -1)
        {
            x = 0;
        }
        else
        {
            x++;
        }
    }

    private void moveLeft(int sizeX)
    {
        if(x == 0)
        {
            x = sizeX - 1;
        }
        else
        {
            x--;
        }
    }
}
