package com.example.a10062376.speakerdemo;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//扬声器测试
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnOpenSpeaker, btnCloseSpeaker, btnPlayMusic, btnStopMusic;
    private static int currVolume = 0;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setListener();
        mediaPlayer = MediaPlayer.create(this, R.raw.receivecall);
        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
    }

    private void initView() {
        btnOpenSpeaker = (Button) findViewById(R.id.openSpeaker);
        btnCloseSpeaker = (Button) findViewById(R.id.closeSpeaker);
        btnPlayMusic = (Button) findViewById(R.id.playMusic);
        btnStopMusic = (Button) findViewById(R.id.stopMusic);
    }

    private void setListener() {
        btnOpenSpeaker.setOnClickListener(this);
        btnCloseSpeaker.setOnClickListener(this);
        btnPlayMusic.setOnClickListener(this);
        btnStopMusic.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openSpeaker:
                openSpeaker();
                break;
            case R.id.closeSpeaker:
                closeSpeaker();
                break;
            case R.id.playMusic:
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.start();
                    }
                });
                break;
            case R.id.stopMusic:
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
                break;
        }
    }

    //打开扬声器
    public void openSpeaker() {
        try {
            //判断扬声器是否在打开
            if (audioManager!=null){
                audioManager.setMode(AudioManager.ROUTE_SPEAKER);
                //获取当前通话音量
                currVolume = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
                if (!audioManager.isSpeakerphoneOn()) {
                    audioManager.setSpeakerphoneOn(true);
                    audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                            audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
                            AudioManager.STREAM_VOICE_CALL);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //关闭扬声器
    public void closeSpeaker() {
        try {
            if (audioManager != null) {
                if (audioManager.isSpeakerphoneOn()) {
                    audioManager.setSpeakerphoneOn(false);
                    audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, currVolume,
                            AudioManager.STREAM_VOICE_CALL);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Toast.makeText(context,扬声器已经关闭",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
