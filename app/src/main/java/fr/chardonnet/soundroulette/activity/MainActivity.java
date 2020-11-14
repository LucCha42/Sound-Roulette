package fr.chardonnet.soundroulette.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

                if (SoundJsonFileStorage.get(getApplicationContext()).size() > 0) {

                    if (mp.isPlaying()) {
                        // setting no name in view
                        nameView.setText(null);

                        // stop the sound
                        mp.pause();

                    } else {
                        // getting random sound
                        Random r = new Random();
                        int randIndex = r.nextInt(SoundJsonFileStorage.get(getApplicationContext()).size());
                        Sound sound = SoundJsonFileStorage.get(getApplicationContext()).findAll().get(randIndex);

                        // setting name in view
                        nameView.setText(sound.getName());

                        // play the sound
                        SoundUtility.playSound(getApplicationContext(), mp, sound);
                    }

                } else {
                    nameView.setText("");
                    Toast.makeText(getApplicationContext(), "No sound registered", Toast.LENGTH_LONG).show();
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