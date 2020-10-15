package fr.chardonnet_rakotoanosy.soundroulette;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class BoardActivity extends AppCompatActivity {

    public final static int REQUEST_LOAD_SOUND = 1;

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
}