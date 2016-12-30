package com.example.user.snake.user_interface;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.example.user.snake.main.GameFragment;
import com.example.user.snake.communication.Answers.Point;

/**
 * Created by user on 21.11.2016.
 */
public class GameView extends SurfaceView implements Runnable {

    private int boardWidth;
    private Painter graphics;
    private Bitmap gameImage;
    private GameFragment gameFragment;
    private Context mContext;
    private InputHandler inputHandler;
    private Canvas gameCanvas;
    Rect gameImageSrc, gameImageDst;
    private Thread gameThread;

    public GameView(GameFragment gameFragment, Context context, Point boardSize) {
        super(context);
        this.mContext = context;
        this.gameFragment = gameFragment;
        createParameters(boardSize.getX(), boardSize.getY());

        gameImage = Bitmap.createBitmap(boardWidth, boardWidth, Bitmap.Config.RGB_565);
        gameImageSrc = new Rect(0, 0, gameImage.getWidth(),
                gameImage.getHeight());
        gameImageDst = new Rect();
        gameCanvas = new Canvas(gameImage);
        graphics = new Painter(gameCanvas, boardWidth, boardWidth);

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
            boardWidth = screenSize.x;
            Painter.textSize = x*4;
            Box.setMultiplier((int) Math.floor((double) boardWidth / x));
        } else {
            boardWidth = screenSize.y;
            Painter.textSize = y*4;
            Box.setMultiplier((int) Math.floor((double) boardWidth / y));
        }
    }

    public int getBoardWidth()
    {
        return boardWidth;
    }

    private void initInput() {
        if (inputHandler == null) {
            inputHandler = new InputHandler(gameFragment, boardWidth);
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


    private void render()
    {
        gameFragment.currentState.render(graphics);
        renderGameImage();
    }

    private void renderGameImage() {
        Canvas screen = getHolder().lockCanvas();
        if(screen != null) {
            screen.getClipBounds(gameImageDst);
            screen.drawBitmap(gameImage, gameImageSrc, gameImageDst, null);
            getHolder().unlockCanvasAndPost(screen);
        }
    }


    @Override
    public void run() {
        while(true) {
           if(gameFragment.currentState.news)
           {
               render();
               gameFragment.currentState.news = false;
           }
        }
    }
}
