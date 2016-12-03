package com.example.user.snake;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends Activity {

    protected User user;
    protected String restAddress;
    public static GameFragment gameFragment;
    private final static String TAG = "GameActivity";
    MidiPlayer midiPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v(TAG, "onCreate");

        gameFragment = (GameFragment)getFragmentManager().findFragmentById(R.id.mainFragment);

        Intent intent = getIntent();
        restAddress = intent.getStringExtra("ADDRESS");
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
