package com.example.user.snake;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by user on 08.11.2016.
 */
public class GameFragment extends Fragment{

    int TIME_TO_RESP;

    //do przekazania w Bundle
    Point [] segments, enemies, meals, walls;
    Point boardSize;
    GameState state = GameState.pause;
    int deltaTime = 300; //ms default
    Stack<Steering> steerings;
    public MainActivity mainActivity;
    CountDownThread countDownThread;

    //layout
    LinearLayout boardView;
    Button startPauseButton;

    public static String TAG = "GameFragment";

    enum GameState
    {
        working,
        pause,
        counting
    }

    private static class Steering
    {
        private static int id;

        private int direction = 0;

        private static int defaultDirection;

        public Steering (Direction newDirection)
        {
            direction = newDirection.getValue();
            defaultDirection = newDirection.getValue();
        }

        public Steering()
        {
            direction = defaultDirection;
        }

        public int getId() {
            return id;
        }

        public int getDirection() {
            return direction;
        }

        public Direction getDir()
        {
            return Direction.getDirection(direction);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.game_layout, container, false);

        startPauseButton = (Button) rootView.findViewById(R.id.startPauseButton);
        startPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state == GameState.working)
                {
                    pauseGame();
                }
                else
                {
                    startGame();
                }
            }
        });
        boardView = (LinearLayout)rootView.findViewById(R.id.board);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, "onActivityCreated");

        //User Info
        mainActivity = (MainActivity) getActivity();
        boardSize = mainActivity.user.getSize();
        SnakeMessage.boardSize = boardSize;
        deltaTime = (int)(1000/mainActivity.user.getFrameRate());
        TIME_TO_RESP = mainActivity.user.getTime2resp();

        //sterowanie
        Steering.id = mainActivity.user.getId();
        steerings = new Stack<>();

        if(savedInstanceState != null)
        {
            segments = (Point[])savedInstanceState.getSerializable(BundleNames.SEGMENTS);
            meals = (Point[])savedInstanceState.getSerializable(BundleNames.MEAL);
            enemies = (Point[])savedInstanceState.getSerializable(BundleNames.ENEMIES);
            walls = (Point[])savedInstanceState.getSerializable(BundleNames.WALLS);
            state = GameState.pause;
        }

        if(segments == null)
        {
            segments = mainActivity.user.getPositions();
        }

        putBoard();

        sendMessageThread.start();
    }

    public void putBoard()
    {
        BoardImage board = new BoardImage(this, getActivity(), boardSize);
        boardView.addView(board);
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(board.getWindowToken(), 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BundleNames.SEGMENTS, segments);
        outState.putSerializable(BundleNames.MEAL, meals);
        outState.putSerializable(BundleNames.ENEMIES, enemies);
        outState.putSerializable(BundleNames.WALLS, walls);
    }


    @Override
    public void onPause() {
        super.onPause();
        pauseGame();
    }

    public void setNewDirection(Direction dir)
    {
        if(checkIfOk(dir))
        {
            steerings.add(new Steering(dir));
        }
    }

    public void setNewDirection()
    {
        setNewDirection(Direction.NO_DIRECTION);
    }

    private boolean checkIfOk(Direction newDirection)// jako element sterowania?
    {
        if(steerings.isEmpty())
        {
            return true;
        }
        else {
            Direction oldDirection = steerings.lastElement().getDir();
            if(oldDirection == newDirection || newDirection.isOpposite(oldDirection))
            {
                return false;
            }
            else
            {
                return true;
            }
        }
    }

    public void startGame()
    {
        if(state == GameState.pause) {
            state = GameState.working;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startPauseButton.setText(getResources().getText(R.string.pause));
                }
            });
            setNewDirection();
        }
    }

    public void pauseGame()
    {
        if(state == GameState.working)
        {
            state = GameState.pause;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startPauseButton.setText(getResources().getText(R.string.start));
                }
            });
            steerings.removeAllElements();
            setNewDirection();
        }
    }


    public class SendSteeringTask extends AsyncTask<Void, Void, SnakeMessage> {

        @Override
        protected SnakeMessage doInBackground(Void... params) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//            Log.v(TAG, "doInBackground");
            Steering steering;
            if(!steerings.isEmpty())
            {
                steering = steerings.firstElement();
                steerings.remove(0);
            }
            else
            {
                steering = new Steering();
            }
            SnakeMessage snakeMessage = restTemplate.postForObject(mainActivity.restAddress, steering, SnakeMessage.class);
            return snakeMessage;
        }

        @Override
        protected void onPostExecute(SnakeMessage snakeMessage) {
            understandSnakeMessage(snakeMessage);
        }
    }

    public void understandSnakeMessage(SnakeMessage snakeMessage)
    {
        segments = snakeMessage.getSnake().transform();
        for (Snake enemy : snakeMessage.getEnemies()) {
            enemies = (Point[]) ArrayUtils.addAll(enemies, enemy.transform());
        }
        walls = snakeMessage.getWall();
        meals = snakeMessage.getMeal();


        if(snakeMessage.getNotification() == 2)//umarles
        {
            pauseGame();
            //jesli pauza, odliczaj
            if(state == GameState.pause) {
                state = GameState.counting;
                countDownThread = new CountDownThread();
                countDownThread.start();
            }
        }

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(state == GameState.counting)
                {
                    startPauseButton.setEnabled(false);
                }
                else
                {
                    startPauseButton.setEnabled(true);
                }
            }
        });
    }

    public class CountDownThread extends Thread
    {
        int deathCount;
        public void run() {
            deathCount = TIME_TO_RESP;
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
            state = GameState.working;
            pauseGame();
        }
    }

    Thread sendMessageThread = new Thread(new Runnable() {
        @Override
        public void run() {
            Log.v(TAG, "sendMessageThread run");
            while(true) {
                try {
                    Thread.sleep(deltaTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(state == GameState.working) {
                    new SendSteeringTask().execute();
                }
            }
        }
    });
}
