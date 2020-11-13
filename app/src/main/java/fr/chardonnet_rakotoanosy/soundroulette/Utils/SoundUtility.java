package fr.chardonnet_rakotoanosy.soundroulette.Utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.Toast;

import java.io.IOException;

import fr.chardonnet_rakotoanosy.soundroulette.Sound;

public class SoundUtility {

    public static void playSound(Context context, MediaPlayer mp, Sound sound) {
        try {
            mp.reset();
            mp.setDataSource(context, sound.getUri());
            mp.prepare();
            mp.start();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Sound playing error", Toast.LENGTH_LONG).show();
        }
    }

}
