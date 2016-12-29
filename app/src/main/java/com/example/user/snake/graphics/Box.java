package com.example.user.snake.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by user on 08.11.2016.
 */
public class Box {
    private Paint boardPaint;
    protected Rect bounds;
    private static int multiplier = 1;
    private Bitmap bitmap = null;

    public enum BoxType
    {
        HEAD,
        SEGMENT,
        WALL,
        MEAL,
        LASER,
        HEAD_ENEMY,
        SEGMENT_ENEMY
    }

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

    public void setType(BoxType type)
    {
        switch (type)
        {
            case HEAD:
                bitmap = Assets.head;
                break;
            case SEGMENT:
                bitmap = Assets.segment;
                break;
            case WALL:
                bitmap = Assets.wall;
                break;
            case MEAL:
                bitmap = Assets.meal;
                break;
            case LASER:
                bitmap = Assets.laser;
                break;
            case HEAD_ENEMY:
                bitmap = Assets.head_enemy;
                break;
            case SEGMENT_ENEMY:
                bitmap = Assets.segment_enemy;
                break;
        }
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
            Log.v("Assets", "Draw bitmap");
            canvas.drawBitmap(bitmap, null, bounds, null);
        }
    }
}
