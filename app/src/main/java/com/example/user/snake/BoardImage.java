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
    Paint textPaint;
    Box box, boardBox;
    static  int width;
    public GameFragment gameFragment;
    Context context;

    public BoardImage(Context context, Point boardSize)
    {
        super(context);
        this.context = context;
        createBoardParameters(boardSize);
    }

    public BoardImage(GameFragment gameFragment, Context context, Point boardSize)
    {
        this(context, boardSize);
        this.gameFragment = gameFragment;
    }

    private void createBoardParameters(Point boardSize)
    {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        android.graphics.Point screenSize = new android.graphics.Point();
        display.getSize(screenSize);
        if(screenSize.x < screenSize.y)   {
            width = screenSize.x;
            Box.mnoznik = (int)Math.floor((double)width/boardSize.getX());
        }
        else {
            width = screenSize.y;
            Box.mnoznik = (int)Math.floor((double)width/boardSize.getY());
        }
        boardBox.setBounds(0, 0, boardSize.getX(), boardSize.getY());
        createTextStyle();
    }

    private void createTextStyle()
    {
        textPaint = new Paint();
        textPaint.setColor(Color.GRAY);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(Box.mnoznik*2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getX() > event.getY())
        {
            if(width - event.getX() > event.getY())
            {
                gameFragment.setNewDirection(Direction.UP);
            }
            else
            {
                gameFragment.setNewDirection(Direction.RIGHT);
            }
        }
        else
        {
            if(width - event.getX() > event.getY())
            {
                gameFragment.setNewDirection(Direction.LEFT);
            }
            else
            {
                gameFragment.setNewDirection(Direction.DOWN);
            }
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        boardBox.draw(canvas);

        paint(canvas, gameFragment.segments, Color.WHITE);
        paint(canvas, gameFragment.enemies, Color.GREEN);
        paint(canvas, gameFragment.walls, Color.BLUE);
        paint(canvas, gameFragment.meals, Color.RED);

        canvas.drawText(getDisplayText(), width/2, width/2, textPaint);

        invalidate();  // Force a re-draw
    }

    private void paint(Canvas canvas, Point [] positions, int color)
    {
        for (Point position : positions) {
            paint(canvas, position, color);
        }
    }

    private void paint(Canvas canvas, Point position, int color)
    {
        box = new Box(color);
        box.setBounds(position.getX(), position.getY());
        box.draw(canvas);
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
}
