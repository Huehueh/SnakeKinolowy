package com.example.user.snake.states;

import android.view.MotionEvent;

import com.example.user.snake.MainActivity;
import com.example.user.snake.Painter;

/**
 * Created by user on 23.11.2016.
 */
public abstract class GameState {
    public void setCurrentState(GameState newState)
    {
        MainActivity.gameFragment.currentState.setCurrentState(newState);
    }

    public abstract void init();

    public abstract void update(float delta);

    public abstract void render(Painter g);

    public abstract boolean onTouch(MotionEvent e, int scaledX, int scaledY);

}
