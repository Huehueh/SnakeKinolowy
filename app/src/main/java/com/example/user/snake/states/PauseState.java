package com.example.user.snake.states;

import com.example.user.snake.communication.Direction;
import com.example.user.snake.communication.Queries.Steering;
import com.example.user.snake.graphics.Painter;
import com.example.user.snake.main.GameFragment;

/**
 * Created by user on 29.12.2016.
 */
public class PauseState extends GameState {

    public PauseState(GameFragment gameFragment)
    {
        super(gameFragment, StateName.pause);
    }

    @Override
    public void init() {
        steerings.removeAllElements();
    }

    @Override
    public synchronized void render(Painter g) {
        g.paintBoard();
        renderMeal(g);
        renderWalls(g);
        renderEnemies(g);
        renderSnake(g);
        renderLaser(g);
        g.paintText("PAUSE", Painter.TextStyle.NORMAL);
    }

    @Override
    public boolean addSteering(Direction dir) {
        return false;
    }

    @Override
    public Steering getSteering() {
        return new Steering(Direction.NO_DIRECTION, false);
    }
}
