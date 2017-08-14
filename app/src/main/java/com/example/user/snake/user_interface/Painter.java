package com.example.user.snake.user_interface;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.Log;

import com.example.user.snake.assets.Assets;
import com.example.user.snake.communication.Answers.Board;
import com.example.user.snake.communication.Answers.Point;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

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
            textPaint.reset();
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setTextSize(textSize);
            textPaint.setFakeBoldText(true);
        }

        if(style == TextStyle.WONSZ) {
            textPaint.setShader(new BitmapShader(Assets.dots, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
        }
        else {
            textPaint.setColor(Color.GRAY);
        }
    }

    public void paintText(String text, TextStyle style)
    {
        setTextStyle(style);
        canvas.drawText(text, width/2, height/2, textPaint);
    }

    //plansze
    public void paintBoard(Board board)
    {
        boardBox.draw(canvas);
        for (int i=0; i<board.board.length; i++) {
            for (int j=0; j< board.board[i].length; j++) {
                paint(i, j, board.board[i][j]);
            }
        }
    }

    public void paint(int x, int y, Board.BoxType type)
    {
        if(type != null) {
            box.bitmap = null;
            switch (type) {
                case HEAD:
                    box.bitmap = Assets.head;
                    break;
                case BODY:
                    box.bitmap = Assets.segment;
                    break;
                case ENEMY_BODY:
                    box.bitmap = Assets.segment_enemy;
                    break;
                case ENEMY_HEAD:
                    box.bitmap = Assets.head_enemy;
                    break;
                case MEAL:
                    box.bitmap = Assets.meal;
                    break;
                case WALL:
                    box.bitmap = Assets.wall;
                    break;
                case LASER:
                    box.bitmap = Assets.laser;
                    break;
                default:
                    break;
            }

            if (box.bitmap != null) {
                box.setBounds(x, y);
                box.draw(canvas);
            }
        }
    }
}
