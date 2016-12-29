package com.example.user.snake.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import com.example.user.snake.R;
import com.example.user.snake.main.MainActivity;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by user on 29.12.2016.
 */
public class Assets {

    public static MediaPlayer mediaPlayer;
    public static SoundPool soundPool;
    public static Bitmap head, segment, wall, meal, laser, head_enemy, segment_enemy;
    public static int laserId, eatId, deathId;

    private final static String TAG = "Assets";

    public static void playMusic(Context context)
    {
        if(mediaPlayer == null)
        {
            mediaPlayer = MediaPlayer.create(context, R.raw.wonsz);
            mediaPlayer.setLooping(true);
        }
        mediaPlayer.start();
    }

    public static void stopMusic()
    {
        if(mediaPlayer != null)
        {
            mediaPlayer.stop();
        }
    }

    //GRAPHICS AND SOUNDS

    public static void load()
    {
        head = loadBitmap("head.png");
        wall = loadBitmap("wall.png");
        segment = loadBitmap("segment.png");
        meal = loadBitmap("meal.png");
        laser = loadBitmap("laser.png");
        head_enemy = loadBitmap("head_enemy.png");
        segment_enemy = loadBitmap("segment_enemy.png");

        laserId = loadSound("hit.wav");
        eatId = loadSound("eat.wav");
        deathId = loadSound("cat.wav");

        Log.v(TAG, "Assets loaded");
    }

    private static Bitmap loadBitmap(String filename)
    {
        InputStream inputStream = null;
        try {
            inputStream = MainActivity.assets.open(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        return bitmap;
    }

    private static int loadSound(String filename) {
        int soundID = 0;
        if (soundPool == null) {
            soundPool = new SoundPool(25, AudioManager.STREAM_MUSIC, 0);
        }
        try {
            soundID = soundPool.load(MainActivity.assets.openFd(filename),
                    1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return soundID;
    }

    public static void playSound(int soundId)
    {
        soundPool.play(soundId, 1, 1, 1, 0, 1);
    }
}
