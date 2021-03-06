package fr.chardonnet.soundroulette;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fr.chardonnet.soundroulette.storage.SoundJsonFileStorage;

public class SoundAdapter extends RecyclerView.Adapter<SoundAdapter.SoundHolder> {

    private OnItemClickListener listener;
    private final Context context;
    // here we use an arrayList to dissociate the adapter model from stored files.
    private ArrayList<Sound> sounds;

    public interface OnItemClickListener {
        // trigger the contextual menu
        void onItemClick(int index, ImageView icon);
        // trigger the playing of the corresponding sound
        void onLongItemClick(int index);
    }

    public static class SoundHolder extends RecyclerView.ViewHolder {

        public TextView nameView;
        public ImageView iconView;

        public SoundHolder(@NonNull final View itemView, final OnItemClickListener listener) {
            super(itemView);

            nameView = itemView.findViewById(R.id.sound_name);
            iconView = itemView.findViewById(R.id.play_img);

            // contextual menu listener
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onLongItemClick(position);
                            return true;
                        }
                    }
                    return false;
                }
            });

            // sound playing listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position, iconView);
                        }
                    }
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

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Is called to refresh the adapter arrayList from files.
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
        // name view
        holder.nameView.setText(sounds.get(index).getName());

        // icon view
        Drawable d;
        if (sounds.get(index).isPlaying()) {
            d = ContextCompat.getDrawable(context, R.drawable.ic_baseline_stop_24);
        } else {
            d = ContextCompat.getDrawable(context, R.drawable.ic_baseline_play_arrow_24);
        }
        holder.iconView.setImageDrawable(d);
    }

    @Override
    public int getItemCount() {
        return sounds.size();
    }
}
