package com.example.user.snake.graphics;

import com.example.user.snake.graphics.Box;

/**
 * Created by user on 16.12.2016.
 */
public class ButtonBox extends Box {

    public ButtonBox(int color, String letters, int letterColor) {
        super(color);

    }


    public boolean isClicked(int touchX, int touchY) {
        return bounds.contains(touchX, touchY);
    }
}


