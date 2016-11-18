package com.example.user.snake;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcelable;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Deque;

/**
 * Created by user on 08.11.2016.
 */
public class BoardImage extends View{

    ArrayList<Point> snakePositions;
    Point[] enemiesPositions;
    Point meal;
    Paint textPaint;
    Box boardBox, snakeBox, mealBox, enemyBox, headBox;
    static  int width;
    public GameFragment gameFragment;
    Context context;

    public BoardImage(Context context)
    {
        super(context);

        boardBox = new Box(Color.BLACK);
        snakeBox = new Box(Color.WHITE);
        mealBox = new Box(Color.RED);
        enemyBox = new Box(Color.BLUE);
        headBox = new Box(Color.GRAY);
        setFocusable(true);
        requestFocus();

        textPaint = new Paint();
        textPaint.setColor(Color.GRAY);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public static BoardImage newInstance(Context context, int x, int y)
    {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        android.graphics.Point point = new android.graphics.Point();
        display.getSize(point);
        if(point.x < point.y)   {
            width = point.x;
            Box.mnoznik = (int)(width/x);
        }
        else {
            width = point.y;
            Box.mnoznik = (int)(width/y);
        }

        BoardImage boardImage = new BoardImage(context);
        boardImage.context = context;
        boardImage.boardBox.setBounds(0, 0, x, y);
        boardImage.textPaint.setTextSize(Box.mnoznik*2);
        return boardImage;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getX() > event.getY())
        {
            if(width - event.getX() > event.getY())
            {
                gameFragment.setNewDirection(context.getResources().getInteger(R.integer.up));
            }
            else
            {
                gameFragment.setNewDirection(context.getResources().getInteger(R.integer.right));
            }
        }
        else
        {
            if(width - event.getX() > event.getY())
            {
                gameFragment.setNewDirection(context.getResources().getInteger(R.integer.left));
            }
            else
            {
                gameFragment.setNewDirection(context.getResources().getInteger(R.integer.down));
            }
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        boardBox.draw(canvas);

        if(enemiesPositions != null) {
            for (Point point : enemiesPositions) {
                enemyBox.setBounds(point.getX(), point.getY(), point.getX()+1, point.getY()+1);
                enemyBox.draw(canvas);
            }
        }

        if(snakePositions != null) {
            for (Point point : snakePositions) {
                snakeBox.setBounds(point.getX(), point.getY(), point.getX()+1, point.getY()+1);
                snakeBox.draw(canvas);
            }
            Point head = snakePositions.get(snakePositions.size()-1);
            headBox.setBounds(head.getX(), head.getY(), head.getX() + 1, head.getY()+1);
            headBox.draw(canvas);
        }

        if(meal != null) {
            mealBox.setBounds(meal.getX(), meal.getY(), meal.getX() + 1, meal.getY() + 1);
            mealBox.draw(canvas);
        }

        canvas.drawText(getDisplayText(), width /2, width /2, textPaint);

        invalidate();  // Force a re-draw
    }

    private String getDisplayText()
    {
        switch (gameFragment.state)
        {
            case working:
                return "";
            case counting:
                return gameFragment.countDownThread.deathCount+"";
            case pause:
                return getResources().getText(R.string.click_start).toString();
            default:
                return "";
        }
    }


    public void update(ArrayList<Point> segments, Point[] enemies, Point meal)
    {
        this.snakePositions = segments;
        this.enemiesPositions = enemies;
        this.meal = meal;
    }
}
