package com.example.eggrun;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Spike implements GameItem {
    private int x, y;
    private static int width;
    private int height;
    private Bitmap spike;
    Rect rect;
    private boolean collected;
    private final int RECT_ERROR;

    public Spike(Resources res, int x) {
        spike = BitmapFactory.decodeResource(res, R.drawable.spike);

        width = spike.getWidth();
        height = spike.getHeight();

        width /= 3;
        height /= 5;

        spike = Bitmap.createScaledBitmap(spike, width, height, false);

        this.x = x;
        moveTo(230);
        RECT_ERROR = 20;

        this.rect = new Rect(x + RECT_ERROR, y, x + width - RECT_ERROR, y + height - RECT_ERROR);

        collected = false;
    }

    public Spike(Resources res, int x, int y) {
        spike = BitmapFactory.decodeResource(res, R.drawable.spike);

        width = spike.getWidth();
        height = spike.getHeight();

        width /= 3;
        height /= 5;

        spike = Bitmap.createScaledBitmap(spike, width, height, false);

        this.x = x;
        this.y = y;
        RECT_ERROR = 20;

        this.rect = new Rect(x + RECT_ERROR, y, x + width - RECT_ERROR, y + height - RECT_ERROR);

        collected = false;
    }

    public static int getWidth() {
        return width;
    }

    public void moveTo (int distance) {
        this.y = 1080 - distance;
    }

    public Rect getRect() {
        return new Rect(x + RECT_ERROR, y, x + width - RECT_ERROR, y + height - 40);
    }

    public void draw(Canvas c, Paint p, int phoneX) {
        if (x-phoneX < 2000 && x-phoneX > -100) {
            c.drawBitmap(spike, x - phoneX, y, p);
        }
    }

    public void update() { }

    public String getType() {
        return "spike";
    }

    public void setCollected(boolean isCollected) {
        collected = isCollected;
    }

    public boolean getCollected() {
        return collected;
    }
}
