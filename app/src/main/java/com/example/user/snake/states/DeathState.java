package com.example.user.snake.states;

import com.example.user.snake.communication.Direction;
import com.example.user.snake.assets.Assets;
import com.example.user.snake.main.GameFragment;
import com.example.user.snake.user_interface.Painter;
import com.example.user.snake.communication.Queries.Steering;

/**
 * Created by user on 03.12.2016.
 */
public class DeathState extends GameState {

    int deathCount;

    public DeathState(GameFragment gameFragment)
    {
        super(gameFragment, StateName.death);
    }

    @Override
    public void init() {
        super.init();
        Assets.playSound(Assets.deathId);
        steerings.removeAllElements();

        deathCount = 10;
        new CountDownThread().start();

    }

    @Override
    public synchronized void render(Painter g) {
        g.paintBoard();
        renderMeal(g);
        renderWalls(g);
        renderEnemies(g);
        renderSnake(g);
        renderLaser(g);
        g.paintText("You are dead \n" + deathCount, Painter.TextStyle.WONSZ);

    }

    @Override
    public boolean addSteering(Direction dir) {
        return false;
    }

    @Override
    public Steering getSteering() {
        return new Steering(Direction.NO_DIRECTION, false);
    }

    public class CountDownThread extends Thread
    {
        public void run() {
            deathCount = gameFragment.TIME_TO_RESP + 1;
            while(deathCount > 0)
            {
                deathCount--;
                try{
                    Thread.sleep(1000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            gameFragment.startPlaying();
        }
    }
}
