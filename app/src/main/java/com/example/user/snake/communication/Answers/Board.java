package com.example.user.snake.communication.Answers;

import android.util.Log;

import com.example.user.snake.communication.Direction;

/**
 * Created by user on 02.06.2017.
 */
public class Board {
    private static int sizeX;
    private static int sizeY;
    public BoxType [][] board;
    public static String TAG = "board";

    public enum BoxType
    {
        NOTHING,
        BODY,
        ENEMY_BODY,
        HEAD,
        ENEMY_HEAD,
        WALL,
        LASER,
        MEAL
    }

    public static int getSizeY() {
        return sizeY;
    }

    public static int getSizeX() {
        return sizeX;
    }

    public Board(int x, int y)
    {
        sizeY = y;
        sizeX = x;
        board = new BoxType[sizeX][sizeY];
        eraseBoard();
    }

    public void eraseBoard()
    {
        for (int i=0; i<board.length; i++)
        {
            for (int j=0; j<board[i].length;j++)
            {
                board[i][j] = BoxType.NOTHING;
            }
        }
    }

    public void insertWall(Point [] points)
    {
        insert(points, BoxType.WALL);
    }

    public void insertMeal(Point [] points)
    {
        insert(points, BoxType.MEAL);
    }

    public void insertSnake(Point [] points)
    {
        insert(points, BoxType.BODY);
        board[points[0].getX()][ points[0].getY()] = BoxType.HEAD;
    }

    private void insert(Point [] points, BoxType type)
    {
        for (Point point : points) {
            board[point.getX()][point.getY()] = type;
        }
        Log.v(TAG, "insert " + type);
    }

    public void insertSnake(Snake snake, boolean isEnemy)
    {
        int i = snake.getX();
        int j = snake.getY();
        if(isEnemy)
            board[i][j] = BoxType.ENEMY_HEAD;
        else
            board[i][j] = BoxType.HEAD;
        for (int move : snake.seg) {
            switch (Direction.getDirection(move))
            {
                case DOWN:
                    j = moveDown(j);
                    break;
                case RIGHT:
                    i = moveRight(i);
                    break;
                case UP:
                    j = moveUp(j);
                    break;
                case LEFT:
                    i = moveLeft(i);
                    break;
            }
            if(isEnemy)
                board[i][j] = BoxType.ENEMY_BODY;
            else
                board[i][j] = BoxType.BODY;
        }

        if(snake.laser)
        {
            Direction tempDir = Direction.getDirection(snake.seg[0]).getOpposite();
            for(int k=0; k<3; k++)
            {
                switch (tempDir)
                {
                    case DOWN:
                        j = moveDown(j);
                        break;
                    case RIGHT:
                        i = moveRight(i);
                        break;
                    case UP:
                        j = moveUp(j);
                        break;
                    case LEFT:
                        i = moveLeft(i);
                        break;
                }
                board[i][j] = BoxType.LASER;
            }
        }
        Log.v(TAG, "insert snake");
    }

    private int moveUp(int y)
    {
        if(y == 0)
        {
            y = Board.getSizeY() - 1;
        }
        else
        {
            y--;
        }
        return y;
    }

    private int moveDown(int y)
    {
        if(y == getSizeY() - 1)
        {
            y = 0;
        }
        else
        {
            y++;
        }
        return y;
    }

    private int moveRight(int x)
    {
        if(x == getSizeX() -1)
        {
            x = 0;
        }
        else
        {
            x++;
        }
        return x;
    }

    private int moveLeft(int x)
    {
        if(x == 0)
        {
            x = getSizeX() - 1;
        }
        else
        {
            x--;
        }
        return x;
    }


}
