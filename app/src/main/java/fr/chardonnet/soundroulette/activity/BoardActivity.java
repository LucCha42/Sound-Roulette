package fr.chardonnet.soundroulette.activity;

import android.content.Intent;
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
import fr.chardonnet.soundroulette.UriUtility;
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
     * Id of the selected item
     */
    private int itemSelected;

    /**
     * On create
     *
     * @param savedInstanceState Saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_activity);

        buildRecyclerView();
    }

    /**
     * On create action menu
     *
     * @param menu Menu created
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.board_menu, menu);
        return true;
    }

    /**
     * On option item selected
     *
     * @param item Item
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // Main button
            case R.id.goto_main_button:
                Intent gotoIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(gotoIntent);
                return true;

            // Add button
            case R.id.add_button:
                // intent
                Intent addAudioIntent = new Intent();
                // audio
                addAudioIntent.setType("audio/*"); 
                // open document
                addAudioIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                // openable
                addAudioIntent.addCategory(Intent.CATEGORY_OPENABLE);
                // multiple selection
                addAudioIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                // start intent
                startActivityForResult(addAudioIntent, REQUEST_LOAD_SOUND);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * On result
     * 
     * @param requestCode Request code
     * @param resultCode Result code, ok or not
     * @param data Data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // add new sound(s)
        if (requestCode == REQUEST_LOAD_SOUND && resultCode == RESULT_OK && data != null)
        {
            // list of all uris
            List<Uri> uris = new ArrayList<>();
            
            // multiple selection
            if (data.getClipData() != null)
            {
                // number of items in multiple selection
                int count = data.getClipData().getItemCount();
                
                // current item id
                int currentItem = 0;
                
                while (currentItem < count)
                {
                    // uri temp
                    Uri tmpUri = data.getClipData().getItemAt(currentItem).getUri();
                    
                    // add uri to list
                    uris.add(tmpUri);
                    
                    // next id
                    currentItem ++;
                }
            }
            else if (data.getData() != null)
            {
                // single selection
                uris.add(data.getData());
            }
            else
            {
                // oops...
                Toast.makeText(this, "File loading error", Toast.LENGTH_LONG).show();
            }
            
            for (Uri fileUri : uris)
            {
                // getting id of the new sound
                int nextId = SoundJsonFileStorage.get(this).getNextId();

                // getting file name and path of the new sound
                String defaultName = UriUtility.getFileName(fileUri, getContentResolver());

                // creating the new sound
                Sound sound = new Sound(nextId, fileUri, defaultName);

                // sound naming dialog
                openDialog(sound);

                // updating model and view
                SoundJsonFileStorage.get(this).insert(sound);
                soundAdapter.notifyItemInserted(sound.getId());
            }
        } else {
            Toast.makeText(this, "No valid activity result", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Rename a sound
     *
     * @param sound Sound to rename
     * @param soundName New name
     */
    @Override
    public void rename(Sound sound, String soundName) {
        sound.setName(soundName);
        SoundJsonFileStorage.get(this).update(sound.getId(), sound);
        soundAdapter.notifyItemChanged(sound.getId());
    }

    /**
     * Build the RecyclerView
     */
    public void buildRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.sound_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        soundAdapter = new SoundAdapter(this);
        recyclerView.setAdapter(soundAdapter);
        soundAdapter.setOnItemClickListener(new SoundAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int index) {
                itemSelected = index;
                // open contextual menu to rename or delete a sound
                if (actionMode == null) {
                    actionMode = startSupportActionMode(actionModeCallback);
                }
            }
        });
    }

    /**
     * Open the edit dialog
     *
     * @param sound Sound to edit
     */
    public void openDialog(Sound sound) {
        SoundNameDialog dialog = new SoundNameDialog(sound);
        dialog.show(getSupportFragmentManager(), "soundNameDialog");
    }

    // Contextual menu
    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
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

                case R.id.rename_button:
                    Toast.makeText(getApplicationContext(), Integer.toString(itemSelected), Toast.LENGTH_SHORT).show();
                    // open dialog to rename sound
                    openDialog(SoundJsonFileStorage.get(getApplicationContext()).find(itemSelected));
                    mode.finish();
                    return true;

                case R.id.delete_buton:
                    //TODO delete sound
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

}
