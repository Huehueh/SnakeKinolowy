package com.example.user.snake.communication.Answers;

import com.example.user.snake.communication.Direction;

import java.io.Serializable;

/**
 * Created by user on 08.11.2016.
 */
public class Point implements Serializable{
    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point()
    {

    }

    public Point (int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Point(Point previous, Direction direction)
    {
        this(previous.getX(), previous.getY());
        switch (direction)
        {
            case DOWN:
                moveDown();
                break;
            case RIGHT:
                moveRight();
                break;
            case UP:
                moveUp();
                break;
            case LEFT:
                moveLeft();
                break;
        }
    }

    private void moveUp()
    {
        if(y == 0)
        {
            y = Board.getSizeY() - 1;
        }
        else
        {
            y--;
        }
    }

    private void moveDown()
    {
        if(y == Board.getSizeY() - 1)
        {
            y = 0;
        }
        else
        {
            y++;
        }
    }

    private void moveRight()
    {
        if(x == Board.getSizeX() -1)
        {
            x = 0;
        }
        else
        {
            x++;
        }
    }

    private void moveLeft()
    {
        if(x == 0)
        {
            x = Board.getSizeX() - 1;
        }
        else
        {
            x--;
        }
    }
}
