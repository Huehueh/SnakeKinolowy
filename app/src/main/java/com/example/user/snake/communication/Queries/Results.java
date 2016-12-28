package com.example.user.snake.communication.Queries;

/**
 * Created by user on 21.12.2016.
 */
public class Results extends JsonMessage {

    public Results()
    {
        super("?score=");
        TAG = "RESULTS";
    }

}
