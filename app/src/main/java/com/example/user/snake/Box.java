package com.example.user.snake;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by user on 08.11.2016.
 */
public class Box {
    private Paint boardPaint;
    private Rect bounds;
    static int mnoznik = 1;

    public Box(int color)
    {
        boardPaint = new Paint();
        boardPaint.setColor(color);

        bounds = new Rect();
    }

    public void setBounds(int i, int j, int i1, int j1)
    {
        bounds.set(i*mnoznik, j*mnoznik, i1*mnoznik, j1*mnoznik);
    }

    public void setBounds(int i, int j)
    {
        bounds.set(i*mnoznik, j*mnoznik, (i+1)*mnoznik, (j+1)*mnoznik);
    }

    public void draw(Canvas canvas)
    {
        canvas.drawRect(bounds, boardPaint);
    }
}
