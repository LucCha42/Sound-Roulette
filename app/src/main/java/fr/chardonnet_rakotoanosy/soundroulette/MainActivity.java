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
import android.widget.Toast;

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
                Toast.makeText(getApplicationContext(), "random", Toast.LENGTH_LONG).show();
                //TODO take random path in the model
                String selectedPath = "this/is/a/file.mp3";
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
                Intent addIntent = new Intent();
                addIntent.setAction(Intent.ACTION_GET_CONTENT);
                addIntent.addCategory(Intent.CATEGORY_OPENABLE);
                addIntent.setType("audio/*");
                startActivityForResult(Intent.createChooser(addIntent, "Ajout d'un son"), REQUEST_LOAD_SOUND);
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
                File soundFile = new File(data.getData().toString());
                Toast.makeText(getApplicationContext(), data.getData().toString(), Toast.LENGTH_LONG).show();
                //TODO add new file to local storage
            } else {
                Toast.makeText(getApplicationContext(), "File loading error", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "No valid activity result", Toast.LENGTH_LONG).show();
        }
    }
}