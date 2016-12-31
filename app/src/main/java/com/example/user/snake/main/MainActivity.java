package com.example.user.snake.main;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

import com.example.user.snake.R;
import com.example.user.snake.communication.Answers.User;
import com.example.user.snake.assets.Assets;


public class MainActivity extends Activity {

    protected User user;
    public GameFragment gameFragment;
    private final static String TAG = "GameActivity";
    public static AssetManager assets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assets = getAssets();
        Assets.load();
        Assets.playMusic(this);

        Log.v(TAG, "onCreate");

        gameFragment = (GameFragment)getFragmentManager().findFragmentById(R.id.mainFragment);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra(User.USER);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void startLoginActivity()
    {
        Assets.stopMusic();
        //TODO kiedys odkomentowac ale sprawdzic dlaczego nie dziala
//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);
        finish();
        System.exit(0);
    }
}
