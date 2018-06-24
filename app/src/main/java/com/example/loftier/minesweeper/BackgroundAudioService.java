package com.example.loftier.minesweeper;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;

public class BackgroundAudioService extends Service {

    MediaPlayer music_player;
    SharedPreferences setting_pref;


    public BackgroundAudioService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        music_player = MediaPlayer.create(getApplicationContext(),R.raw.background_music);
        music_player.setLooping(true);

        setting_pref=getSharedPreferences("setting_keys",MODE_PRIVATE);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(setting_pref.getBoolean("volume",false))
            music_player.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        music_player.stop();
        music_player.release();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
