package fr.chardonnet_rakotoanosy.soundroulette.activity;

import android.content.Intent;
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
import fr.chardonnet_rakotoanosy.soundroulette.SoundNameDialog;

public class BoardActivity extends AppCompatActivity implements SoundNameDialog.SoundNameDialogListener {

    public final static int REQUEST_LOAD_SOUND = 1;

    private ArrayList<Sound> sounds = new ArrayList<>();
    private RecyclerView recyclerView;
    private SoundAdapter soundAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_activity);

        buildRecyclerView();

        //TODO loading model from files
        //sounds = (ArrayList<Sound>) getIntent().getSerializableExtra("Sounds");
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
                addIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
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
                Sound sound = new Sound(sounds.size(), "", data.getData());
                // sound naming dialog
                openDialog(sound);
                //updating model and view
                sounds.add(sound);
                soundAdapter.notifyItemInserted(sounds.indexOf(sound));
            } else {
                Toast.makeText(this, "File loading error", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "No valid activity result", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void rename(Sound sound, String soundName) {
        sound.setName(soundName);
        soundAdapter.notifyItemChanged(sounds.indexOf(sound));
    }

    public void buildRecyclerView() {
        recyclerView = findViewById(R.id.sound_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        soundAdapter = new SoundAdapter(sounds);
        recyclerView.setAdapter(soundAdapter);
        soundAdapter.setOnItemClickListener(new SoundAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int index) {
                // open dialog to rename sound
                openDialog(sounds.get(index));
            }
        });
    }

    public void openDialog(Sound sound) {
        SoundNameDialog dialog = new SoundNameDialog(sound);
        dialog.show(getSupportFragmentManager(), "soundNameDialog");
    }
}