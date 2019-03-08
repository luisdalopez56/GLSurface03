package com.videos.luisdalopez56.glsurface03;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    SurfaceView surfaceView;
    RelativeLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rl = findViewById(R.id.surfaceView);
        final GLSurface glSurface = new GLSurface(this);
        rl.addView(glSurface);
    }
}
