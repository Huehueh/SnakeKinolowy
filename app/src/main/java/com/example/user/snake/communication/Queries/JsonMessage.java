package com.example.user.snake.communication.Queries;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by user on 21.12.2016.
 */
public class JsonMessage {
    protected String query = null;
    protected static Integer id;
    protected static String address = null;
    protected String TAG = "";
    protected String message = null;

    protected JsonMessage(String _query)
    {
        query = _query;
        if(id != null && address != null)
        {
            setQuery();
        }
    }

    public void setQuery()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(address);
        builder.append(query);
        builder.append(id);
        //Log.v(TAG, builder.toString());
        message = builder.toString();
    }

    public String getQuery()
    {
        if(message== null)
        {
            setQuery();
        }
        return message;
    }

    public static int getId()
    {
        return id;
    }

    public static void setId(int _id)
    {
        id = _id;
    }

    public static void setAddress(String ip) {
        StringBuilder builder = new StringBuilder();
        builder.append("http://");
        builder.append(ip);
        builder.append(":3911/wonsz");
        address = builder.toString();
    }
}
