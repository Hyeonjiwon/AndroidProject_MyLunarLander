package kr.co.company.mylunarlander;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class MusicService extends Service {

    private static MediaPlayer bgm;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override public void onCreate() {
        super.onCreate();
    }


    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        Log.d("Music", "Service onStart()");

        // Download https://seo6285.tistory.com/212
        bgm = MediaPlayer.create(this, R.raw.music);

        //반복 재생
        bgm.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bgm.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bgm.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                mp.stop();
                mp.release();
            }
        });

    }
}
