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
import java.util.List;

/**
 * Created by user on 08.11.2016.
 */
public class BoardImage extends View{

    public GameFragment gameFragment;
    Context context;
    Canvas canvas;
    Paint textPaint;
    Box box, boardBox;
    public static int width;

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
        boardBox = new Box(Color.BLACK);
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
        this.canvas = canvas;
        boardBox.draw(canvas);

        paintWithHead(gameFragment.segments, Color.WHITE, Color.GRAY);
        paintWithHead(gameFragment.enemies, Color.GREEN, Color.CYAN);
        paint(gameFragment.walls, Color.BLUE);
        paint(gameFragment.meals, Color.RED);
        paint(gameFragment.laser, Color.YELLOW);

        canvas.drawText(getDisplayText(), width/2, width/2, textPaint);

        invalidate();  // Force a re-draw
    }

    private void paint(Point position, int color)
    {
        box = new Box(color);
        box.setBounds(position.getX(), position.getY());
        box.draw(canvas);
    }

    private void paint(Point [] positions, int color)
    {
        if(positions!=null) {
            for (Point position : positions) {
                paint(position, color);
            }
        }
    }

    private void paintWithHead(Point [] positions, int color, int color2)
    {
        paint(positions, color);
        if(positions != null) {
            if(positions.length > 0)
                paint(positions[0], color2);
        }
    }

    private void paintWithHead(List<Point []> positions, int color, int color2)
    {
        if(positions != null)
        {
            for (Point[] position : positions) {
                paintWithHead(position, color, color2);
            }
        }
    }

    private String getDisplayText()
    {
        if(gameFragment.gameWorking)
        {
            return "";
        }
        else
        {
            if(gameFragment.deathCount == -1) {
                return getResources().getText(R.string.click_start).toString();
            }
            else {
                return gameFragment.deathCount+"";
            }

        }
    }
}
