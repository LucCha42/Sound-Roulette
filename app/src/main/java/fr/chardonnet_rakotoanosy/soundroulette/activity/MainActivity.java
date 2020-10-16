package fr.chardonnet_rakotoanosy.soundroulette.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import fr.chardonnet_rakotoanosy.soundroulette.R;
import fr.chardonnet_rakotoanosy.soundroulette.Sound;
import fr.chardonnet_rakotoanosy.soundroulette.activity.BoardActivity;

public class MainActivity extends AppCompatActivity {

    private final ArrayList<Sound> sounds = new ArrayList<>();
    private final MediaPlayer mp = new MediaPlayer();

    public final static int REQUEST_LOAD_SOUND = 1;

    public ArrayList<Sound> getSounds() {
        return sounds;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // random button listener
        findViewById(R.id.RandomButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sounds.size() > 0) {

                    // getting random sound
                    Random r = new Random();
                    int randIndex = r.nextInt(sounds.size());
                    Sound sound = sounds.get(randIndex);

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
                    Toast.makeText(getApplicationContext(), "No sound registered", Toast.LENGTH_LONG).show();
                }
            }
        });

        //TODO loading saved sounds in model
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

            case R.id.add_button:
                Intent addIntent = new Intent();
                addIntent.setAction(Intent.ACTION_GET_CONTENT);
                addIntent.addCategory(Intent.CATEGORY_OPENABLE);
                addIntent.setType("audio/*");
                startActivityForResult(addIntent, REQUEST_LOAD_SOUND);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOAD_SOUND && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                // updating model
                Sound sound = new Sound(data.getData());
                sounds.add(sound);
            } else {
                Toast.makeText(this, "File loading error", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "No valid activity result", Toast.LENGTH_LONG).show();
        }
    }
}