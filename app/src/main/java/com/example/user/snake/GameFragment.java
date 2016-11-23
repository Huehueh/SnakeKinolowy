package com.example.user.snake;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by user on 08.11.2016.
 */
public class GameFragment extends Fragment{

    int TIME_TO_RESP;

    Point [] segments, meals, walls, laser;
    List<Point []> enemies;
    Point boardSize;
    boolean gameWorking = false;
    int deltaTime = 300; //ms default
    Stack<Steering> steerings;
    public MainActivity mainActivity;
    CountDownThread countDownThread;
    int deathCount = -1;
    boolean sendLaser = false;
    SendSteeringTask task = null;

    //layout
    LinearLayout boardView;
    Button startPauseButton;
    Button laserButton;
    Direction currentDirection = Direction.NO_DIRECTION;

    public static String TAG = "GameFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.game_layout, container, false);

        startPauseButton = (Button) rootView.findViewById(R.id.startPauseButton);
        startPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                steerings.removeAllElements();
                startPauseGame(!gameWorking);
            }
        });
        boardView = (LinearLayout)rootView.findViewById(R.id.board);
        laserButton = (Button) rootView.findViewById(R.id.laserButton);
        laserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLaser = true;
                Log.v("laser", "zmieniam na true");
            }
        });


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, "onActivityCreated");

        //User Info
        mainActivity = (MainActivity) getActivity();
        boardSize = mainActivity.user.getSize();
        deltaTime = (int)(1000/mainActivity.user.getFrameRate());
        //deltaTime = 2000;
        TIME_TO_RESP = mainActivity.user.getTime2resp();

        //sterowanie
        Steering.setId(mainActivity.user.getId());
        steerings = new Stack<>();

        if(segments == null)
        {
            segments = mainActivity.user.getPositions();
        }

        putBoard();
        startPauseGame(false);
        sendMessageThread.start();
    }

    public void putBoard()
    {
        BoardImage board = new BoardImage(this, getActivity(), boardSize);
        boardView.setLayoutParams(new LinearLayout.LayoutParams(BoardImage.width, BoardImage.width));
        boardView.addView(board);
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(board.getWindowToken(), 0);
    }

    @Override
    public void onPause() {
        super.onPause();
        startPauseGame(false);
    }

    public void setNewDirection(Direction dir)
    {
        if(checkIfOk(dir))
        {
            Log.v("laser", dir.toString() + " is OK");
            steerings.add(new Steering(dir, sendLaser));
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


    public void startPauseGame(boolean working)
    {
        gameWorking = working;
        if(!gameWorking)
        {
            steerings.removeAllElements();
            deathCount = -1;
        }
        setNewDirection();
        setButton();
    }

    private void setButton()
    {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(gameWorking) {
                    startPauseButton.setText(mainActivity.getResources().getText(R.string.pause));
                }
                else {
                    startPauseButton.setText(mainActivity.getResources().getText(R.string.start));
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(task != null)
        {
            task.cancel(true);
            task = null;
        }
        if(sendMessageThread != null)
        {
            try {
                sendMessageThread.join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    public class SendSteeringTask extends AsyncTask<Void, Void, SnakeMessage> {

        @Override
        protected SnakeMessage doInBackground(Void... params) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            Steering steering;
            if(!steerings.isEmpty())
            {
                Log.v("laser", "stos sterowan " + steerings.size()+ " " + steerings.get(0).getDir().toString());
                steering = steerings.firstElement();
                steerings.remove(0);
            }
            else
            {
                steering = new Steering(sendLaser);
            }
            if(steering.isLaser())
                Log.v("laser", "steering ma laser");

            currentDirection = steering.getDir();

            Log.v("NOTYFIKACJA", "wysylam " + steering.getDir().toString());
            SnakeMessage snakeMessage = restTemplate.postForObject(mainActivity.restAddress, steering, SnakeMessage.class);
            if(sendLaser)
            {
                sendLaser = false;
                Log.v("laser", "zmieniam na false");
            }
            return snakeMessage;
        }

        @Override
        protected void onPostExecute(SnakeMessage snakeMessage) {
            understandSnakeMessage(snakeMessage);
            task = null;
        }
    }

    public void understandSnakeMessage(SnakeMessage snakeMessage)
    {
        segments = snakeMessage.getSnake().transformToArray(boardSize);
        enemies = new ArrayList<>();
        for (int i = 0; i< snakeMessage.getEnemies().length; i++) {
            enemies.add(snakeMessage.getEnemies()[i].transformToArray(boardSize));
        }
        walls = snakeMessage.getWall();
        meals = snakeMessage.getMeal();

        switch(snakeMessage.getSnakeNotification())
        {
            case NIC:
                break;
            case OZYLES:
                startPauseGame(true);
                startPauseButton.setEnabled(true);
                break;
            case UMARLES:
                startPauseGame(false);
                countDownThread = new CountDownThread();
                countDownThread.start();
                startPauseButton.setEnabled(false);
                break;
            case ZJADLES:
                break;
        }

        laser = snakeMessage.getSnake().getLaserArray(boardSize);
        for (int i = 0; i< snakeMessage.getEnemies().length; i++) {
            laser = ArrayUtils.addAll(laser, snakeMessage.getEnemies()[i].getLaserArray(boardSize));
        }
    }

    public class CountDownThread extends Thread
    {
        public void run() {
            deathCount = TIME_TO_RESP + 1;
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
        }
    }

    Thread sendMessageThread = new Thread(new Runnable() {
        @Override
        public void run() {
            Log.v(TAG, "sendMessageThread run blavo;ihgff");
            while(true) {
                try {
                    Thread.sleep(deltaTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(task != null){
                    task.cancel(true);
                }
                task =  new SendSteeringTask();
                task.execute();
            }
        }
    });
}
