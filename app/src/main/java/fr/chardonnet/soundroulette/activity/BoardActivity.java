package fr.chardonnet.soundroulette.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import fr.chardonnet.soundroulette.R;
import fr.chardonnet.soundroulette.Sound;
import fr.chardonnet.soundroulette.SoundAdapter;
import fr.chardonnet.soundroulette.SoundNameDialog;
import fr.chardonnet.soundroulette.Utils.SoundUtility;
import fr.chardonnet.soundroulette.Utils.UriUtility;
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
        int itemId = item.getItemId();

        // go to main activity
        if (itemId == R.id.goto_main_button) {
            Intent gotoIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(gotoIntent);
            return true;
        }
        // open device's file explorer to choose a sound to add
        else if (itemId == R.id.add_button) {
            Intent addAudioIntent = new Intent();
            addAudioIntent.setType("audio/*");
            addAudioIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            addAudioIntent.addCategory(Intent.CATEGORY_OPENABLE);
            addAudioIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(addAudioIntent, REQUEST_LOAD_SOUND);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // adding a new sound after choosing a file
        if (requestCode == REQUEST_LOAD_SOUND && resultCode == RESULT_OK && data != null) {
            // list of all uris
            List<Uri> uris = new ArrayList<>();

            // multiple selection
            if (data.getClipData() != null) {
                // number of items in multiple selection
                int count = data.getClipData().getItemCount();

                // current item id
                int currentItem = 0;

                while (currentItem < count) {
                    // uri temp
                    Uri tmpUri = data.getClipData().getItemAt(currentItem).getUri();

                    // add uri to list
                    uris.add(tmpUri);

                    // next id
                    currentItem ++;
                }
            }
            else if (data.getData() != null) {
                // single selection
                uris.add(data.getData());
            } else {
                Toast.makeText(this, getResources().getString(R.string.board_file_error), Toast.LENGTH_LONG).show();
            }

            for (Uri fileUri : uris) {
                // getting id of the new sound
                int nextId = SoundJsonFileStorage.get(this).getNextId();

                // getting file name and path of the new sound
                String defaultName = UriUtility.getFileName(fileUri, getContentResolver());

                // creating the new sound
                Sound sound = new Sound(nextId, fileUri, defaultName, false);

                // sound naming dialog
                openDialog(sound);

                //updating model and view
                SoundJsonFileStorage.get(this).insert(sound);
                soundAdapter.update();
                soundAdapter.notifyItemInserted(sound.getId());
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.board_file_invalid), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Open the renaming dialog.
     * @param sound     sound to rename
     */
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

    /**
     * Builds the recycler view and its events.
     * Contains the list of sound added by the user.
     * A click on an item plays the corresponding sound.
     * A long click on an item open a contextual menu
     * to choose an action to do on it.
     */
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
            public void onItemClick(int index, ImageView icon) {
                Sound sound = soundAdapter.getSounds().get(index);

                if (mp.isPlaying() && sound.isPlaying()) {
                    // stop the sound
                    sound.setPlaying(false);
                    mp.pause();

                    // enable the screen to go to sleep
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                } else {
                    // reset all other items
                    for (Sound s : soundAdapter.getSounds()) {
                        s.setPlaying(false);
                    }
                    // play the selected sound
                    sound.setPlaying(true);
                    SoundUtility.playSound(getApplicationContext(), mp, sound);

                    // keep the screen awake
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }

                // updating view
                soundAdapter.notifyDataSetChanged();
            }
        });
    }

    // ---- Contextual menu

    private final ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.edit_menu, menu);
            mode.setTitle(getString(R.string.board_contextual_text));
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int itemId = item.getItemId();

            // open dialog to rename sound
            if (itemId == R.id.rename_button) {
                openDialog(soundAdapter.getSounds().get(itemSelected));
                mode.finish();
                return true;
            }
            // delete sound
            else if (itemId == R.id.delete_buton) {// getting id of sound to delete
                int soundId = soundAdapter.getSounds().get(itemSelected).getId();
                // remove sound from file
                SoundJsonFileStorage.get(getApplicationContext()).delete(soundId);
                // refreshing the view
                soundAdapter.update();
                soundAdapter.notifyItemRemoved(itemSelected);
                mode.finish();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
        }
    };

    @Override
    protected void onPause() {
        super.onPause();

        // set all sounds playing to false
        for (Sound s : soundAdapter.getSounds()) {
            s.setPlaying(false);
        }
        soundAdapter.notifyDataSetChanged();

        // killing the current media player every time
        // we change activity or application
        mp.stop();
    }

}