package com.example.user.snake.communication.Queries;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by user on 21.12.2016.
 */
public class JsonMessage {
    protected String query = null;
    protected static int id;
    protected static String address;
    protected String TAG = "";

    protected JsonMessage(String _query)
    {
        query = _query;
    }

    public String getQuery()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(address);
        builder.append(query);
        builder.append(id);
        Log.v(TAG, builder.toString());
        return builder.toString();
    }

    public static int getId()
    {
        return id;
    }

    public static void setId(int _id)
    {
        id = _id;
    }

    public void setAddress(String ip) {
        StringBuilder builder = new StringBuilder();
        builder.append("http://");
        builder.append(ip);
        builder.append(":3911/wonsz");
        address = builder.toString();
    }
}
