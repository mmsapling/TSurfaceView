package com.tylz.surfaceviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity
        extends AppCompatActivity
{
    private TSurfaceView mTSurfaceView;
    private String       mVideoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("tylz", "onCreate");
        setContentView(R.layout.activity_main);

        mVideoPath = "http://www.lejurobot.com/uploads/media/20160613/1465820031.mp4";
        mTSurfaceView = (TSurfaceView) findViewById(R.id.main_tsv);
        mTSurfaceView.setVideoPath(mVideoPath);
        mTSurfaceView.setWaitingImageResourec(R.mipmap.ic_launcher);
    }


    @Override
    protected void onStop() {
        super.onStop();
        mTSurfaceView.setStop();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mTSurfaceView.setContinuePlay();
    }

}
