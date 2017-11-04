package com.example.ahmed.solarcircles;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class MainScene extends Activity {

    SolarView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glSurfaceView = new SolarView(this);
        setContentView(glSurfaceView);
    }
}
