package com.example.eggrun;

import android.graphics.Bitmap;
import android.content.res.Resources;
import android.graphics.BitmapFactory;


public class Background {
    private int x=0, y=0;
    private Bitmap background;

    Background (Resources res) {
        background = BitmapFactory.decodeResource(res, R.drawable.bg);
        background = Bitmap.createScaledBitmap(background, 3960, 1080, false);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Bitmap getBackground() {
        return background;
    }

    public void setX(int newX) {
        x = newX;
    }
}
