package com.example.user.snake.states;

import com.example.user.snake.communication.Direction;
import com.example.user.snake.assets.Assets;
import com.example.user.snake.main.GameFragment;
import com.example.user.snake.user_interface.Painter;
import com.example.user.snake.communication.Queries.Steering;

/**
 * Created by user on 03.12.2016.
 */
public class PlayState extends GameState {

    protected PlayState(GameFragment gameFragment)
    {
        super(gameFragment, StateName.play);
    }

    @Override
    public void init() {
        super.init();
        gameFragment.running = true;
    }


    @Override
    public synchronized void render(Painter g) {
        g.paintBoard();
        renderLaser(g);
        renderMeal(g);
        renderWalls(g);
        renderEnemies(g);
        renderSnake(g);
    }

    @Override
    public void sendLaser() {
        laser = true;
        Assets.playSound(Assets.laserId);
    }

    @Override
    public boolean addSteering(Direction dir)  {
        if(checkIfOk(dir))
        {
            steerings.add(new Steering(dir, laser));
            if(laser)
            {
                laser = false;
            }
        }
        return true;
    }

    @Override
    public Steering getSteering() {
        Steering steering;
        if(!steerings.isEmpty())
        {
            steering = steerings.firstElement();
            steerings.remove(0);
        }
        else
        {
            steering = new Steering(currentDirection, laser);
            if(laser)
            {
                laser = false;
            }
        }
        currentDirection = steering.getDir();

        return steering;
    }


    private boolean checkIfOk(Direction newDirection)
    {
        if(steerings.isEmpty())
        {
            /*wykomentowac jezeli chcesz by klikniecie do tylu zatrzymywalo*/
            if(newDirection.isOpposite(currentDirection) || newDirection == currentDirection)
            {
                return false;
            }
            else {
                return true;
            }
        }
        else {
            Direction oldDirection = steerings.lastElement().getDir();
            if(oldDirection == newDirection || newDirection.isOpposite(oldDirection))
            {
                return false;
            }
            else {
                return true;
            }
        }
    }
}
