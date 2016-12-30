package com.example.user.snake.user_interface;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;

import com.example.user.snake.assets.Assets;
import com.example.user.snake.communication.Answers.Point;

import java.util.List;

/**
 * Created by user on 21.11.2016.
 */
public class Painter {
    private Canvas canvas;
    Paint textPaint;
    Box box, boardBox;
    int width, height;
    public static int textSize;

    public enum TextStyle
    {
        NORMAL,
        WONSZ
    }

    public Painter(Canvas canvas, int x, int y)
    {
        this.canvas = canvas;
        this.width = x;
        this.height = y;
        box = new Box();
        boardBox = new Box(Color.BLACK);
        boardBox.setBounds(0, 0, x, y);

    }

    private void setTextStyle(TextStyle style)
    {
        if(textPaint == null)
        {
            textPaint = new Paint();
        }

        textPaint.reset();
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(textSize);
        textPaint.setFakeBoldText(true);

        switch (style)
        {
            case NORMAL:
                textPaint.setColor(Color.GRAY);
                break;
            case WONSZ:
                Bitmap bitmap = Assets.dots;
                Shader shader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                textPaint.setShader(shader);
                break;
        }
    }

    public void paintText(String text, TextStyle style)
    {
        setTextStyle(style);
        canvas.drawText(text, width/2, height/2, textPaint);
    }

    //plansze
    public void paintBoard()
    {
        boardBox.draw(canvas);
    }

    //jeden element
    public void paint(Point position, Box.BoxType type)
    {
        box.setType(type);
        box.setBounds(position.getX(), position.getY());
        box.draw(canvas);

    }

    //np. sciany, jedzenie
    public void paint(Point [] positions, Box.BoxType type)
    {
        if(positions != null) {
            for (Point position : positions) {
                paint(position, type);
            }
        }
    }

    //wonsze
    public void paintWithHead(Point [] positions)
    {
        if(positions != null) {
            for (int i = 0; i < positions.length; i++) {
                if (i == 0) {
                    paint(positions[i], Box.BoxType.HEAD);
                } else {
                    paint(positions[i], Box.BoxType.SEGMENT);
                }
            }
        }
    }

    private void paintEnemyWithHead(Point [] positions)
    {
        for (int i = 0; i< positions.length; i++) {
            if(i==0)
            {
                paint(positions[i], Box.BoxType.HEAD_ENEMY);
            }
            else{
                paint(positions[i], Box.BoxType.SEGMENT_ENEMY);
            }
        }
    }


    //wrogowie
    public void paintWithHead(List<Point[]> positions)
    {
        if(positions != null) {
            for (Point[] position : positions) {
                paintEnemyWithHead(position);
            }
        }
    }
}
