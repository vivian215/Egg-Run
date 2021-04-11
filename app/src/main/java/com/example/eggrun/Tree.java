package com.example.eggrun;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Tree implements GameItem {
    private int x, y, width, height;
    private Bitmap tree;
    private Rect rect;
    private boolean collected;
    private final int RECT_ERROR;

    public Tree (Resources res, int x) {
        width = 300;
        height = 800;
        tree = BitmapFactory.decodeResource(res, R.drawable.tree);
        tree = Bitmap.createScaledBitmap(tree, width, height, false);

        this.x = x;
        y = 160;

        RECT_ERROR = 20;

        rect = new Rect(x + RECT_ERROR, y, x + width - RECT_ERROR, y + height - RECT_ERROR);

        collected = false;
    }

    public void draw(Canvas c, Paint p, int phoneX) {
        c.drawBitmap(tree, x - phoneX, y, p);
    }

    public Rect getRect() {
        return rect;
    }

    public void update() { }

    public String getType() {
        return "tree";
    }

    public void setCollected(boolean isCollected) {
        collected = isCollected;
    }

    public boolean getCollected() {
        return collected;
    }
}
