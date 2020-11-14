package fr.chardonnet.soundroulette.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.core.content.ContextCompat;
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

/**
 * Board activity
 */
public class BoardActivity extends AppCompatActivity implements SoundNameDialog.SoundNameDialogListener {
    /**
     * Request code for sound loading
     */
    public final static int REQUEST_LOAD_SOUND = 1;

    /**
     * Sound adapter
     */
    private SoundAdapter soundAdapter;

    /**
     * Action mode
     */
    private ActionMode actionMode;

    /**
     * Media player
     */
    private MediaPlayer mp;
    /**
     * Id of the selected item
     */
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
            //Main button
            case R.id.goto_main_button:
                Intent gotoIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(gotoIntent);
                return true;
            //Add button
            case R.id.add_button:
                //intent
                Intent addAudioIntent = new Intent();
                //audio
                addAudioIntent.setType("audio/*");
                //open document
                addAudioIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                //openable
                addAudioIntent.addCategory(Intent.CATEGORY_OPENABLE);
                //start intent
                startActivityForResult(addAudioIntent, REQUEST_LOAD_SOUND);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // add new sound(s)
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
                Toast.makeText(this, "File loading error", Toast.LENGTH_LONG).show();
            }

            for (Uri fileUri : uris) {

                // getting id of the new sound
                int nextId = SoundJsonFileStorage.get(this).getNextId();

                // getting file name and path of the new sound
                String defaultName = UriUtility.getFileName(fileUri, getContentResolver());

                // creating the new sound
                Sound sound = new Sound(nextId, fileUri, defaultName);

                // sound naming dialog
                openDialog(sound);

                //updating model and view
                SoundJsonFileStorage.get(this).insert(sound);
                soundAdapter.update();
                soundAdapter.notifyItemInserted(sound.getId());

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
            public void onItemClick(int index, ImageView icon) {
                itemSelected = index;
                Sound sound = soundAdapter.getSounds().get(itemSelected);

                if (mp.isPlaying()) {
                    // stop the sound
                    mp.pause();
                    // set the icon of the item
                    Drawable d = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_play_arrow_24);
                    icon.setImageDrawable(d);
                } else {
                    // play the selected sound
                    SoundUtility.playSound(getApplicationContext(), mp, sound);
                    // set the icon of the item
                    Drawable d = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_stop_24);
                    icon.setImageDrawable(d);
                }
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