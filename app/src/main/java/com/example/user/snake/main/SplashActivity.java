package com.example.user.snake.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by user on 30.12.2016.
 */
public class SplashActivity extends AppCompatActivity {

    boolean entered = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("SPLASH", "onCreate");
        new Thread(new MyRunnable()).start();
    }


    public void startLoginActivity()
    {
        if(!entered) {
            Intent intent = new Intent(this, LoginActivity.class);
            entered = true;
            startActivity(intent);
            this.finish();
        }
    }

    @Override
    public void onUserInteraction() {
        startLoginActivity();
    }

    private class MyRunnable implements Runnable
    {
        @Override
        public void run() {
            try {
                Thread.sleep(3000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            finally {
                startLoginActivity();
            }
        }
    }
}
