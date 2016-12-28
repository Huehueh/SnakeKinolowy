package com.example.user.snake.communication.Queries;

import android.util.Log;

/**
 * Created by user on 21.12.2016.
 */
public class Username extends JsonMessage {

    String login;

    public Username(int id, String _login)
    {
        super("?login=");
        TAG = "Username";
        setId(id);
        login = _login;
    }

    public String getLogin() {
        return login;
    }
}
