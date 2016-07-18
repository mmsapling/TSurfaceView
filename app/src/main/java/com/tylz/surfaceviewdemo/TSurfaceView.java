package com.tylz.surfaceviewdemo;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;

/*
 *  @项目名：  SurfaceViewDemo 
 *  @包名：    com.tylz.surfaceviewdemo
 *  @文件名:   TSurfaceView
 *  @创建者:   陈选文
 *  @创建时间:  2016/7/18 20:35
 *  @描述：    在Activity中onStop()方法中调用 setStop();
 *  @描述：    在Activity中onRestart()方法中调用setContinuePlay();
 */
public class TSurfaceView
        extends RelativeLayout
{
    private SurfaceView mSurfaceView;
    private ImageView   mIvPlay;
    private MediaPlayer mMediaPlayer;
    private int mCurrentPosition = 0;
    private String      mVideoPath;
    private ProgressBar mPbProgress;
    private boolean isDestroyed = false;
    private ImageView mIvWaiting;

    public TSurfaceView(Context context) {
        super(context, null);
        initView();
    }

    public TSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.view_t_surface, this);
        mSurfaceView = (SurfaceView) view.findViewById(R.id.sv);
        mIvPlay = (ImageView) view.findViewById(R.id.iv_play);
        mIvWaiting = (ImageView) view.findViewById(R.id.iv_waiting);
        mPbProgress = (ProgressBar) view.findViewById(R.id.pb_progress);
        mSurfaceView.getHolder()
                    .addCallback(mCallback);
        mIvPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play(mCurrentPosition);
            }
        });
    }

    private SurfaceHolder.Callback mCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (isDestroyed) {
                play(mCurrentPosition);
                mCurrentPosition = 0;
            }
            isDestroyed = false;
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            isDestroyed = true;
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mCurrentPosition = mMediaPlayer.getCurrentPosition();
                mMediaPlayer.stop();
            }
        }
    };

    private void play(final int mesc) {
        if (TextUtils.isEmpty(mVideoPath)) {
            Toast.makeText(getContext(), "视频路径不存在！", Toast.LENGTH_SHORT)
                 .show();
            return;
        }
        try {
            /*
                播放按钮隐藏不见
                显示加载,显示等待图片
             */
            mIvPlay.setVisibility(View.GONE);
            mPbProgress.setVisibility(View.VISIBLE);
            mIvWaiting.setVisibility(View.VISIBLE);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(mVideoPath);
            mMediaPlayer.setDisplay(mSurfaceView.getHolder());
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mPbProgress.setVisibility(View.GONE);
                    mIvWaiting.setVisibility(View.GONE);
                    mMediaPlayer.start();
                    mMediaPlayer.seekTo(mesc);
                }
            });
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Toast.makeText(getContext(), "视频发生位置错误！", Toast.LENGTH_SHORT)
                         .show();
                    //显示播放控件
                    mIvPlay.setVisibility(View.VISIBLE);
                    return false;
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mCurrentPosition = 0;
                    mIvPlay.setVisibility(View.VISIBLE);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setWaitingImageResourec(int resId) {
        mIvWaiting.setImageResource(resId);
        mIvWaiting.setVisibility(VISIBLE);
    }

    public void setVideoPath(String videoPath) {
        mVideoPath = videoPath;
    }

    public void setStop() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mCurrentPosition = mMediaPlayer.getCurrentPosition();
            mMediaPlayer.pause();
        }
    }

    public void setContinuePlay() {
        if (!isDestroyed) {
            play(mCurrentPosition);
        }
    }
}
