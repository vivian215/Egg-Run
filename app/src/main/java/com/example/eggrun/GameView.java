package com.example.eggrun;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;

public class GameView extends SurfaceView implements Runnable {
    private boolean isPlaying;
    private Background bg1, bg2;
    private Paint paint, halfPaint;
    private final int BG_SPEED, BG_WIDTH;
    private Thread thread;
    private final int DELTA_T;
    private Player player;
    private int timerCount;
    private double initSpeed;
    private int timeIntervalNum;
    private int screenX, screenY;
    private ArrayList<GameItem> gameItems;
    private int phoneX;
    private boolean gameOver;
    private Bitmap youwin;
    private Bitmap blackBg;
    private Bitmap gameOverText;
    private int gameOverWidth, gameOverHeight;
    private int currPlayerX;
    private boolean firstTime;
    private boolean isWin;
    private MediaPlayer bgmusic, victorymusic, gameoversound, birdsound;

    public GameView (Context context, int screenX, int screenY, final Resources res) {
        super(context);

        isPlaying = true;

        bg1 = new Background(res);
        bg2 = new Background(res);

        paint = new Paint();
        halfPaint = new Paint();
        halfPaint.setAlpha(150);

        BG_SPEED = 15;
        BG_WIDTH = 3960;
        bg2.setX(BG_WIDTH);

        DELTA_T = 17;

        player = new Player(res);
        currPlayerX = player.getX();

        initSpeed = 50.0/DELTA_T;

        timeIntervalNum = 0;

        gameItems = new ArrayList<GameItem>() {
            {
                add(new Spike(res, 1500));
                for (int i = 0; i < 2; i++) {
                    add(new Spike(res, i * Spike.getWidth() + 2500));
                }
                add(new Nest(res, 3000));
                add(new Spike(res, 4000, 300));
                add(new Spike(res, 4500, 600));
                add(new Spike(res, 5000, 500));
                add(new Spike(res, 5600, 300));
                add(new Tree(res, 7000));
            }
        };

        phoneX = 0;

        gameOver = false;

        this.screenX = screenX;
        this.screenY = screenY;

        youwin = BitmapFactory.decodeResource(res, R.drawable.youwin);
        blackBg = BitmapFactory.decodeResource(res, R.drawable.black);
        gameOverText = BitmapFactory.decodeResource(res, R.drawable.gameover);
        gameOverWidth = 750;
        gameOverHeight = 393;
        gameOverText = Bitmap.createScaledBitmap(gameOverText, gameOverWidth, gameOverHeight, false);
        firstTime = true;

        isWin = false;

        bgmusic = MediaPlayer.create(context, R.raw.bgmusic);
        bgmusic.setVolume((float) 0.1, (float) 0.1);
        bgmusic.setLooping(true);
        victorymusic = MediaPlayer.create(context, R.raw.victorymusic);
        victorymusic.setVolume((float) .2, (float) .2);
        gameoversound = MediaPlayer.create(context, R.raw.gameoversound);
        gameoversound.setVolume((float) .5, (float) .5);
        birdsound = MediaPlayer.create(context, R.raw.birdsound);
        birdsound.setVolume((float) .2, (float) .2);
    }

    @Override
    public void run() {
        bgmusic.start();
        while (isPlaying) {
            update();
            draw();
            sleep();
        }
    }

    private void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();

            canvas.drawBitmap(bg1.getBackground(), bg1.getX(), bg1.getY(), paint);
            canvas.drawBitmap(bg2.getBackground(), bg2.getX(), bg2.getY(), paint);
            player.draw(canvas, paint);
            for (int i = 0; i < gameItems.size(); i++) {
                GameItem item = gameItems.get(i);
                if (!item.getCollected()) {
                    item.draw(canvas, paint, phoneX);
                } else {
                    if (item.getType().equals("nest")) {
                        player.setIsEgg(false);
                    }
                }
            }
            if (gameOver) {
                gameOver(canvas);
            }
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void update() {
        phoneX += BG_SPEED;
        timerCount++;
        timeIntervalNum++;
        currPlayerX += BG_SPEED;

        // scroll bg
        bg1.setX(bg1.getX()-BG_SPEED);
        bg2.setX(bg2.getX()-BG_SPEED);

        // lets the 2 background take turns
        if (bg1.getX() + bg1.getBackground().getWidth() < 0) {
            bg1.setX(BG_WIDTH);
        }
        if (bg2.getX() + bg2.getBackground().getWidth() < 0) {
            bg2.setX(BG_WIDTH);
        }
        player.update(timerCount);

        //checks if player goes out of screen
        if (player.getY() > 1080) {
            player.setY(1080);
        }
        if (player.getY() < 0) {
            player.setY(0);
        }

        //handles player jumping
        if (player.getIsJumping()) {
            initSpeed = 50.0/DELTA_T;
            if (!player.isEgg() && player.isTransition()) {
                initSpeed = 0.5;
            } else if (!player.isEgg()) {
                initSpeed = -1.8;
            }
            player.setY(player.getY() - (int) (initSpeed * DELTA_T + .5 * player.getAccel() * DELTA_T * (2 * timeIntervalNum * DELTA_T + DELTA_T)));
        }

        //handles player landing
        if (player.getY() >= 820) {
            if (player.isEgg()) {
                player.setY(820);
                player.setIsJumping(false);
                timeIntervalNum = 0;
            }
        }

        // checks if player collides with gameitems
        for (int i = 0; i < gameItems.size(); i++) {
            GameItem item = gameItems.get(i);
            item.update();

            if (Rect.intersects(player.getRect(currPlayerX), item.getRect())) {
                item.setCollected(true);
                if (item.getType().equals("spike")) {
                    gameOver = true;
                } else if (item.getType().equals("nest") && firstTime) {
                    player.setIsTransition(true);
                    birdsound.start();
                    firstTime = false;
                }
                if (item.getType().equals("tree")) {
                    isWin = true;
                    gameOver = true;
                }
            }
        }
    }

    private void sleep() {
        try {
            Thread.sleep(DELTA_T);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume () {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause () {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }

    public void gameOver(Canvas canvas) {
        bgmusic.stop();
        isPlaying = false;
        blackBg = Bitmap.createScaledBitmap(blackBg, 2500, 1500, false);
        canvas.drawBitmap(blackBg, -10, -10, halfPaint);

        if (isWin) {
            youwin = Bitmap.createScaledBitmap(youwin, 1553, 300, false);
            canvas.drawBitmap(youwin, 200, 300, paint);
            victorymusic.start();
        } else {
            canvas.drawBitmap(gameOverText, screenX / 2 - gameOverWidth / 2, screenY / 2 - gameOverHeight / 2, paint);
            gameoversound.start();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (gameOver) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    getActivity().startActivity(intent);
                    return false;
                } else if (!player.getIsJumping()) {
                    player.setIsJumping(true);
                    timeIntervalNum = 0;
                    MediaPlayer jumpSound = MediaPlayer.create(getContext(), R.raw.jumpsound);
                    jumpSound.setVolume((float) .5, (float) .5);
                    jumpSound.start();
                } else if (!player.isEgg()) {
                    player.setAccel(0);
                    timeIntervalNum = 0;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!player.isEgg()) {
                    player.setAccel(0.008);
                    timeIntervalNum = 0;
                    initSpeed = 0;
                }
                break;
        }
        return true;
    }
}
