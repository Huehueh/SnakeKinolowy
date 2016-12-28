package com.example.user.snake.graphics;

import android.view.MotionEvent;
import android.view.View;

import com.example.user.snake.communication.Direction;
import com.example.user.snake.main.GameFragment;
import com.example.user.snake.states.GameState;

/**
 * Created by user on 21.11.2016.
 */
public class InputHandler implements View.OnTouchListener {

    private Direction result;
    private int width;
    private GameState currentState;
    GameFragment gameFragment;

    public InputHandler(GameFragment gameFragment, int boardWidth)
    {
        this.gameFragment = gameFragment;
        this.width = boardWidth;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        currentState = gameFragment.currentState;
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

        return currentState.addSteering(result);
    }
}
