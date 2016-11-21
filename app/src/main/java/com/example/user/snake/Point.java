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

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeIntArray(new int[]{x,y});
//    }
//
//    public static final Parcelable.Creator<Point> CREATOR = new Parcelable.Creator<Point>()
//    {
//        @Override
//        public Point createFromParcel(Parcel source) {
//            return new Point(source);
//        }
//
//        @Override
//        public Point[] newArray(int size) {
//            return new Point[size];
//        }
//    };
//
//    private Point(Parcel in)
//    {
//        int[] array = new int[2];
//        in.readIntArray(array);
//        x = array[0];
//        y = array[1];
//    }

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
