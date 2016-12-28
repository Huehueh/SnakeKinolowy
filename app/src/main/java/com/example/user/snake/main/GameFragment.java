package com.example.user.snake.main;

import android.app.Fragment;
import android.app.VoiceInteractor;
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
import android.widget.RelativeLayout;

import com.example.user.snake.R;
import com.example.user.snake.communication.Direction;
import com.example.user.snake.communication.Point;
import com.example.user.snake.communication.Queries.LogOut;
import com.example.user.snake.communication.SnakeMessage;
import com.example.user.snake.communication.Queries.Steering;
import com.example.user.snake.communication.User;
import com.example.user.snake.graphics.GameView;
import com.example.user.snake.states.DeathState;
import com.example.user.snake.states.GameState;
import com.example.user.snake.states.PlayState;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by user on 08.11.2016.
 */
public class GameFragment extends Fragment implements View.OnClickListener{

    public int TIME_TO_RESP;
    private Point boardSize;
    public int deltaTime = 300; //ms default

    public MainActivity mainActivity;
    AsyncTask task = null;

    public GameState currentState;
    public boolean running = true;

    //layout
    LinearLayout boardView;

    public static String TAG = "GameFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.game_layout, container, false);

        boardView = (LinearLayout)rootView.findViewById(R.id.board);

        rootView.findViewById(R.id.laserButton).setOnClickListener(this);
        rootView.findViewById(R.id.exitButton).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setCurrentState(new PlayState(this));
        //User Info
        mainActivity = (MainActivity) getActivity();
        getInfoFromUser(mainActivity.user);
        putBoard();
        sendMessageThread.start();
    }

    private void getInfoFromUser(User user)
    {
        boardSize = user.getSize();
        deltaTime = (int)(1000/user.getFrameRate());
        TIME_TO_RESP = user.getTime2resp();
        currentState.setInitialState(mainActivity.user);
    }

    public void setCurrentState(GameState newState)
    {
        System.gc();
        if(currentState != null) {
            if (currentState.getName() != newState.getName()) {
                currentState = newState;
                currentState.init();
            }
        }
        else
        {
            currentState = newState;
            currentState.init();
        }
        Log.v(TAG, currentState.getName().toString());
    }

    public void putBoard()
    {
        GameView board = new GameView(this, getActivity(), boardSize);
        boardView.setLayoutParams(new RelativeLayout.LayoutParams(board.getBoardWidth(), board.getBoardWidth()));
        boardView.addView(board);
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(board.getWindowToken(), 0);
    }

    //USTAWIANIE KIERUNKU

    public void setNewDirection(Direction dir)
    {
        currentState.addSteering(dir);
    }

    public void setNewDirection()
    {
        setNewDirection(Direction.NO_DIRECTION);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.laserButton:
                currentState.sendLaser();
                break;
            case R.id.exitButton:
                endGame();
                logOut();
                mainActivity.finish();
                break;
        }
    }

    public void endGame()
    {
        running = false;
        if(task != null)
        {
            task.cancel(true);
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

    public void logOut()
    {
        task = new LogOutTask();
        task.execute();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        endGame();
    }

    //KOMUNIKACJA

    Thread sendMessageThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while(running) {
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

    public class LogOutTask extends AsyncTask<Object, Void, Void>
    {
        RestTemplate restTemplate;
        LogOut logOut;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            logOut = new LogOut();
        }

        @Override
        protected Void doInBackground(Object... params) {
            restTemplate.delete(logOut.getQuery(), logOut);
            return null;
        }
    }

    public class SendSteeringTask extends AsyncTask<Object, Void, SnakeMessage> {

        RestTemplate restTemplate;
        Steering steering;

        public SendSteeringTask()
        {
            restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            steering = currentState.getSteering();
        }

        @Override
        protected SnakeMessage doInBackground(Object... params) {
            return restTemplate.postForObject(steering.getQuery(), steering, SnakeMessage.class);

        }

        @Override
        protected void onPostExecute(SnakeMessage snakeMessage) {
            understandSnakeMessage(snakeMessage);
            task = null;
        }
    }

    public void understandSnakeMessage(SnakeMessage snakeMessage)
    {
        //STANY
        switch(snakeMessage.getSnakeNotification())
        {
            case NIC:
                break;
            case OZYLES:
                setCurrentState(new PlayState(this));
                break;
            case UMARLES:
                setCurrentState(new DeathState(this));
                break;
            case ZJADLES:

                break;
            case KONIEC_GRY:
                endGame();
                break;
        }
        currentState.setStates(snakeMessage);
    }


}
