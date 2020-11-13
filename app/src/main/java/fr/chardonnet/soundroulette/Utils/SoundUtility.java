package fr.chardonnet.soundroulette.Utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.IOException;

import fr.chardonnet.soundroulette.Sound;

public class SoundUtility {

    public static void playSound(@NonNull Context context, @NonNull MediaPlayer mp, @NonNull Sound sound) {
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
