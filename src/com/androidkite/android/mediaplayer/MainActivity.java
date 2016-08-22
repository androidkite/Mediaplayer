package com.androidkite.android.mediaplayer;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import java.io.IOException;

public class MainActivity extends Activity implements View.OnClickListener
{

    private static final String PLAY_RINGING_PATH = "rkcloud_av_ring.mp3";

    private MediaPlayer mediaPlayer1;
    private MediaPlayer mediaPlayer2;





    private Handler handler;



	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        findViewById(R.id.play1).setOnClickListener(this);
        findViewById(R.id.play2).setOnClickListener(this);
        findViewById(R.id.stop1).setOnClickListener(this);
        findViewById(R.id.stop2).setOnClickListener(this);

        mediaPlayer1  = new MediaPlayer();
        mediaPlayer2 = new MediaPlayer();

        HandlerThread handlerThread = new HandlerThread("background");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());

	}
    private void setModeToNormal() {
        AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        am.setSpeakerphoneOn(true);
        am.setMode(AudioManager.MODE_NORMAL);
//		am.setSpeakerphoneOn(false);
//		am.setStreamMute(AudioManager.STREAM_VOICE_CALL, false);
    }

    private void stopSystemMusicPlayer() {
        Intent intent = new Intent("com.android.music.musicservicecommand");
        intent.putExtra("command", "stop");
      sendBroadcast(intent);
    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.play1:

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        play1();
//                    }
//                }).start();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        play1();
                    }
                });
                break;
            case R.id.stop1:

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        stop1();
                    }
                });


//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        stop1();
//                    }
//                }).start();

                break;
            case R.id.play2:
                play2();
                break;
            case R.id.stop2:
                stop2();
                break;
        }
    }



    private void play1()
    {
        stopSystemMusicPlayer();
        setModeToNormal();
        mediaPlayer1.reset();
        mediaPlayer1.setLooping(true);
        mediaPlayer1.setAudioStreamType(AudioManager.STREAM_RING);
        try {
            AssetFileDescriptor descriptor  =getApplicationContext().getResources().getAssets().openFd(PLAY_RINGING_PATH);
            if(null!=descriptor && null!=descriptor.getFileDescriptor() && descriptor.getLength()>0){
                 boolean   fileExist = true;
            }

            mediaPlayer1.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            mediaPlayer1.prepare();
            mediaPlayer1.start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    private void stop1()
    {
        if (mediaPlayer1 != null && mediaPlayer1.isPlaying())
        {
            mediaPlayer1.stop();
        }
        AudioManager mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setMode(AudioManager.MODE_NORMAL);
        mAudioManager.setSpeakerphoneOn(false);
    }


    private void play2()
    {
//        mediaPlayer2.reset();
//        mediaPlayer2.setAudioStreamType(AudioManager.STREAM_RING);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Test");
        builder.setContentText("Test Play mp3");
        builder.setSmallIcon(R.drawable.ic_launcher);
        Notification notification = builder.build();
        notification.sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.rkcloud_chat_sound_custom);
//        notification.defaults = Notification.DEFAULT_ALL;


        notification.flags = Notification.FLAG_AUTO_CANCEL;


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0,notification);
    }

    private void stop2() {
//        if (mediaPlayer2 != null && mediaPlayer2.isPlaying()) {
//            mediaPlayer2.stop();
//        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }


}
