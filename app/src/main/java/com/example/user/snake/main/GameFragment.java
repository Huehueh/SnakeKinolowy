package com.example.user.snake.main;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.user.snake.R;
import com.example.user.snake.ResultsDialogFragment;
import com.example.user.snake.communication.Answers.Score;
import com.example.user.snake.communication.Answers.ScoreMessage;
import com.example.user.snake.communication.Answers.SendingTask;
import com.example.user.snake.communication.Direction;
import com.example.user.snake.communication.Answers.Point;
import com.example.user.snake.communication.Queries.LogOut;
import com.example.user.snake.communication.Answers.SnakeMessage;
import com.example.user.snake.communication.Queries.Results;
import com.example.user.snake.communication.Queries.Steering;
import com.example.user.snake.communication.Answers.User;
import com.example.user.snake.graphics.Assets;
import com.example.user.snake.graphics.GameView;
import com.example.user.snake.states.DeathState;
import com.example.user.snake.states.EndGameState;
import com.example.user.snake.states.GameState;
import com.example.user.snake.states.PauseState;
import com.example.user.snake.states.PlayState;
import com.example.user.snake.states.WaitingState;

import org.springframework.web.client.RestClientException;

/**
 * Created by user on 08.11.2016.
 */
public class GameFragment extends Fragment implements View.OnClickListener{

    public int TIME_TO_RESP;
    private Point boardSize;
    public int deltaTime = 300; //ms default

    public MainActivity mainActivity;
    SendingTask task = null;

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
        rootView.findViewById(R.id.showResultsButton).setOnClickListener(this);

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
                Assets.playSound(Assets.laserId);
                break;
            case R.id.exitButton:
                setCurrentState(new PauseState(this));
                showLogOutDialog();
                break;
            case R.id.showResultsButton:
                askForResults();
                break;
        }
    }

    public void askForResults()
    {
        task = new AskForResultsTask();
        task.execute();
    }

    private void showLogOutDialog()
    {
        new AlertDialog.Builder(mainActivity)
                .setTitle(getText(R.string.exit_title))
                .setMessage(getText(R.string.exit_question))
                .setCancelable(true)
                .setPositiveButton(getText(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        endGame();
                        logOut();
                        mainActivity.finish();
                    }
                })
                .setNegativeButton(getText(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        onExitDialog();
                    }
                })
                .show();
    }

    public void onExitDialog()
    {
        if(currentState.getName() != GameState.StateName.endGame) {
            running = true;
            setCurrentState(new PlayState(this));
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

    public class LogOutTask extends SendingTask<Void>
    {
        LogOut logOut;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                if(message != null)
                    Log.v("SCORE", message.toString());
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
        if(message!= null) {
            if(currentState.getName() != GameState.StateName.endGame) {
                setCurrentState(new PauseState(this));
            }
            for (Score score : message.getScores()) {
                Log.v("SCORES", score.getName() + " " + score.getPoints() + " " + score.getDeaths());
            }
            ResultsDialogFragment fragment = ResultsDialogFragment.newInstance(message, this);
            fragment.show(getFragmentManager(), "dialog");
        }
    }

    public class SendSteeringTask extends SendingTask<SnakeMessage> {

        Steering steering;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            steering = currentState.getSteering();
        }

        @Override
        protected SnakeMessage doInBackground(Object... params) {
            SnakeMessage message = null;
            try{
            message = restTemplate.postForObject(steering.getQuery(), steering, SnakeMessage.class);
            }
            catch(RestClientException e)
            {
                e.printStackTrace();
            }
            return message;

        }

        @Override
        protected void onPostExecute(SnakeMessage snakeMessage) {
            understandSnakeMessage(snakeMessage);
            task = null;
        }
    }

    public void understandSnakeMessage(SnakeMessage snakeMessage)
    {
        if(snakeMessage != null) {
            switch (snakeMessage.getSnakeNotification()) {
                case OZYLES:
                    setCurrentState(new PlayState(this));
                    break;
                case UMARLES:
                    setCurrentState(new DeathState(this));
                    break;
                case ZJADLES:
                    Assets.playSound(Assets.eatId);
                    break;
                case KONIEC_GRY:
                    setCurrentState(new EndGameState(this));
                    break;
                case ZA_MALO_GRACZY:
                    setCurrentState(new WaitingState(this));
                    break;
            }
            currentState.setStates(snakeMessage);
        }
    }


}
