package com.example.user.snake.communication.Queries;

import android.util.Log;

/**
 * Created by user on 28.12.2016.
 */
public class LogOut extends JsonMessage {


    public LogOut() {
        super("?logout=");
        TAG = "LogOut";
    }
}
