package com.example.user.snake.graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by user on 08.11.2016.
 */
public class Box {
    private Paint boardPaint;
    protected Rect bounds;
    private static int multiplier = 1;

    public Box()
    {
        boardPaint = new Paint();
        bounds = new Rect();
    }

    public Box(int color)
    {
        this();
        setColor(color);
    }

    public static void setMultiplier(int i)
    {
        multiplier = i;
    }

    public static int getMultiplier()
    {
        return multiplier;
    }

    public void setColor(int i)
    {
        boardPaint.setColor(i);
    }

    public void setBounds(int i, int j, int i1, int j1)
    {
        bounds.set(i* multiplier, j* multiplier, i1* multiplier, j1* multiplier);
    }

    public void setBounds(int i, int j)
    {
        bounds.set(i* multiplier, j* multiplier, (i+1)* multiplier, (j+1)* multiplier);
    }

    public void draw(Canvas canvas)
    {
        canvas.drawRect(bounds, boardPaint);
    }
}
