package com.example.eggrun;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public interface GameItem {
    void draw(Canvas c, Paint p, int phoneX);
    Rect getRect();
    void update();
    String getType();
    void setCollected(boolean isCollected);
    boolean getCollected();
}
