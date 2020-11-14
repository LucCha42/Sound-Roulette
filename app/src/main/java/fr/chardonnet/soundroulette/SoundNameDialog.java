package fr.chardonnet.soundroulette;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

public class SoundNameDialog extends AppCompatDialogFragment {

    private EditText editSoundName; // name input
    private SoundNameDialogListener listener;
    private final Sound sound; // sound to rename

    public SoundNameDialog(Sound sound) {
        this.sound = sound;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.sound_name_dialog, null);

        builder.setView(view)
                .setTitle(R.string.rename_dialog_title)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!editSoundName.getText().toString().equals("")) {
                            String name = editSoundName.getText().toString();
                            listener.rename(sound, name);
                        } else {
                            String text = getResources().getString(R.string.rename_dialog_invalid);
                            Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        editSoundName = view.findViewById(R.id.edit_sound_name);
        editSoundName.setText(sound.getName());
        editSoundName.requestFocus();
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (SoundNameDialogListener) context;
    }

    public interface SoundNameDialogListener {
        void rename(Sound sound, String soundName);
    }
}
