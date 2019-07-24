package com.hfad.mathquiz.utilities;

import android.content.Context;
import android.media.SoundPool;

public class SoundManager {

    private SoundPool pool;
    private Context context;

    public SoundManager(Context context) {
        this.context = context;
        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setMaxStreams(10);
        pool = builder.build();
    }

    public int addSound(int resourceId) {
        return pool.load(context, resourceId, 1);
    }

    public void play(int soundId) {
        pool.play(soundId, 1, 1, 1, 0, 1);
    }
}
