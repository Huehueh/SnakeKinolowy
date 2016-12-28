package com.example.user.snake.communication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 18.11.2016.
 */
public enum Direction {

    NO_DIRECTION(0),
    DOWN(1),
    RIGHT(2),
    UP(3),
    LEFT(4);

    private int value;
    Direction(int value)
    {
        this.value = value;
    }
    public int getValue()
    {
        return value;
    }

    private static Map<Integer, Direction> map;

    private static void initMapping()
    {
        map = new HashMap<>();
        for(Direction d : values())
        {
            map.put(d.value, d);
        }
    }

    public static Direction getDirection(int i)
    {
        if(map == null){
            initMapping();
        }
        return map.get(i);
    }

    public Direction getOpposite()
    {
        switch (this)
        {
            case RIGHT:
                return LEFT;
            case LEFT:
                return RIGHT;
            case UP:
                return  DOWN;
            case DOWN:
                return UP;
            default:
                return NO_DIRECTION;
        }
    }

    public boolean isOpposite(Direction dir)
    {
        if(dir == getOpposite())
            return true;
        else
            return false;
    }
}
