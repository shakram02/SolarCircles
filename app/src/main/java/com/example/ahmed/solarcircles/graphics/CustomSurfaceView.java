package com.example.ahmed.solarcircles.graphics;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by ahmed on 11/22/17.
 */

public class CustomSurfaceView extends GLSurfaceView {
    private BasicRenderer renderer;


    public CustomSurfaceView(Context context) {
        super(context);
    }

    public void setRenderer(BasicRenderer renderer) {
        super.setRenderer(renderer);
        this.renderer = renderer;
    }

    private static float ONE_THIRD = 1.0f / 3.0f;
    private static float TWO_THIRDS = 2.0f / 3.0f;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.
        // Origin is top left
        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                // reverse direction of rotation above the mid-line
                if (y <= ONE_THIRD * getHeight()) {
                    renderer.moveUp();
                } else if (y >= TWO_THIRDS * getHeight()) {
                    renderer.moveDown();
                }

                // reverse direction of rotation to left of the mid-line
                if (x >= TWO_THIRDS * getWidth()) {
                    renderer.moveRight();
                } else if (x <= ONE_THIRD * getWidth()) {
                    renderer.moveLeft();
                }

                Log.i("CUSTOM_SURFACE_VIEW", String.format("TOUCH X: %s, Y: %s", x, y));
                requestRender();
        }

        return true;
    }
}
