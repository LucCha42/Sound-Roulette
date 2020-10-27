package fr.chardonnet_rakotoanosy.soundroulette.activity;

import android.content.Context;
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

import fr.chardonnet_rakotoanosy.soundroulette.R;
import fr.chardonnet_rakotoanosy.soundroulette.Sound;
import fr.chardonnet_rakotoanosy.soundroulette.SoundAdapter;
import fr.chardonnet_rakotoanosy.soundroulette.SoundNameDialog;
import fr.chardonnet_rakotoanosy.soundroulette.storage.SoundJsonFileStorage;

public class BoardActivity extends AppCompatActivity implements SoundNameDialog.SoundNameDialogListener {

    public final static int REQUEST_LOAD_SOUND = 1;

    private SoundAdapter soundAdapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_activity);

        this.context = getApplicationContext();

        buildRecyclerView();
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

        // add new sound
        if (requestCode == REQUEST_LOAD_SOUND && resultCode == RESULT_OK && data != null) {
            if (data.getData() != null) {

                // getting id of the new sound
                int nextId = SoundJsonFileStorage.get(context).getNextId();
                Sound sound = new Sound(nextId, data.getData(), "unnamed");

                // sound naming dialog
                openDialog(sound, true);

                //updating model and view
                SoundJsonFileStorage.get(context).insert(sound);
                soundAdapter.notifyItemInserted(sound.getID());

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
        SoundJsonFileStorage.get(context).update(sound.getID(), sound);
        soundAdapter.notifyItemChanged(sound.getID());
    }

    public void buildRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.sound_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        soundAdapter = new SoundAdapter(context);
        recyclerView.setAdapter(soundAdapter);
        soundAdapter.setOnItemClickListener(new SoundAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int index) {
                // open dialog to rename sound
                openDialog(SoundJsonFileStorage.get(context).find(index), false);
            }
        });
    }

    public void openDialog(Sound sound, boolean isNew) {
        SoundNameDialog dialog = new SoundNameDialog(sound, isNew);
        dialog.show(getSupportFragmentManager(), "soundNameDialog");
    }
}