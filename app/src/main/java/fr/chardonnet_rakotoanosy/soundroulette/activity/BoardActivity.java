package fr.chardonnet_rakotoanosy.soundroulette.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fr.chardonnet_rakotoanosy.soundroulette.SoundAdapter;
import fr.chardonnet_rakotoanosy.soundroulette.R;
import fr.chardonnet_rakotoanosy.soundroulette.Sound;

public class BoardActivity extends AppCompatActivity {

    public final static int REQUEST_LOAD_SOUND = 1;

    private ArrayList<Sound> sounds = new ArrayList<>();
    private RecyclerView list;
    private SoundAdapter soundAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_activity);

        //TODO loading sounds
        sounds.add(new Sound("Mozart"));
        sounds.add(new Sound("Beethoven"));
        sounds.add(new Sound("Chopin"));

        // build recycler view
        list = findViewById(R.id.sound_list);
        list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        soundAdapter = new SoundAdapter(sounds);
        list.setAdapter(soundAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.board_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.goto_main_button:
                Intent gotoIntent = new Intent(getApplicationContext(), MainActivity.class);
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
                // updating model and view
                Sound sound = new Sound("son", data.getData());
                sounds.add(sound);
                soundAdapter.notifyItemInserted(sounds.indexOf(sound));
            } else {
                Toast.makeText(this, "File loading error", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "No valid activity result", Toast.LENGTH_LONG).show();
        }
    }
}