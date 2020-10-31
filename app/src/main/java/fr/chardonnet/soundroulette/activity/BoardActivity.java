package fr.chardonnet.soundroulette.activity;

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

import fr.chardonnet.soundroulette.R;
import fr.chardonnet.soundroulette.Sound;
import fr.chardonnet.soundroulette.SoundAdapter;
import fr.chardonnet.soundroulette.SoundNameDialog;
import fr.chardonnet.soundroulette.UriUtility;
import fr.chardonnet.soundroulette.storage.SoundJsonFileStorage;

public class BoardActivity extends AppCompatActivity implements SoundNameDialog.SoundNameDialogListener {

    public final static int REQUEST_LOAD_SOUND = 1;

    private SoundAdapter soundAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_activity);

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
                int nextId = SoundJsonFileStorage.get(this).getNextId();

                // getting file name and path of the new sound
                String defaultName = UriUtility.getFileName(data.getData(), getContentResolver());

                // creating the new sound
                Sound sound = new Sound(nextId, data.getData(), defaultName);

                // sound naming dialog
                openDialog(sound);

                //updating model and view
                SoundJsonFileStorage.get(this).insert(sound);
                soundAdapter.notifyItemInserted(sound.getId());

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
        SoundJsonFileStorage.get(this).update(sound.getId(), sound);
        soundAdapter.notifyItemChanged(sound.getId());
    }

    public void buildRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.sound_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        soundAdapter = new SoundAdapter(this);
        recyclerView.setAdapter(soundAdapter);
        soundAdapter.setOnItemClickListener(new SoundAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int index) {
                // open dialog to rename sound
                openDialog(SoundJsonFileStorage.get(getApplicationContext()).find(index));
            }
        });
    }

    public void openDialog(Sound sound) {
        SoundNameDialog dialog = new SoundNameDialog(sound);
        dialog.show(getSupportFragmentManager(), "soundNameDialog");
    }
}