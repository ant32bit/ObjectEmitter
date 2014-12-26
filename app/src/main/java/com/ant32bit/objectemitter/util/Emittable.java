package com.ant32bit.objectemitter.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Anthony on 26/12/2014.
 */
public class Emittable {

    Bitmap bmpObject;
    int x, y, dy, dx;
    Boolean bVisible, bHasBegun;

    public Emittable(Bitmap bmp, int x1, int y1, int x2, int y2) {
        bmpObject = bmp;

        x = x2;
        y = y2;

        dx = (x1 - x2)/30;
        dy = (y1 - y2)/30;

        bHasBegun = false;
        bVisible = true;
        if (dx == 0 && dy == 0) {
            bVisible = false;
        }


    }

    public void animate (Canvas canvas) {
        if (!bVisible) return;

        int drawX = x - bmpObject.getWidth()/2;
        int drawY = y - bmpObject.getHeight()/2;

        int maxX = drawX + bmpObject.getWidth();
        int maxY = drawY + bmpObject.getHeight();

        canvas.drawBitmap(bmpObject, drawX, drawY, null);

        x += dx;
        y += dy;

        if (maxX + dx <= 0 || maxY + dx <= 0 || drawX + dx > canvas.getWidth() || drawY + dy > canvas.getHeight()) {
            bVisible = false;
        }

        bHasBegun = true;
    }

    public boolean hasBegun() {
        return bHasBegun;
    }

    public boolean isVisible() {
        return bVisible;
    }
}
