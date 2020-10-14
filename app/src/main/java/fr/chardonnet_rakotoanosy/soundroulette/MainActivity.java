package fr.chardonnet_rakotoanosy.soundroulette;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button randomButton;

    private ArrayList<Sound> sounds = new ArrayList<Sound>();
    private MediaPlayer mp = new MediaPlayer();

    public final static int REQUEST_LOAD_SOUND = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        randomButton = findViewById(R.id.RandomButton);

        // random button listener
        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO take random path in the model
                String selectedPath = "/fichier/de/son";
                try {
                    mp.setDataSource(selectedPath);
                    mp.prepare();
                    mp.start(); // play the sound
                } catch (IOException e) {
                    e.printStackTrace();
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
                Intent addIntent = new Intent(Intent.ACTION_PICK);
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
        if(requestCode == REQUEST_LOAD_SOUND && requestCode == RESULT_OK) {
            File soundFile = new File(data.getData().toString());
            //TODO add new file to local storage
        }
    }
}