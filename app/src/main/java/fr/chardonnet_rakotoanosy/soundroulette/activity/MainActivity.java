package fr.chardonnet_rakotoanosy.soundroulette.activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Random;

import fr.chardonnet_rakotoanosy.soundroulette.R;
import fr.chardonnet_rakotoanosy.soundroulette.Sound;
import fr.chardonnet_rakotoanosy.soundroulette.storage.SoundJsonFileStorage;

public class MainActivity extends AppCompatActivity {

    //private final ArrayList<Sound> sounds = new ArrayList<>();
    private Context context;
    private final MediaPlayer mp = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        context = getApplicationContext();

        // random button listener
        findViewById(R.id.RandomButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SoundJsonFileStorage.get(context).size() > 0) {

                    // getting random sound
                    Random r = new Random();
                    int randIndex = r.nextInt(SoundJsonFileStorage.get(context).size());
                    Sound sound = SoundJsonFileStorage.get(context).find(randIndex);

                    // play the sound
                    try {
                        mp.reset();
                        mp.setDataSource(getApplicationContext(), sound.getURI());
                        mp.prepare();
                        mp.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Sound playing error", Toast.LENGTH_LONG).show();
                    }
                } else {
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