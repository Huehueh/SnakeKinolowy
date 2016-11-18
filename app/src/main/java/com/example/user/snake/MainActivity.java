package com.example.user.snake;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends Activity {

    protected User user;
    protected String restAddress;
    GameFragment gameFragment;
    private final static String TAG = "GameActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v(TAG, "onCreate");

        gameFragment = (GameFragment)getFragmentManager().findFragmentById(R.id.mainFragment);

        Intent intent = getIntent();
        restAddress = intent.getStringExtra("ADDRESS");
        user = (User) intent.getSerializableExtra(User.USER);
    }
}
