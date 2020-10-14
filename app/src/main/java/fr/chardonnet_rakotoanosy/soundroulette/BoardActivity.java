package fr.chardonnet_rakotoanosy.soundroulette;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class BoardActivity extends AppCompatActivity {

    public final static int REQUEST_LOAD_SOUND = 1;

    private Button goToMainButton;
    private Button addSongButton;
    private Button addButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
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
                Intent addIntent = new Intent(Intent.ACTION_PICK);
                addIntent.setType("audio/*");
                startActivityForResult(addIntent, REQUEST_LOAD_SOUND);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}




/*

        //Listener Go to main button
        goToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


 */