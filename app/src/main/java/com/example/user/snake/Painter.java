package com.example.user.snake;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.List;

/**
 * Created by user on 21.11.2016.
 */
public class Painter {
    private Canvas canvas;
    Paint textPaint;
    Box box, boardBox;
    int x, y;

    public Painter(Canvas canvas, int x, int y)
    {
        this.canvas = canvas;
        this.x = x;
        this.y = y;
    }

    public void drawText(String text)
    {
        textPaint = new Paint();
        textPaint.setColor(Color.GRAY);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(Box.mnoznik*2);
        canvas.drawText(text, x/2, y/2, textPaint);
    }

    public void paintBoard()
    {
        boardBox = new Box(Color.BLACK);
        boardBox.setBounds(0, 0, x, y);
        boardBox.draw(canvas);
    }

    public void paint(Point position, int color)
    {
        box = new Box(color);
        box.setBounds(position.getX(), position.getY());
        box.draw(canvas);
    }

    public void paint(Point [] positions, int color)
    {
        if(positions!=null) {
            for (Point position : positions) {
                paint(position, color);
            }
        }
    }

    public void paintWithHead(Point [] positions, int color, int color2)
    {
        paint(positions, color);
        if(positions != null) {
            if(positions.length > 0)
                paint(positions[0], color2);
        }
    }

    public void paintWithHead(List<Point []> positions, int color, int color2)
    {
        if(positions != null)
        {
            for (Point[] position : positions) {
                paintWithHead(position, color, color2);
            }
        }
    }
}
