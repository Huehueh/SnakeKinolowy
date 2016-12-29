package com.example.user.snake.graphics;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.user.snake.R;

/**
 * Created by user on 27.11.2016.
 */
public class MidiPlayer {

    Context context;
    MediaPlayer mediaPlayer;

    public MidiPlayer(Context context)
    {
        this.context = context;
    }


    public void playMusic()
    {
        mediaPlayer = MediaPlayer.create(context, R.raw.wonsz);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    public void stopMusic()
    {
        if(mediaPlayer != null)
        {
            mediaPlayer.stop();
        }
    }



}