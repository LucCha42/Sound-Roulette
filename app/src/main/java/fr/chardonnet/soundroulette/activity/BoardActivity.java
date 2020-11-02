package fr.chardonnet.soundroulette.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import fr.chardonnet.soundroulette.R;
import fr.chardonnet.soundroulette.Sound;
import fr.chardonnet.soundroulette.SoundAdapter;
import fr.chardonnet.soundroulette.SoundNameDialog;
import fr.chardonnet.soundroulette.SoundUtility;
import fr.chardonnet.soundroulette.UriUtility;
import fr.chardonnet.soundroulette.storage.SoundJsonFileStorage;

public class BoardActivity extends AppCompatActivity implements SoundNameDialog.SoundNameDialogListener {

    public final static int REQUEST_LOAD_SOUND = 1;

    private SoundAdapter soundAdapter;
    private ActionMode actionMode;
    private MediaPlayer mp;
    private int itemSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_activity);

        mp = new MediaPlayer();
        buildRecyclerView();
    }

    // ---- Menu action bar

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
                Intent addAudioIntent = new Intent();
                addAudioIntent.setType("audio/*");
                addAudioIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                addAudioIntent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(addAudioIntent, REQUEST_LOAD_SOUND);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // adding a new sound
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
                soundAdapter.update();
                soundAdapter.notifyItemInserted(sound.getId());

            } else {
                Toast.makeText(this, "File loading error", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "No sound selected", Toast.LENGTH_LONG).show();
        }
    }

    public void openDialog(Sound sound) {
        SoundNameDialog dialog = new SoundNameDialog(sound);
        dialog.show(getSupportFragmentManager(), "soundNameDialog");
    }

    @Override
    public void rename(Sound sound, String soundName) {
        sound.setName(soundName);
        SoundJsonFileStorage.get(this).update(sound.getId(), sound);
        soundAdapter.update();
        soundAdapter.notifyDataSetChanged();
    }

    // ---- Recycler View

    public void buildRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.sound_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        soundAdapter = new SoundAdapter(this);
        recyclerView.setAdapter(soundAdapter);
        soundAdapter.setOnItemClickListener(new SoundAdapter.OnItemClickListener() {
            @Override
            public void onLongItemClick(int index) {
                itemSelected = index;
                // open contextual menu to rename or delete a sound
                if (actionMode == null) {
                    actionMode = startSupportActionMode(actionModeCallback);
                }
            }

            @Override
            public void onItemClick(int index) {
                itemSelected = index;
                Sound sound = soundAdapter.getSounds().get(itemSelected);
                SoundUtility.playSound(getApplicationContext(), mp, sound);
            }
        });
    }

    // ---- Contextual menu

    private final ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.edit_menu, menu);
            mode.setTitle("Choose an action");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {

                // open dialog to rename sound
                case R.id.rename_button:
                    openDialog(soundAdapter.getSounds().get(itemSelected));
                    mode.finish();
                    return true;

                // delete sound
                case R.id.delete_buton:
                    // getting id of sound to delete
                    int soundId = soundAdapter.getSounds().get(itemSelected).getId();
                    // remove sound from file
                    SoundJsonFileStorage.get(getApplicationContext()).delete(soundId);
                    // refreshing the view
                    soundAdapter.update();
                    soundAdapter.notifyItemRemoved(itemSelected);
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        mp.stop();
    }

}