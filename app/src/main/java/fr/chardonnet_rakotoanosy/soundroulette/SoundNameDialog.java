package fr.chardonnet_rakotoanosy.soundroulette;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

public class SoundNameDialog extends AppCompatDialogFragment {

    private EditText editSoundName; // name input
    private Sound sound; // sound to change name
    private SoundNameDialogListener listener;

    public SoundNameDialog(Sound sound) {
        this.sound = sound;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.sound_name_dialog, null);

        builder.setView(view)
                .setTitle("Enter the sound name")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = editSoundName.getText().toString();
                        listener.rename(sound, name);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        editSoundName = view.findViewById(R.id.edit_sound_name);
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
