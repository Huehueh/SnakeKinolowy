package com.example.user.snake.states;

import android.util.Log;

import com.example.user.snake.assets.Assets;
import com.example.user.snake.communication.Answers.Board;
import com.example.user.snake.communication.Answers.Snake;
import com.example.user.snake.communication.Direction;
import com.example.user.snake.user_interface.Box;
import com.example.user.snake.main.GameFragment;
import com.example.user.snake.user_interface.Painter;
import com.example.user.snake.communication.Answers.Point;
import com.example.user.snake.communication.Answers.SnakeMessage;
import com.example.user.snake.communication.Queries.Steering;
import com.example.user.snake.communication.Answers.User;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by user on 23.11.2016.
 */
public abstract class GameState {
    public String TAG;
    protected GameFragment gameFragment;
    public enum StateName {
        play,
        pause,
        death,
        waiting,
        endGame
    }
    protected StateName name;

    Board board;
    public boolean news = false;

    //STEROWANIE
    public Stack<Steering> steerings;
    Direction currentDirection = Direction.NO_DIRECTION;
    boolean laser = false;

    protected GameState(GameFragment _gameFragment, StateName _name)
    {
        this.gameFragment = _gameFragment;
        steerings = new Stack<>();
        this.name = _name;
    }

    public static GameState newInstance(GameFragment gameFragment, StateName name)
    {
        GameState newState;
        switch (name) {
            case waiting:
                newState = new WaitingState(gameFragment);
                break;
            case play:
                newState = new PlayState(gameFragment);
                break;
            case pause:
                newState = new PauseState(gameFragment);
                break;
            case endGame:
                newState = new EndGameState(gameFragment);
                break;
            case death:
                newState = new DeathState(gameFragment);
                break;
            default:
                newState = new PlayState(gameFragment);
                break;
        }
        return newState;
    }

    public void init()
    {
        Log.v("STATE", getName().toString());
    }

    public synchronized void render(Painter g)
    {
        g.paintBoard(board);
    }

    public StateName getName()
    {
        return name;
    }

    public void sendLaser()
    {

    }

    public abstract Steering getSteering();

    public abstract boolean addSteering(Direction dir);

    public void setBoard(SnakeMessage snakeMessage)
    {
        if(board != null) {
            board.eraseBoard();
            board.insertSnake(snakeMessage.getSnake(), false);
            for (Snake enemy : snakeMessage.getEnemies()) {
                board.insertSnake(enemy, true);
            }
            board.insertMeal(snakeMessage.getMeal());
            board.insertWall(snakeMessage.getWall());
            news = true;
        }
    }

    public void setInitialState(User user)
    {
        board = new Board(user.getSize().getX(), user.getSize().getY());
        board.insertSnake(user.getPositions());
    }
}
