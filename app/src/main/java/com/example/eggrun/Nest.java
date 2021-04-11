package com.example.eggrun;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Nest implements GameItem {
    private int x, y;
    private int width;
    private int height;
    private Bitmap nest;
    private Rect rect;
    private boolean collected;
    private final int RECT_ERROR;

    public Nest(Resources res, int x) {
        nest = BitmapFactory.decodeResource(res, R.drawable.nest);

        width = nest.getWidth() / 50;
        height = nest.getHeight() / 50;
        nest = Bitmap.createScaledBitmap(nest, width, height, false);

        this.x = x;
        y = 800;
        RECT_ERROR = 20;

        this.rect = new Rect(x + RECT_ERROR, y, x + width - RECT_ERROR, y + height - RECT_ERROR);

        collected = false;
    }

    public Rect getRect() {
        return rect;
    }

    public void draw(Canvas c, Paint p, int phoneX) {
        if (x-phoneX < 2000 && x-phoneX > -100) {
            c.drawBitmap(nest, x - phoneX, y, p);
        }
    }

    public void update() { }

    public String getType() {
        return "nest";
    }

    public void setCollected(boolean isCollected) {
        collected = isCollected;
    }

    public boolean getCollected() {
        return collected;
    }
}
