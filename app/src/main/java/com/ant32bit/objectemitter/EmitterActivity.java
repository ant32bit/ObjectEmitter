package com.ant32bit.objectemitter;

import com.ant32bit.objectemitter.util.AnimatedSurfaceView;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class EmitterActivity extends Activity implements View.OnTouchListener, View.OnSystemUiVisibilityChangeListener {

    private static int IMMERSIVE_VISIBILITY = View.SYSTEM_UI_FLAG_IMMERSIVE|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_FULLSCREEN;

    private AnimatedSurfaceView m_asv;
    private boolean m_isImmersive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        m_asv = new AnimatedSurfaceView(this);
        m_asv.setOnTouchListener(this);
        m_asv.setOnSystemUiVisibilityChangeListener(this);
        m_asv.setSystemUiVisibility(IMMERSIVE_VISIBILITY);
        setContentView(m_asv);
    }

    @Override
    protected void onPause() {
        super.onPause();
        m_asv.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        m_asv.resume();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                m_asv.setStartPoint((int)event.getX(), (int)event.getY());

                if (!m_isImmersive) {
                    m_asv.setSystemUiVisibility(IMMERSIVE_VISIBILITY);
                }

                break;
            case MotionEvent.ACTION_UP:
                m_asv.setEndPoint((int)event.getX(), (int)event.getY());
                m_asv.createEmittable();
                break;
            default:
                m_asv.setEndPoint((int)event.getX(), (int)event.getY());
                break;
        }

        return true;
    }

    @Override
    public void onSystemUiVisibilityChange(int visibility) {
        m_isImmersive = (visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) != 0;
    }
}
