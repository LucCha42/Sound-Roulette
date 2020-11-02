package fr.chardonnet.soundroulette;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fr.chardonnet.soundroulette.storage.SoundJsonFileStorage;

public class SoundAdapter extends RecyclerView.Adapter<SoundAdapter.SoundHolder> {

    private OnItemClickListener listener;
    private final Context context;
    // here we use an arrayList to dissociate the adapter model from stored files.
    private ArrayList<Sound> sounds;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class SoundHolder extends RecyclerView.ViewHolder {

        public TextView name;

        public SoundHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            name = itemView.findViewById(R.id.sound_name);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
    }

    public SoundAdapter(Context context) {
        this.context = context;
        this.sounds = (ArrayList<Sound>) SoundJsonFileStorage.get(context).findAll();
    }

    public ArrayList<Sound> getSounds() {
        return sounds;
    }

    /**
     * Is called to refresh the adapter arrayList form files.
     */
    public void update() {
        this.sounds = (ArrayList<Sound>) SoundJsonFileStorage.get(context).findAll();
    }

    @NonNull
    @Override
    public SoundHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_item, parent, false);
        return new SoundHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull SoundHolder holder, int index) {
        holder.name.setText(sounds.get(index).getName());
    }

    @Override
    public int getItemCount() {
        return sounds.size();
    }
}
