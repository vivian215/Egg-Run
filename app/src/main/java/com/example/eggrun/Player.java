package com.example.eggrun;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class Player {
    private boolean isEgg;
    private Bitmap egg;
    private Bitmap[] eggs;
    private int x, y;
    private int width;
    private int angleCount, birdCount;
    private int totalAngles;
    private boolean isJumping;
    private double accel;
    private final int RECT_ERROR;
    private Bitmap[] birds;
    private Bitmap bird1, bird2, bird3, bird4;
    private Bitmap poof1, poof2, poof3, poof4, poof5, poof6, poof7, poof8, poof9, poof10;
    private Bitmap[] poofs;
    private boolean isTransition;
    private int poofCount;

    public Player (Resources res) {
        width = 160;
        y = 820;
        x = 250;

        RECT_ERROR = 20;

        egg = BitmapFactory.decodeResource(res, R.drawable.egg);
        egg = Bitmap.createScaledBitmap(egg, width, width, false);

        eggs = new Bitmap[15];
        for (int i = 0; i < 15; i++) {
            eggs[i] = rotateBitmap(egg, (float) (24 * i));
        }
        angleCount = 0;
        birdCount = 0;

        isEgg = true;
        totalAngles = 15;
        isJumping = false;
        accel = -.015;

        bird1 = BitmapFactory.decodeResource(res, R.drawable.bird1);
        bird1 = Bitmap.createScaledBitmap(bird1, 250, 250, false);
        bird2 = BitmapFactory.decodeResource(res, R.drawable.bird2);
        bird2 = Bitmap.createScaledBitmap(bird2, 250, 250, false);
        bird3 = BitmapFactory.decodeResource(res, R.drawable.bird3);
        bird3 = Bitmap.createScaledBitmap(bird3, 250, 250, false);
        bird4 = BitmapFactory.decodeResource(res, R.drawable.bird4);
        bird4 = Bitmap.createScaledBitmap(bird4, 250, 250, false);
        birds = new Bitmap[] {bird1, bird2,bird3, bird4, bird3, bird2};

        poof1 = BitmapFactory.decodeResource(res, R.drawable.poof1);
        poof2 = BitmapFactory.decodeResource(res, R.drawable.poof2);
        poof3 = BitmapFactory.decodeResource(res, R.drawable.poof3);
        poof4 = BitmapFactory.decodeResource(res, R.drawable.poof4);
        poof5 = BitmapFactory.decodeResource(res, R.drawable.poof5);
        poof6 = BitmapFactory.decodeResource(res, R.drawable.poof6);
        poof7 = BitmapFactory.decodeResource(res, R.drawable.poof7);
        poof8 = BitmapFactory.decodeResource(res, R.drawable.poof8);
        poof9 = BitmapFactory.decodeResource(res, R.drawable.poof9);
        poof10 = BitmapFactory.decodeResource(res, R.drawable.poof10);
        poofs = new Bitmap[] {poof1, poof2, poof3, poof4, poof5, poof6, poof7, poof8, poof9, poof10};
        for (int i = 0; i < poofs.length; i++) {
            poofs[i] = Bitmap.createScaledBitmap(poofs[i], width + 300, width + 300, false);
        }
        poofCount = 0;
    }

    public void setIsTransition(boolean newTransition) {
        isTransition = newTransition;
    }

    public boolean isTransition() {
        return isTransition;
    }

    public double getAccel() {
        return accel;
    }

    public void setAccel(double newAccel) {
        accel = newAccel;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setY(int newY) {
        y = newY;
    }

    public boolean getIsJumping() {
        return isJumping;
    }

    public void setIsJumping(boolean newIsJumping) {
        isJumping = newIsJumping;
    }

    public boolean isEgg() {
        return isEgg;
    }

    public void setIsEgg(boolean newIsEgg) {
        isEgg = newIsEgg;
    }

    public void draw(Canvas c, Paint p) {
        Bitmap currPlayer = getPlayer(angleCount, birdCount);
        c.drawBitmap(currPlayer, x, y, p);
        if (isTransition) {
            Log.d("poof", poofCount+"");
            c.drawBitmap(poofs[poofCount], x - 150, y - 150, p);
        }
    }

    public void update(int timerCount) {
        if (timerCount % 2 == 0) {
            angleCount++;
        }
        if (timerCount % 2 == 0) {
            birdCount++;
        }
        if (isTransition) {
            poofCount++;
        }

        if (angleCount >= totalAngles) {
            angleCount = 0;
        }
        if (birdCount >= birds.length) {
            birdCount = 0;
        }

        if (poofCount >= 10) {
            poofCount = 0;
            isTransition = false;
        }

    }

    public Bitmap getPlayer (int angleCount, int birdCount) {
        int n = angleCount;
        int m = birdCount;
        if (isEgg) {
            return eggs[n];
        }
        return birds[m];
    }

    private Bitmap rotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.setRotate(angle,source.getWidth()/2,source.getHeight()/2);

        Bitmap result = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        if (!(angle % 90 == 0)) {
            result = Bitmap.createBitmap(result, (int) ((result.getWidth()-source.getWidth())/ 2.0), (int) ((result.getHeight()-source.getHeight())/ 2.0), source.getWidth(), source.getWidth());
        }
        return result;
    }

    public Rect getRect(int x) {
        return new Rect(x + RECT_ERROR, y, x + width - RECT_ERROR, y + width - RECT_ERROR);
    }
}
