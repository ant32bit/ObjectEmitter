package com.ant32bit.objectemitter.util;

import com.ant32bit.objectemitter.util.Emittable;
import com.ant32bit.objectemitter.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


/**
 * Created by Anthony on 26/12/2014.
 */
public class AnimatedSurfaceView extends SurfaceView implements Runnable {

    private Emittable aeObjects[] = new Emittable[100];
    private Bitmap abmpObjects[], bmpStartReticle, bmpFinger;
    private Boolean pDrawStart, pDrawEnd, pRunning;
    private int x1, y1, x2, y2, iIndex, iEmittableCount, iPopID;
    private MediaPlayer mpSFX;

    private SurfaceHolder shThis;
    private Thread thrThis;



    public AnimatedSurfaceView(Context context) {
        super(context);

        initialiseMembers(context);
    }

    public AnimatedSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialiseMembers(context);
    }

    private void initialiseMembers(Context context) {
        int aiResourceIDs[] = {
                R.drawable.basketball_small,
                R.drawable.tennis_ball_small,
                R.drawable.football_small,
                R.drawable.soccer_ball_small_2,
                R.drawable.eight_ball_small,
                R.drawable.pokeball_small,
                R.drawable.earth_small,
                R.drawable.fireworks_small,
                R.drawable.awesome_face_small,
                R.drawable.wikipedia_small,
                R.drawable.crazy_tv_small,
                R.drawable.skype_small,
                R.drawable.facebook_small,
                R.drawable.amoeba_small,
                R.drawable.lego_small,
                R.drawable.angry_bird_small,
                R.drawable.cake_small,
                R.drawable.android_small,
                R.drawable.ichi_up_small,
                R.drawable.blue_shell_small };

        abmpObjects = new Bitmap[aiResourceIDs.length];
        int iBmpIndex = 0;
        for (int iResourceID : aiResourceIDs) {
            abmpObjects[iBmpIndex++] = BitmapFactory.decodeResource(getResources(), iResourceID);
        }

        bmpStartReticle = BitmapFactory.decodeResource(getResources(),
                R.drawable.start_reticle);
        bmpFinger = BitmapFactory.decodeResource(getResources(),
                R.drawable.end_reticle);
        iIndex = 0;
        iEmittableCount = 0;
        pDrawStart = pDrawEnd = false;
        mpSFX = MediaPlayer.create(context.getApplicationContext(), R.raw.pop);

        shThis = getHolder();
    }

    public void setStartPoint(int x, int y) {
        x1 = x;
        y1 = y;
        pDrawStart = true;
    }

    public void setEndPoint(int x, int y) {
        x2 = x;
        y2 = y;
        pDrawEnd = true;
    }

    public void createEmittable() {

        pDrawStart = false;
        pDrawEnd = false;

        Emittable e = new Emittable(abmpObjects[iIndex], x1, y1, x2, y2);
        if (!e.isVisible()) return;

        iIndex++;
        if (iIndex >= abmpObjects.length) iIndex = 0;
        aeObjects[iEmittableCount++] = e;
    }

    private void cleanEmittables() {
        int iOffset = 0;

        for (int i = 0; i < iEmittableCount; i++) {
            if (aeObjects[i] == null) {
                iOffset++;
            } else {
                aeObjects[i - iOffset] = aeObjects[i];
            }
        }

        iEmittableCount -= iOffset;
    }

    public void run() {
        while (pRunning) {
            if (!shThis.getSurface().isValid())
                continue;

            cleanEmittables();

            Canvas canvas = shThis.lockCanvas();
            canvas.drawColor(Color.WHITE);

            if (pDrawEnd) {
                Paint p = new Paint();
                p.setColor(Color.RED);
                canvas.drawLine(x1, y1, x2, y2, p);
                canvas.drawBitmap(bmpFinger, x2 - bmpFinger.getWidth()/2, y2 - bmpFinger.getHeight()/2, null);
            }

            if (pDrawStart) {
                canvas.drawBitmap(bmpStartReticle, x1 - bmpStartReticle.getWidth()/2, y1 - bmpStartReticle.getHeight()/2, null);
            }

            Boolean bPlayedSound = false;

            for (int i = 0; i < iEmittableCount; i++) {
                if (aeObjects[i] != null) {
                    if (!aeObjects[i].hasBegun()) {
                        bPlayedSound = true;
                        mpSFX.start();
                    }

                    aeObjects[i].animate(canvas);

                    if (!aeObjects[i].isVisible()) {
                        aeObjects[i] = null;
                    }
                }
            }

            shThis.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        pRunning = false;
    }

    public void resume() {
        pRunning = true;
        thrThis = new Thread(this);
        thrThis.start();
    }
}