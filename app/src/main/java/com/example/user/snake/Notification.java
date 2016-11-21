package com.example.user.snake;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by user on 21.11.2016.
 */
public enum Notification {
    NIC(0),
    OZYLES(1),
    UMARLES(2),
    ZJADLES(3);

    private int value;
    Notification(int value)
    {
        this.value = value;
    }
    public int getValue()
    {
        return value;
    }

    private static HashMap<Integer, Notification> map;

    private static void initList()
    {
        map = new HashMap<>();
        for (Notification notification : values()) {
            map.put(notification.value, notification);
        }
    }

    public static Notification getNotification(int i)
    {
        if(map == null)
        {
            initList();
        }
        Log.v("NOTYFIKACJA", map.get(i).toString());
        return map.get(i);
    }

}
