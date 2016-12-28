package com.example.user.snake.graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.user.snake.communication.Point;

import java.util.List;

/**
 * Created by user on 21.11.2016.
 */
public class Painter {
    private Canvas canvas;
    Paint textPaint;
    Box box, boardBox;
    ButtonBox button;
    int width, height;

    public Painter(Canvas canvas, int x, int y)
    {
        this.canvas = canvas;
        this.width = x;
        this.height = y;
        box = new Box();
        boardBox = new Box(Color.BLACK);
        boardBox.setBounds(0, 0, x, y);
        textPaint = new Paint();
        textPaint.setColor(Color.GRAY);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(Box.getMultiplier()*2);
    }

    public void paintText(String text)
    {
        canvas.drawText(text, width/2, height/2, textPaint);
    }

    public void paintButton(String text)
    {
        button = new ButtonBox(Color.BLUE, text, Color.BLACK);

    }

    public void paintBoard()
    {
        boardBox.draw(canvas);
    }

    public void paint(Point position, int color)
    {
        box.setColor(color);
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
        paint(positions, color2);
        if(positions != null) {
            if(positions.length > 0)
                paint(positions[0], color);
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
