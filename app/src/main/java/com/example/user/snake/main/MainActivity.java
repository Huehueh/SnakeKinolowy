package com.example.user.snake.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.user.snake.R;
import com.example.user.snake.communication.Answers.User;
import com.example.user.snake.graphics.MidiPlayer;


public class MainActivity extends Activity {

    protected User user;
    public GameFragment gameFragment;
    private final static String TAG = "GameActivity";
    MidiPlayer midiPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v(TAG, "onCreate");

        gameFragment = (GameFragment)getFragmentManager().findFragmentById(R.id.mainFragment);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra(User.USER);

        midiPlayer = new MidiPlayer(this);
        midiPlayer.playMusic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(midiPlayer != null)
        {
            midiPlayer.stopMusic();
        }
    }
}
