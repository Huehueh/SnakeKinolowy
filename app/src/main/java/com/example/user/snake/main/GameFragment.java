package com.example.user.snake.main;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.snake.R;
import com.example.user.snake.communication.Answers.Score;
import com.example.user.snake.communication.Answers.ScoreMessage;
import com.example.user.snake.communication.Answers.SendingTask;
import com.example.user.snake.communication.Answers.Point;
import com.example.user.snake.communication.Queries.JsonMessage;
import com.example.user.snake.communication.Queries.LogOut;
import com.example.user.snake.communication.Answers.SnakeMessage;
import com.example.user.snake.communication.Queries.Reset;
import com.example.user.snake.communication.Queries.Results;
import com.example.user.snake.communication.Queries.Steering;
import com.example.user.snake.communication.Answers.User;
import com.example.user.snake.assets.Assets;
import com.example.user.snake.user_interface.GameView;
import com.example.user.snake.states.GameState;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import java.util.Calendar;

/**
 * Created by user on 08.11.2016.
 */
public class GameFragment extends Fragment implements View.OnClickListener{

    public int TIME_TO_RESP;
    public int deltaTime = 300; //ms default

    public MainActivity mainActivity;
    SendingTask task = null;
    public GameState currentState = null;
    public boolean running;
    public GameView gameView;

    //layout
    LinearLayout boardView;
    Button resetButton, pingButton;
    TextView pointsTextView;

    public static String TAG = "GameFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.game_layout, container, false);

        boardView = (LinearLayout)rootView.findViewById(R.id.board);

        rootView.findViewById(R.id.laserButton).setOnClickListener(this);
        rootView.findViewById(R.id.exitButton).setOnClickListener(this);
        rootView.findViewById(R.id.showResultsButton).setOnClickListener(this);

        resetButton = (Button) rootView.findViewById(R.id.resetButton);
        resetButton.setOnClickListener(this);
        pointsTextView = (TextView) rootView.findViewById(R.id.pointsTextView);
        pingButton = (Button) rootView.findViewById(R.id.pingButton);
        pingButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        startPlaying();
        //User Info
        mainActivity = (MainActivity) getActivity();
        initializeData(mainActivity.user);

        sendMessageThread.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        gameView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        gameView.pause();
    }

    private void initializeData(User user)
    {
        currentState.setInitialState(mainActivity.user);
        deltaTime = (int)(1000/user.getFrameRate());
        TIME_TO_RESP = user.getTime2resp();
        String login = user.getLogin();
        if(login.substring(0, Math.min(login.length(), 5)).equals(getString(R.string.admin_name)))
        {
            resetButton.setVisibility(View.VISIBLE);
        }
        putBoard(user.getSize());
    }

    public void startPlaying()
    {
        setCurrentState(GameState.StateName.play);
    }

    public void setCurrentState(GameState.StateName stateName)
    {
        if(currentState != null) {

            if(currentState.getName() != stateName && currentState.getName() != GameState.StateName.endGame)
            {
                currentState = GameState.newInstance(this, stateName);
                currentState.init();
            }
        }
        else
        {
            currentState = GameState.newInstance(this, stateName);
            currentState.init();
        }
    }

    public void putBoard(Point boardSize)
    {
        gameView = new GameView(this, getActivity(), boardSize);
        boardView.setLayoutParams(new RelativeLayout.LayoutParams(gameView.getBoardWidth(), gameView.getBoardWidth()));
        boardView.addView(gameView);
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(gameView.getWindowToken(), 0);
    }

    //KOMUNIKACJA

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.laserButton:
                currentState.sendLaser();
                break;
            case R.id.exitButton:
                showLogOutDialog();
                break;
            case R.id.showResultsButton:
                askForResults();
                break;
            case R.id.resetButton:
                task = new ResetTask();
                task.execute();
                break;
            case R.id.pingButton:
                Log.v("PING", "klikam");

                break;
        }
    }

    //RESET PLANSZY

    public class ResetTask extends SendingTask<Void>
    {
        Reset reset;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            running = false;
            reset = new Reset();
        }

        @Override
        protected Void doInBackground(Object... params) {
            try
            {
                restTemplate.delete(reset.getQuery(), reset);
            }
            catch (ResourceAccessException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mainActivity.startLoginActivity();
        }
    }

    //WYLOGOWANIE

    private void showLogOutDialog()
    {
        setCurrentState(GameState.StateName.pause);
        new AlertDialog.Builder(mainActivity)
                .setTitle(getText(R.string.exit_title))
                .setMessage(getText(R.string.exit_question))
                .setCancelable(true)
                .setPositiveButton(getText(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logOutAndClose();
                    }
                })
                .setNegativeButton(getText(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        setCurrentState(GameState.StateName.play);
                    }
                })
                .show();
    }

    public void logOutAndClose()
    {
        task = new LogOutTask();
        task.execute();
    }

    public class LogOutTask extends SendingTask<Void>
    {
        LogOut logOut;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            running = false;
            logOut = new LogOut();
        }

        @Override
        protected Void doInBackground(Object... params) {
            try{
                restTemplate.delete(logOut.getQuery(), logOut);
            }
            catch(RestClientException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mainActivity.startLoginActivity();
        }
    }

    //PROSBA O PUNKTY

    public void askForResults()
    {
        task = new AskForResultsTask();
        task.execute();
    }

    public class AskForResultsTask extends SendingTask<ScoreMessage>
    {
        Results results;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            results = new Results();
        }

        @Override
        protected ScoreMessage doInBackground(Object... params) {
            ScoreMessage message = null;
            try {
                message = restTemplate.getForObject(results.getQuery(), ScoreMessage.class);
            }
            catch(RestClientException e)
            {
                e.printStackTrace();
            }
            return message;
        }

        @Override
        protected void onPostExecute(ScoreMessage scoreMessage) {
            super.onPostExecute(scoreMessage);
            understandScoreMessage(scoreMessage);
        }
    }

    public void understandScoreMessage(ScoreMessage message)
    {
        setCurrentState(GameState.StateName.pause);
        if(message!= null) {
            for (Score score : message.getScores()) {
                Log.v("SCORES", score.getName() + " " + score.getPoints() + " " + score.getDeaths());
            }
            ResultsDialogFragment fragment = ResultsDialogFragment.newInstance(message, this);
            fragment.show(getFragmentManager(), "dialog");
        }
    }

    //STEROWANIE

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

    public class SendSteeringTask extends SendingTask<SnakeMessage> {

        Steering steering;
        long startTime, endTime;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            steering = currentState.getSteering();
        }

        @Override
        protected SnakeMessage doInBackground(Object... params) {
            SnakeMessage message = null;
            try{
                if(running) {
                    startTime = Calendar.getInstance().getTimeInMillis();
                    message = restTemplate.postForObject(steering.getQuery(), steering, SnakeMessage.class);

                }
            }
            catch(ResourceAccessException e)
            {
                e.printStackTrace();
            }
            catch(HttpClientErrorException e)
            {
                e.printStackTrace();
                mainActivity.startLoginActivity();
            }
            return message;
        }

        @Override
        protected void onPostExecute(SnakeMessage snakeMessage) {
            endTime = Calendar.getInstance().getTimeInMillis();
            long diffInMs = endTime - startTime;
            if (diffInMs>180)
                Log.v("PING", diffInMs + " ms");
            understandSnakeMessage(snakeMessage);
            task = null;
        }
    }

    public void understandSnakeMessage(final SnakeMessage snakeMessage)
    {
        if(snakeMessage != null) {
            switch (snakeMessage.getSnakeNotification()) {
                case OZYLES:
                    if(currentState.getName() == GameState.StateName.waiting)
                    {
                        setCurrentState(GameState.StateName.play);
                    }
                    break;
                case UMARLES:
                    setCurrentState(GameState.StateName.death);
                    break;
                case ZJADLES:
                    Assets.playSound(Assets.eatId);
                    break;
                case KONIEC_GRY:
                    setCurrentState(GameState.StateName.endGame);
                    break;
                case ZA_MALO_GRACZY:
                    setCurrentState(GameState.StateName.waiting);
                    break;
            }

            currentState.setBoard(snakeMessage);

            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pointsTextView.setText(getText(R.string.your_result) + " " + snakeMessage.getPoints());
                }
            });

        }
    }


}
