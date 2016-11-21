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

//        Log.v(TAG, "length "+ seg.length + 1);
        for(int i = 0; i < seg.length; i++)
        {
//            Log.v(TAG, "direction " + seg[i]);
            result[i+1] = new Point(result[i], Direction.getDirection(seg[i]), SnakeMessage.boardSize);
        }
        return result;
    }
}
