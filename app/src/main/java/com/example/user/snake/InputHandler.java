package com.example.user.snake;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by user on 21.11.2016.
 */
public class InputHandler implements View.OnTouchListener {

    Direction result;
    int width;

    public InputHandler(int width)
    {
        this.width = width;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getX() > event.getY())
        {
            if(width - event.getX() > event.getY())
            {
                result = Direction.UP;
            }
            else
            {
               result = Direction.RIGHT;
            }
        }
        else
        {
            if(width - event.getX() > event.getY())
            {
                result = Direction.LEFT;
            }
            else
            {
                result = Direction.DOWN;
            }
        }
        return false;
    }
}
