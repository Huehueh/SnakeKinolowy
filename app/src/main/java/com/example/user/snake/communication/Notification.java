package com.example.user.snake.communication;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by user on 21.11.2016.
 */
public enum Notification {
    NIC(0),
    OZYLES(1),
    UMARLES(2),
    ZJADLES(3),
    KONIEC_GRY(4);

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
        return map.get(i);
    }

}
