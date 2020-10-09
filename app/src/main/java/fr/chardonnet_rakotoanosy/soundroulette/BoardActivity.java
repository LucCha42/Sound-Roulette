package fr.chardonnet_rakotoanosy.soundroulette;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class BoardActivity extends AppCompatActivity {
    private Button goToMainButton;
    private Button addSongButton;

    private Button addButton;
    public final static int REQUEST_LOAD_SOUND = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        addButton = findViewById(R.id.AddButton);
        goToMainButton = findViewById(R.id.GotoMainButton);

        //Listener GotoRandomButton
        goToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // add a sound file listener
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("audio/*");
                startActivityForResult(intent, REQUEST_LOAD_SOUND);
            }
        });

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