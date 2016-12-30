package com.example.user.snake.communication.Queries;

/**
 * Created by user on 21.12.2016.
 */
public class Reset extends JsonMessage {

    public Reset()
    {
        super("?reset=");
        TAG = "Reset";
    }


}
