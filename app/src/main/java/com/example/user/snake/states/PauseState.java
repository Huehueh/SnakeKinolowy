package com.example.user.snake.states;

import com.example.user.snake.communication.Direction;
import com.example.user.snake.communication.Queries.Steering;
import com.example.user.snake.user_interface.Painter;
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
        super.init();
        steerings.removeAllElements();
    }

    @Override
    public synchronized void render(Painter g) {
        super.render(g);
        g.paintText("PAUSE", Painter.TextStyle.WONSZ);
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
