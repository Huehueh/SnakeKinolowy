package com.example.user.snake.states;

import com.example.user.snake.communication.Direction;
import com.example.user.snake.communication.Queries.Steering;
import com.example.user.snake.user_interface.Painter;
import com.example.user.snake.main.GameFragment;

/**
 * Created by user on 30.12.2016.
 */
public class WaitingState extends GameState {

    public WaitingState(GameFragment _gameFragment) {
        super(_gameFragment, StateName.waiting);
    }

    @Override
    public void init() {
        super.init();
        gameFragment.running = true;
    }

    @Override
    public void render(Painter g) {
        g.paintBoard();
        g.paintText("Waiting for" + System.lineSeparator() + " more players", Painter.TextStyle.WONSZ);
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
