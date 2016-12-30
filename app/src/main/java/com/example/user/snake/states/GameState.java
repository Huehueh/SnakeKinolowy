package com.example.user.snake.states;

import android.graphics.Color;

import com.example.user.snake.communication.Direction;
import com.example.user.snake.graphics.Box;
import com.example.user.snake.main.GameFragment;
import com.example.user.snake.graphics.Painter;
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

    //RYSOWANIE
    Point[] segments, meals, walls, lasers;
    List<Point []> enemies;
    public static Point boardSize;
    public boolean news = false;

    //STEROWANIE
    public Stack<Steering> steerings;
    Direction currentDirection = Direction.NO_DIRECTION;
    boolean laser = false;

    public GameState(GameFragment _gameFragment, StateName _name)
    {
        this.gameFragment = _gameFragment;
        steerings = new Stack<>();
        this.name = _name;
    }

    public void setCurrentState(GameState newState)
    {
        gameFragment.setCurrentState(newState);
    }

    public abstract void init();

    public abstract void render(Painter g);

    public StateName getName()
    {
        return name;
    }

    public void sendLaser()
    {
        laser = true;
    }

    public abstract Steering getSteering();

    public abstract boolean addSteering(Direction dir);

    public void setStates(SnakeMessage snakeMessage)
    {
        //RYSOWANIE
        segments = snakeMessage.getSnake().transformToArray(boardSize);
        enemies = new ArrayList<>();
        for (int i = 0; i< snakeMessage.getEnemies().length; i++) {
            enemies.add(snakeMessage.getEnemies()[i].transformToArray(boardSize));
        }
        walls = snakeMessage.getWall();
        meals = snakeMessage.getMeal();
        lasers = snakeMessage.getSnake().getLaserArray(boardSize);
        for (int i = 0; i< snakeMessage.getEnemies().length; i++) {
            lasers = ArrayUtils.addAll(lasers, snakeMessage.getEnemies()[i].getLaserArray(boardSize));
        }
        news = true;
    }

    public void setInitialState(User user)
    {
        segments = user.getPositions();
        boardSize = user.getSize();
    }

    //painting
    protected void renderSnake(Painter p)
    {
        p.paintWithHead(segments);
    }

    protected void renderEnemies(Painter p)
    {
        p.paintWithHead(enemies);
    }

    protected void renderWalls(Painter p)
    {
        p.paint(walls, Box.BoxType.WALL);
    }

    protected void renderLaser(Painter p)
    {
        p.paint(lasers, Box.BoxType.LASER);
    }

    protected void renderMeal(Painter p)
    {
        p.paint(meals, Box.BoxType.MEAL);
    }

}
