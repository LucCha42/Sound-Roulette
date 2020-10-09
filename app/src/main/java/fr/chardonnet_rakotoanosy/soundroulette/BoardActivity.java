package fr.chardonnet_rakotoanosy.soundroulette;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BoardActivity extends AppCompatActivity {
    private Button goToMainButton;
    private Button addSongButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        //getting buttons
        goToMainButton = findViewById(R.id.GotoMainButton);
        addSongButton = findViewById(R.id.AddButton);
        //Listener GotoRandomButton
        goToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // random button listener
        addSongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO add function
            }
        });
    }
}