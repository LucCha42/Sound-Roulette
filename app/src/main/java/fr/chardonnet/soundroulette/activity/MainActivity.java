package fr.chardonnet.soundroulette.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;

import android.view.WindowManager;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Random;

import fr.chardonnet.soundroulette.R;
import fr.chardonnet.soundroulette.Sound;
import fr.chardonnet.soundroulette.Utils.SoundUtility;
import fr.chardonnet.soundroulette.storage.SoundJsonFileStorage;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mp = new MediaPlayer();

        // random button listener
        findViewById(R.id.random_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getting name to displayed it in view
                TextView nameView = findViewById(R.id.sound_name_playing);

                //getting image displayed in view
                Button buttonView = findViewById(R.id.random_button);
                Drawable d;

                if (SoundJsonFileStorage.get(getApplicationContext()).size() > 0) {

                    // Stops the sound when already playing
                    if (mp.isPlaying()) {
                        // setting no name in view
                        nameView.setText(null);

                        // setting image on play
                        d = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_play_arrow_24);
                        buttonView.setCompoundDrawablesWithIntrinsicBounds(null,null,null,d);

                        // setting button color
                        findViewById(R.id.random_button).setBackgroundColor(getResources().getColor(R.color.colorAccent, getTheme()));

                        // stop the sound
                        mp.pause();

                        // enable the screen to go to sleep
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                    } else {
                        // getting random sound
                        Random r = new Random();
                        int randIndex = r.nextInt(SoundJsonFileStorage.get(getApplicationContext()).size());
                        Sound sound = SoundJsonFileStorage.get(getApplicationContext()).findAll().get(randIndex);

                        // setting name in view
                        nameView.setText(sound.getName());

                        // setting button color
                        findViewById(R.id.random_button).setBackgroundColor(getResources().getColor(R.color.colorAccentDark, getTheme()));

                        // setting image on stop
                        d = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_stop_24);
                        buttonView.setCompoundDrawablesWithIntrinsicBounds(null,null,null,d);

                        // play the sound
                        SoundUtility.playSound(getApplicationContext(), mp, sound);

                        // keep the screen awake
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    }
                } else {
                    nameView.setText("");
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.random_button_invalid), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.goto_board_button) {
            Intent gotoIntent = new Intent(getApplicationContext(), BoardActivity.class);
            startActivity(gotoIntent);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // killing the current media player every time
        // we change activity or application
        mp.stop();
    }

}