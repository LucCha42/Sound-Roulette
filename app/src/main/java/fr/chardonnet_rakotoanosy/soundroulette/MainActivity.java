package fr.chardonnet_rakotoanosy.soundroulette;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button gotoBoardButton;
    private Button randomButton;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // getting buttons
            gotoBoardButton = findViewById(R.id.GotoBoardButton); // auto cast
            randomButton = findViewById(R.id.RandomButton);

        // go to board button listener
        gotoBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BoardActivity.class);
                startActivity(intent);
            }
        });

        // random button listener
        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO random function
            }
        });
    }

}