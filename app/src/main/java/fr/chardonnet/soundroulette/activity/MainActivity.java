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

import java.io.IOException;
import java.util.Random;

import fr.chardonnet.soundroulette.R;
import fr.chardonnet.soundroulette.Sound;
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

                // getting name displayed in view
                TextView nameView = findViewById(R.id.sound_name_playing);

                if (SoundJsonFileStorage.get(getApplicationContext()).size() > 0) {

                    // getting random sound
                    Random r = new Random();
                    int randIndex = r.nextInt(SoundJsonFileStorage.get(getApplicationContext()).size());
                    Sound sound = SoundJsonFileStorage.get(getApplicationContext()).find(randIndex);

                    // setting name in view
                    nameView.setText(sound.getName());

                    // play the sound
                    try {
                        mp.reset();
                        mp.setDataSource(getApplicationContext(), sound.getUri());
                        mp.prepare();
                        mp.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Sound playing error", Toast.LENGTH_LONG).show();
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
        switch (item.getItemId()) {

            case R.id.goto_board_button:
                Intent gotoIntent = new Intent(getApplicationContext(), BoardActivity.class);
                startActivity(gotoIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}