package com.example.user.snake.user_interface;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.user.snake.assets.Assets;

/**
 * Created by user on 08.11.2016.
 */
public class Box {
    private Paint boardPaint;
    protected Rect bounds;
    public static int multiplier = 1;
    public Bitmap bitmap = null;

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
        if(bitmap == null) {
            canvas.drawRect(bounds, boardPaint);
        }
        else {
            canvas.drawBitmap(bitmap, null, bounds, null);
        }
    }
}
