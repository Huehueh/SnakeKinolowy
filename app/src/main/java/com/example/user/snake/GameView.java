package com.example.user.snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

/**
 * Created by user on 21.11.2016.
 */
public class GameView extends SurfaceView implements Runnable {

    int width;
    Painter painter;
    Bitmap gameImage;
    GameFragment gameFragment;
    Context mContext;
    private InputHandler inputHandler;
    private Canvas gameCanvas;
    private Thread gameThread;

    public GameView(GameFragment gameFragment, Context context, Point boardSize) {
        super(context);
        this.mContext = context;
        this.gameFragment = gameFragment;
        createParameters(boardSize.getX(), boardSize.getY());

        gameImage = Bitmap.createBitmap(boardSize.getX(), boardSize.getY(), Bitmap.Config.RGB_565);
        gameCanvas = new Canvas(gameImage);

        SurfaceHolder holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                initInput();
                initGame();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                pauseGame();
            }
        });


    }

    private void createParameters(int x, int y) {
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        android.graphics.Point screenSize = new android.graphics.Point();
        display.getSize(screenSize);
        if (screenSize.x < screenSize.y) {
            width = screenSize.x;
            Box.mnoznik = (int) Math.floor((double) width / x);
        } else {
            width = screenSize.y;
            Box.mnoznik = (int) Math.floor((double) width / y);
        }
    }

    private void initInput() {
        if (inputHandler == null) {
            inputHandler = new InputHandler(width);
        }
        setOnTouchListener(inputHandler);
    }

    private void initGame() {
        gameThread = new Thread(this, "Game Thread");
        gameThread.start();
    }

    private void pauseGame() {
        while (gameThread.isAlive()) {
            try {
                gameThread.join();
                break;
            } catch (InterruptedException e) {
            }
        }
    }

    private String getDisplayText() {
        if (gameFragment.gameWorking) {
            return "";
        } else {
            if (gameFragment.deathCount == -1) {
                return getResources().getText(R.string.click_start).toString();
            } else {
                return gameFragment.deathCount + "";
            }

        }
    }

    private void renderGameImage()
    {
        Canvas screen = getHolder().lockCanvas();
        if(screen != null)
        {

        }
    }

    @Override
    public void run() {

    }
}
