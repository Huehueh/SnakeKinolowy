package com.example.user.snake.states;

import com.example.user.snake.communication.Direction;
import com.example.user.snake.communication.Queries.Steering;
import com.example.user.snake.assets.Assets;
import com.example.user.snake.user_interface.Painter;
import com.example.user.snake.main.GameFragment;

/**
 * Created by user on 30.12.2016.
 */
public class EndGameState extends GameState {
    public EndGameState(GameFragment _gameFragment) {
        super(_gameFragment, StateName.endGame);
    }

    @Override
    public void init() {
        super.init();
        Assets.stopMusic();
        Assets.playSound(Assets.endGameId);
        gameFragment.running = false;
        gameFragment.askForResults();
    }

    @Override
    public void render(Painter g) {
        super.render(g);
        g.paintText("THE END", Painter.TextStyle.WONSZ);

    }

    @Override
    public Steering getSteering() {
        return new Steering(Direction.NO_DIRECTION, false);
    }

    @Override
    public boolean addSteering(Direction dir) {
        return false;
    }
}
