package com.example.user.snake;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;

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
    ArrayList<Point> segments;
    Point[] enemies = null;
    Point meal;
    GameState state = GameState.pause;

    int sizeX, sizeY;
    int deltaTime = 300; //ms default

    BoardImage board = null;
    Stack<Steering> steerings;
    Button startPauseButton;
    public MainActivity mainActivity;
    CountDownThread countDownThread;
    LinearLayout boardView;

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

        public Steering (int newDirection)
        {
            direction = newDirection;
            defaultDirection = newDirection;
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Log.v(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.game_layout, container, false);

        startPauseButton = (Button) rootView.findViewById(R.id.startPauseButton);
        startPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state == GameState.working) {
                    pauseGame();
                    updateView();
                }
                else {
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
        sizeX = mainActivity.user.getSize().getX();
        sizeY = mainActivity.user.getSize().getY();
        deltaTime = (int)(1000/mainActivity.user.getFrameRate());
        TIME_TO_RESP = mainActivity.user.getTime2resp();

        //sterowanie
        Steering.id = mainActivity.user.getId();
        steerings = new Stack<>();

        if(savedInstanceState != null)
        {
            segments = (ArrayList<Point>)savedInstanceState.getSerializable(BundleNames.SEGMENTS);
            meal = (Point)savedInstanceState.getSerializable(BundleNames.MEAL);
            enemies = (Point[])savedInstanceState.getSerializable(BundleNames.ENEMIES);
            state = GameState.pause;
        }

        if(segments == null)
        {
            segments = new ArrayList<>();
            for (int k = mainActivity.user.getPositions().length - 1; k >=0; k--) {
                segments.add(mainActivity.user.getPositions()[k]);
            }
        }

        putBoard();

        sendMessageThread.start();
    }

    public void putBoard()
    {
        board = BoardImage.newInstance(mainActivity, sizeX, sizeY);
        board.gameFragment = this;
        updateView();
        boardView.addView(board);
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(board.getWindowToken(), 0);
    }

    private class BundleNames
    {
        private final static String SEGMENTS = "SEGMENTS";
        private final static String MEAL = "MEAL";
        private final static String ENEMIES = "ENEMIES";
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BundleNames.SEGMENTS, segments);
        outState.putSerializable(BundleNames.MEAL, meal);
        outState.putSerializable(BundleNames.ENEMIES, enemies);
    }


    @Override
    public void onPause() {
        super.onPause();
        pauseGame();
    }

    public void setNewDirection(int dir)
    {
        if(!checkIfOpposite(dir))
        {
            steerings.add(new Steering(dir));
        }
    }

    public void setNewDirection()
    {
        setNewDirection(getResources().getInteger(R.integer.no_direction));
    }

    private boolean checkIfOpposite(int newDirection)
    {
        if(steerings.size() == 0)
        {
            return false;
        }
        else {
            int oldDirection = steerings.lastElement().getDirection();
            if ((oldDirection == getResources().getInteger(R.integer.left) && newDirection == getResources().getInteger(R.integer.right))
                    || (oldDirection == getResources().getInteger(R.integer.right) && newDirection == getResources().getInteger(R.integer.left))
                    || (oldDirection == getResources().getInteger(R.integer.up) && newDirection == getResources().getInteger(R.integer.down))
                    || (oldDirection == getResources().getInteger(R.integer.down) && newDirection == getResources().getInteger(R.integer.up))) {
                return true;
            } else {
                return false;
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

    public void updateView()
    {
        board.update(segments, enemies, meal);
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
        switch (snakeMessage.getNotification())
        {
            case 0:
                if (!(snakeMessage.getNewPos().getX() == segments.get(segments.size()-1).getX()
                        && snakeMessage.getNewPos().getY() == segments.get(segments.size()-1).getY()))
                {
                    segments.remove(0);
                    segments.add(snakeMessage.getNewPos());
                }
                break;
            case 1:
                //zjadlem
                segments.add(snakeMessage.getNewPos());
                break;
            case 2:
                //umieram
                //jesli pracuje, zapauzuj
                pauseGame();
                //jesli pauza, odliczaj
                if(state == GameState.pause) {
                    state = GameState.counting;
                    countDownThread = new CountDownThread();
                    countDownThread.start();
                }
                break;

        }
        meal = snakeMessage.getMeal();
        enemies = snakeMessage.getEnemiesPositions();
        updateView();
    }

    public class CountDownThread extends Thread
    {
        int deathCount;
        public void run() {
            deathCount = TIME_TO_RESP;
            while(deathCount > 0)
            {
                deathCount--;
                updateView();
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
            updateView();
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
